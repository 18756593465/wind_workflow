package com.bcx.wind.workflow.mybatis;

import com.bcx.wind.workflow.dataSource.transaction.AccessTransactionManager;
import com.bcx.wind.workflow.db.DBhelper;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.jdbc.JdbcAccess;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * mybatis  access 数据处理器
 *
 * @author zhanglei
 */
public class MybatisAccess extends JdbcAccess {

    private static final Logger logger = LoggerFactory.getLogger(MybatisAccess.class);

    /**
     * sqlSessionFactory
     */
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * 是否提交
     */
    private boolean autoCommit = false;


    public MybatisAccess(SqlSessionFactory sqlSessionFactory){
        super();
        this.sqlSessionFactory = sqlSessionFactory;
        this.dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
        this.transactionManager = AccessTransactionManager.getInstance();
        if(!ObjectHelper.isEmpty(dataSource)){
            this.paging = DBhelper.createPaging(getConnection());
        }
    }

    public void setAutoCommit(boolean autoCommit){
        this.autoCommit = autoCommit;
    }


    @Override
    public Connection getConnection() {
        synchronized (sqlSessionFactory) {
            Connection connection =  transactionManager.getConnection();
            if (ObjectHelper.isEmpty(connection)) {
                connection = sqlSessionFactory.openSession().getConnection();
                Assert.notEmpty("mybatis sqlSessionFactory find connection error !", connection);
                try {
                    connection.setAutoCommit(autoCommit);
                } catch (Exception var3) {
                    var3.printStackTrace();
                    if (logger.isDebugEnabled()) {
                        logger.debug(var3.getMessage(), var3);
                    }
                    throw new WorkflowException(var3.getMessage());
                }
                transactionManager.setConnection(connection);
            }
            return connection;
        }
    }
}
