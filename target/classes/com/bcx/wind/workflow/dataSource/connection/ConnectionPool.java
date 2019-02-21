package com.bcx.wind.workflow.dataSource.connection;

import javax.sql.DataSource;
import java.util.LinkedList;

/**
 * 数据库连接池
 *
 * @author zhanglei
 *
 */
public class ConnectionPool {

    /**
     * 活跃连接池
     */
    private LinkedList<WorkflowConnection>  activePool = new LinkedList<>();

    /**
     * 闲置连接池
     */
    private LinkedList<WorkflowConnection>  leisurePool = new LinkedList<>();


    /**
     * 数据源
     */
    private DataSource dataSource;


    private static ConnectionPool pool = new ConnectionPool();


    private ConnectionPool(){}

    public static ConnectionPool build(DataSource dataSource){
        pool.setDataSource(dataSource);
        return pool;
    }

    public LinkedList<WorkflowConnection> getActivePool() {
        return activePool;
    }

    public void setActivePool(LinkedList<WorkflowConnection> activePool) {
        this.activePool = activePool;
    }

    public LinkedList<WorkflowConnection> getLeisurePool() {
        return leisurePool;
    }

    public void setLeisurePool(LinkedList<WorkflowConnection> leisurePool) {
        this.leisurePool = leisurePool;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
