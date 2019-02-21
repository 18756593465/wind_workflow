package com.bcx.wind.workflow.spring;

import com.bcx.wind.workflow.AbstractAccess;
import com.bcx.wind.workflow.db.DBhelper;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

/**
 * spring实现的数据处理器
 *
 * @author zhanglei
 */
public class SpringAccess extends AbstractAccess {

    /**
     * jdbcTemplate
     */
    private JdbcTemplate jdbcTemplate;

    /**
     * 是否提交
     */
    private boolean autoCommit;


    public SpringAccess(){}

    public  void setAutoCommit(boolean autoCommit){
        this.autoCommit = autoCommit;
    }


    public void setDataSource(DataSource dataSource){
        if(this.jdbcTemplate==null || this.jdbcTemplate.getDataSource()!=dataSource){
            this.jdbcTemplate = new JdbcTemplate(dataSource);
            if(!ObjectHelper.isEmpty(this.jdbcTemplate)) {
                this.paging = DBhelper.createPaging(getConnection());
            }
        }
    }

    public DataSource getDataSource(){
        Assert.isTrue("spring environment lack dataSource ! ",ObjectHelper.isEmpty(jdbcTemplate) || ObjectHelper.isEmpty(jdbcTemplate.getDataSource()));
        return jdbcTemplate.getDataSource();
    }


    @Override
    public int saveOrUpdate(String sql, Object[] args) {
        return this.jdbcTemplate.update(sql,args);
    }

    @Override
    public <T> List<T> query(String sql, Object[] args, Class<T> clazz) {
        return this.jdbcTemplate.query(sql,new BeanPropertyRowMapper<>(clazz),args);
    }

    @Override
    public long queryCount(String sql, Object[] args) {
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
    public Connection getConnection() {

        try {
            Connection conn = getDataSource().getConnection();
            conn.setAutoCommit(autoCommit);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WorkflowException(e.getMessage());
        }
    }
}
