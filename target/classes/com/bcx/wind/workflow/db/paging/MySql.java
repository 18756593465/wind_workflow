package com.bcx.wind.workflow.db.paging;

import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.db.DBhelper;
import com.bcx.wind.workflow.helper.ObjectHelper;

public class MySql implements Paging {


    @Override
    public StringBuilder buildPageSql(StringBuilder sql, FlowPage page) {
        if(ObjectHelper.isEmpty(sql)){
            return sql;
        }

        int pageSize = DBhelper.getPageSize(page.getPageSize());
        int pageNum = DBhelper.getPageNum(page.getPageNum());

        int start = (pageNum-1) * pageSize;

        sql = sql.append(" limit "+start+" , "+pageSize+" ;");
        return sql;
    }
}
