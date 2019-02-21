package com.bcx.wind.workflow.dataSource.dataSource;

import com.bcx.wind.workflow.dataSource.connection.ConnectionPool;
import com.bcx.wind.workflow.dataSource.connection.WorkflowConnection;
import com.bcx.wind.workflow.helper.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;

import static java.util.logging.Logger.GLOBAL_LOGGER_NAME;


/**
 * 工作流数据源
 *
 * @author zhanglei
 */
public class WorkflowDataSource implements DataSource {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowDataSource.class);

    //数据库驱动
    private String driver;

    //地址
    private String url;

    //用户名
    private String user;

    //密码
    private String password;

    /**
     * 连接池
     */
    private final ConnectionPool connectionPool = ConnectionPool.build(this);;

    //是否自动提交
    private boolean autoCommit = false;

    //最大活跃连接数
    private int maxActiveSize = 100;

    //最大闲置数
    private int maxLeisureSize = 100;

    //最大连接等待时间 单位毫秒
    private long maxWaitTime = 20000;

    //是否测试连接 默认否
    private boolean enablePing = false;

    //阻塞等待时间
    private long blockWaitTime = 20000;

    //尝试连接次数
    private int  tryConnectionSecond = 3;

    /**
     * 测试连接sql语句
     */
    private static final String PING_QUERY = "select 1";


    public WorkflowDataSource(String driver,String url,String user,String password){
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public int getTryConnectionSecond() {
        return tryConnectionSecond;
    }

    public void setTryConnectionSecond(int tryConnectionSecond) {
        this.tryConnectionSecond = tryConnectionSecond;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public long getBlockWaitTime() {
        return blockWaitTime;
    }

    public void setBlockWaitTime(long blockWaitTime) {
        this.blockWaitTime = blockWaitTime;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public int getMaxActiveSize() {
        return maxActiveSize;
    }

    public void setMaxActiveSize(int maxActiveSize) {
        this.maxActiveSize = maxActiveSize;
    }

    public int getMaxLeisureSize() {
        return maxLeisureSize;
    }

    public void setMaxLeisureSize(int maxLeisureSize) {
        this.maxLeisureSize = maxLeisureSize;
    }

    public long getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(long maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public boolean isEnablePing() {
        return enablePing;
    }

    public void setEnablePing(boolean enablePing) {
        this.enablePing = enablePing;
    }

    /**
     * 从连接池中拿出连接
     */
    public WorkflowConnection popConnection() throws SQLException {
        WorkflowConnection connection = null;
        //无效连接次数
        int invalidSecond = 0;

        while (connection==null) {
            synchronized (connectionPool) {
                //获取空闲连接池
                List<WorkflowConnection> leisurePools = this.connectionPool.getLeisurePool();
                if (leisurePools.isEmpty()) {
                    //空闲为空，活跃连接池
                    List<WorkflowConnection> activePools = this.connectionPool.getActivePool();
                    //是否达到最大连接数
                    if (activePools.size() < maxActiveSize) {
                        //如果没有达到，生成新的连接放置到活跃连接池中
                        try {
                            connection = new WorkflowConnection(this, getConnection(this.user, this.password));
                        } catch (SQLException e) {
                            if (logger.isDebugEnabled()) {
                                logger.debug(e.getMessage(), e);
                            }
                        }
                    } else {
                        //否则，获取最新的连接池，查看是否连接超时
                        connection = activePools.get(0);
                        if (System.currentTimeMillis() - connection.getStartTime() > this.maxWaitTime) {
                            //从连接池中删除
                            connection = activePools.remove(0);
                            //回滚提交
                            if (connection.getRealConnection().getAutoCommit()) {
                                connection.getRealConnection().rollback();
                            }
                        } else {
                            //阻塞等待
                            try {
                                connectionPool.wait(this.blockWaitTime);
                                logger.debug(" Thread ["+Thread.currentThread().getId()+"] has unable to pop connection , wait now !");
                            } catch (Exception e) {
                                if (logger.isDebugEnabled()) {
                                    logger.debug(e.getMessage(), e);
                                }
                            }
                        }
                    }
                } else {
                    //直接获取空闲连接池
                    connection = leisurePools.remove(0);
                }

                if (connection != null) {
                    if (connection.isEnable()) {
                        //如果连接获取成功，添加到活跃线程池中
                        connection.setCreateTime(System.currentTimeMillis());
                        connection.setDataSource(this);
                        connection.setStartTime(System.currentTimeMillis());
                        this.connectionPool.getActivePool().add(connection);
                    } else {
                        invalidSecond++;
                        Assert.isTrue("Too many attempts to connect, please try again later",invalidSecond >= this.tryConnectionSecond);
                    }
                }
            }
        }

        Assert.notEmpty("try attempts fail !",connection);
        return connection;
    }


    /**
     * 使用连接完毕，归还连接
     */
    public void pushConnection(WorkflowConnection connection) throws SQLException {

        synchronized (connectionPool) {
            this.connectionPool.getActivePool().remove(connection);

            //如果连接尚未提交 直接回滚
            if (!connection.getRealConnection().getAutoCommit()) {
                connection.getRealConnection().rollback();
            }

            //如果闲置线程池数量已达最大值，则不添加，删除连接
            if (connectionPool.getLeisurePool().size() >= maxLeisureSize) {
                connection.getRealConnection().close();
                connection.setEnable(false);
            } else {
                connectionPool.getLeisurePool().addLast(connection);
                //唤醒
                connectionPool.notifyAll();
                logger.debug(" Thread ["+Thread.currentThread().getId()+"] back connection , being notifyAll waiting Thread !");
            }
        }
    }


    /**
     * 测试连接
     */
    public boolean pingConnection(WorkflowConnection connection){
        if(this.enablePing){
            Connection conn = connection.getRealConnection();
            PreparedStatement statement =null;
            ResultSet resultSet = null;
            try {
                if(!conn.isClosed()){
                    statement = conn.prepareStatement(PING_QUERY);
                    resultSet = statement.executeQuery();
                    if(logger.isDebugEnabled()){
                        logger.debug("connection ping success! "+conn);
                    }
                    return true;
                }
            } catch (Exception e) {
                if(logger.isDebugEnabled()){
                    logger.debug(e.getMessage());
                }
            }finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (resultSet != null) {
                        resultSet.close();
                    }
                }catch (SQLException e) {
                    if(logger.isDebugEnabled()){
                        logger.debug(e.getMessage());
                    }
                }
            }
        }else{
            return true;
        }
        return false;
    }

    private void connectionConfig(Connection connection){
        try {
            connection.setAutoCommit(this.autoCommit);
        } catch (SQLException e) {
            if(logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
        }
    }


    @Override
    public Connection getConnection() throws SQLException {
        return popConnection().getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        try {
            Class.forName(this.driver);
            Connection connection =  DriverManager.getConnection(this.url,username,password);
            connectionConfig(connection);
            if(logger.isDebugEnabled()){
                logger.debug("pop connection success ! "+connection);
            }
            return connection;
        } catch (ClassNotFoundException e) {
            if(logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
        }
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return java.util.logging.Logger.getLogger(GLOBAL_LOGGER_NAME);
    }


}
