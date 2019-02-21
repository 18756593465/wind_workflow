package com.bcx.wind.workflow.jdbc;

import com.bcx.wind.workflow.AbstractAccess;
import com.bcx.wind.workflow.dataSource.transaction.AccessTransactionManager;
import com.bcx.wind.workflow.db.DBhelper;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * jdbc  自定义数据源处理
 *
 * @author zhanglei
 */
public class JdbcAccess extends AbstractAccess {

    private static final Logger logger = LoggerFactory.getLogger(JdbcAccess.class);

    private final QueryRunner queryRunner = new QueryRunner(true);

    /**
     * 事务管理器
     */
    protected AccessTransactionManager transactionManager;


    /**
     * 数据源
     */
    protected DataSource dataSource;

    public JdbcAccess(){}

    public JdbcAccess(DataSource dataSource){
        super();
        Assert.nonEmpty(dataSource);
        this.dataSource = dataSource;
        this.transactionManager = AccessTransactionManager.getInstance();
        if(!ObjectHelper.isEmpty(this.dataSource)){
            this.paging = DBhelper.createPaging(getConnection());
        }
    }


    @Override
    public int saveOrUpdate(String sql, Object[] args) {
        Connection conn = getConnection();
        try {
            return queryRunner.update(conn,sql,args);
        } catch (SQLException e) {

            if(logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
            return 0;
        }

    }




    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> query(String sql, Object[] args,Class<T> clazz) {

        Connection conn = getConnection();

        try {
            return this.queryRunner.query(conn,sql,new FlowResultHandler<>(clazz),args);
        } catch (SQLException e) {

            if(logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
            throw new WorkflowException(e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long queryCount(String sql, Object[] args) {
        Connection connection = getConnection();
        try {
            return (long)this.queryRunner.query(connection,sql,new FlowResultCountHandler(),args) ;
        } catch (SQLException e) {
            if(logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
        }
        return 0;
    }

    @Override
    public <T> T getSingleton(String sql, Object[] args, Class<T> clazz) {
        List<T> list = query(sql,args,clazz);
        if(!ObjectHelper.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }


    @Override
    public int queryMaxVersionProcess(String processName) {
        return 0;
    }

    @Override
    public synchronized Connection getConnection() {
        Connection conn = transactionManager.getConnection();
        if (conn == null) {
            try {
                conn = dataSource.getConnection();
            } catch (SQLException e) {
                logger.info(e.getMessage(), e);
                throw new WorkflowException(e.getMessage());
            }
            transactionManager.setConnection(conn);
            return conn;
        }
        return conn;
    }

}
