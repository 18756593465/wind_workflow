package com.bcx.wind.workflow.jdbc;

import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FlowResultCountHandler implements ResultSetHandler {


    @Override
    public Object handle(ResultSet resultSet) throws SQLException {
        long count = 0;
        while(resultSet.next()){
            count = (long) resultSet.getObject(1 );
            break;
        }
        return count;
    }
}
