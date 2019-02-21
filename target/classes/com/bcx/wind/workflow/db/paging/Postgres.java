package com.bcx.wind.workflow.db.paging;


import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.db.DBhelper;

public class Postgres implements Paging {

    public  Postgres(){}



    @Override
    public StringBuilder buildPageSql(StringBuilder sql, FlowPage page) {
        if(page==null){
            return sql;
        }

        int pageSize = DBhelper.getPageSize(page.getPageSize());
        int pageNum = DBhelper.getPageNum(page.getPageNum());

        int limit = pageNum*pageSize;
        int offset = limit-pageSize;

        return sql.append(" limit "+limit+" offset "+offset);
    }
}
