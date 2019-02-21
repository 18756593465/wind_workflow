package com.bcx.wind.workflow.core;

import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.OrderBusiness;
import com.bcx.wind.workflow.entity.OrderInstance;

import java.util.List;

/**
 * 流程实例模块
 *
 * @author zhanglei
 */
public interface OrderService {


    List<OrderInstance>  queryList(QueryFilter filter);


    List<OrderInstance>  queryList(QueryFilter filter, FlowPage<OrderInstance> page);


    /**
     * 通过流程实例ID（流程实例号） 查询流程实例
     * @param id   流程实例号
     * @return     流程实例
     */
    OrderInstance  queryOne(String id);


    String insert(OrderInstance orderInstance);


    int update(OrderInstance orderInstance);


    /**
     * 为指定流程 添加流程变量
     * @param orderId   流程实例号
     * @param key       键
     * @param value     值
     * @return          添加结果
     */
    int addOrderVariable(String orderId,String key,Object value);


    /**
     * 为指定流程，删除流程变量
     * @param orderId  流程实例号
     * @param key      键
     * @return         删除结果
     */
    int removeOrderVariable(String orderId,String key);

    int deleteById(String id);


    int  insertOrderBusiness(List<String> businessIds,String orderId,String system);


    int  removeOrderBusinessByOrderId(String orderId);


    List<OrderBusiness> queryOrderBusiness(QueryFilter filter);


    int deleteByParentId(String parentOrderId);


}
