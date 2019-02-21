package com.bcx.wind.workflow.db;

import com.bcx.wind.workflow.db.paging.*;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.helper.ObjectHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库相关工具
 *
 * @author zhanglei
 */
public class DBhelper {

    private static final String H2 = "h2";
    private static final String MYSQL = "mysql";
    private static final String ORACLE = "oracle";
    private static final String POSTGRESQL = "postgresql";
    private static final String MICROSOFT = "microsoft";


    /**
     * 数据库类型集合
     *
     * @return  数据库属性
     */
    private static Properties createDBPageSql(){
        Properties properties = new Properties();
        properties.setProperty("H2",H2);
        properties.setProperty("MySQL",MYSQL);
        properties.setProperty("Oracle",ORACLE);
        properties.setProperty("PostgreSQL",POSTGRESQL);
        properties.setProperty("Microsoft SQL Server",MICROSOFT);
        return properties;
    }


    public static Paging createPaging(Connection connection){
        //数据库类型属性
        Properties dbProperties = createDBPageSql();
        try {
            String dbName = connection.getMetaData().getDatabaseProductName();
            if(H2.equals(dbProperties.get(dbName))){
                return new H2();
            }else if(MYSQL.equals(dbProperties.get(dbName))){
                return new MySql();
            }else if(ORACLE.equals(dbProperties.get(dbName))){
                return new Oracle();
            }else if(POSTGRESQL.equals(dbProperties.get(dbName))){
                return new Postgres();
            }else if(MICROSOFT.equals(dbProperties.get(dbName))){
                return new Microsoft();
            }else{
                throw new WorkflowException("sorry ! "+dbName+ " database wind workflow is not support! please use h2 or mysql or postgres or oracle or microsoft sql server!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new WorkflowException(e.getMessage());
        }
    }


    public static int getPageSize(int pageSize){
        if(!ObjectHelper.isEmpty(pageSize) && pageSize>0){
            return pageSize;
        }
        return 10;
    }

    public static int getPageNum(int pageNum){
        if(!ObjectHelper.isEmpty(pageNum) && pageNum>0){
            return pageNum;
        }
        return 1;
    }

}
