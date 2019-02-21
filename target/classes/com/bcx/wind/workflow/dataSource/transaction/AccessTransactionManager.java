package com.bcx.wind.workflow.dataSource.transaction;

import com.bcx.wind.workflow.exception.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class AccessTransactionManager {

    private static final Logger logger = LoggerFactory.getLogger(AccessTransactionManager.class);

    /**
     * 线程本地操作
     */
    private final ThreadLocal<Connection>  local = new ThreadLocal<>();


    private AccessTransactionManager(){

    }

    private static class TransactionManager{
        private static AccessTransactionManager accessTransactionManager = new AccessTransactionManager();
    }

    public static AccessTransactionManager getInstance(){
        return TransactionManager.accessTransactionManager;
    }


    /**
     * 为当前线程设置access
     *
     * @param connection  连接
     */
    public void setConnection(Connection connection){
        this.local.set(connection);
    }


    /**
     * 获取连接
     *
     * @return  connection
     */
    public Connection getConnection(){
        return this.local.get();
    }


    /**
     * 提交事务
     */
    public void commit(){
        try {
            this.local.get().commit();
        } catch (SQLException e) {
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e.getMessage());
        }
    }


    /**
     * 回滚事务
     */
    public void rollback(){
        try {
            this.local.get().rollback();
        } catch (SQLException e) {
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e.getMessage());
        }
    }


    /**
     * 关闭连接
     */
    public  void close(){
        try {
            this.local.get().close();
        } catch (SQLException e) {
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e.getMessage());
        }
    }

}
