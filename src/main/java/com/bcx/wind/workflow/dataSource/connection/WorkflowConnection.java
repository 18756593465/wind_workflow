package com.bcx.wind.workflow.dataSource.connection;

import com.bcx.wind.workflow.dataSource.dataSource.WorkflowDataSource;
import com.bcx.wind.workflow.helper.Assert;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * 数据库连接
 */
public class WorkflowConnection implements InvocationHandler {

    /**
     * 连接获取时间
     */
    private long startTime;

    /**
     * 连接创建时间
     */
    private long createTime;

    /**
     * 真实的connection
     */
    private Connection realConnection;

    /**
     * 代理的connection
     */
    private Connection proxyConnection;

    /**
     * 工作流数据源
     */
    private WorkflowDataSource dataSource;

    /**
     * 连接是否有效
     */
    private boolean isEnable;

    private static final String CLOSE = "close";


    public WorkflowConnection(WorkflowDataSource dataSource,Connection connection){
        this.realConnection = connection;
        this.createTime = System.currentTimeMillis();
        this.dataSource = dataSource;
        this.isEnable = true;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),new Class[]{Connection.class},this);
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public void setRealConnection(Connection realConnection) {
        this.realConnection = realConnection;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    public void setProxyConnection(Connection proxyConnection) {
        this.proxyConnection = proxyConnection;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(WorkflowDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isEnable() {
        return isEnable &&  this.realConnection!=null && this.dataSource.pingConnection(this);
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果调用的方法为关闭，则使用代理的关闭方法
        if(method.getName().hashCode()==CLOSE.hashCode() || method.getName().equals(CLOSE)){
            this.dataSource.pushConnection(this);
            return null;
        }else{
            Assert.isTrue("the connection is useless!",!this.isEnable);
            return method.invoke(realConnection,args);
        }
    }
}
