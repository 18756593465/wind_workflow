package com.bcx.wind.workflow.core;

import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.OrderHistoryInstance;

import java.util.List;

public interface OrderHistoryService{


    List<OrderHistoryInstance> queryList(QueryFilter filter);


    List<OrderHistoryInstance>  queryList(QueryFilter filter, FlowPage<OrderHistoryInstance> page);


    /**
     * 通过流程实例ID（流程实例号） 查询流程实例
     * @param id   流程实例号
     * @return     流程实例
     */
    OrderHistoryInstance  queryOne(String id);


    int insert(OrderHistoryInstance orderHistoryInstance);


    int update(OrderHistoryInstance orderHistoryInstance);

    int deleteById(String id);

}
