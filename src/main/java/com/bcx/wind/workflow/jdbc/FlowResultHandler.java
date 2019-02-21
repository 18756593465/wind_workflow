package com.bcx.wind.workflow.jdbc;

import com.bcx.wind.workflow.helper.JsonHelper;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class FlowResultHandler<T> implements ResultSetHandler {

    private final Class<T> clazz ;

    public FlowResultHandler(Class<T> clazz){
        this.clazz = clazz;
    }

    @Override
    public Object handle(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        //列数量
        int columns = metaData.getColumnCount();
        List<Map<String,Object>> datas = new LinkedList<>();
        while(resultSet.next()){
            Map<String,Object> data = new HashMap<>();
            for(int i=1; i<=columns ; i++){
                String columnName = metaData.getColumnName(i);
                Object object = resultSet.getObject(columnName);
                columnName = toHump(columnName);
                data.put(columnName,object);
            }
            datas.add(data);
        }

        if(clazz.isAssignableFrom(Map.class)){
            return datas;
        }
        return JsonHelper.coverObject(datas,List.class,clazz);
    }


    /**
     * 下划线转驼峰
     *
     * @param field 下划线字段
     * @return      驼峰字符串
     */
    private static String toHump(String field) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        String str = field.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer builder = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(builder, matcher.group(1).toUpperCase());
        }

        matcher.appendTail(builder);
        return builder.toString();
    }

}
