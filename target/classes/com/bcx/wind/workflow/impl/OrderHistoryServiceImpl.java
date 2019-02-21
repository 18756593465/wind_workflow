package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.AccessService;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.OrderHistoryService;
import com.bcx.wind.workflow.entity.OrderHistoryInstance;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.message.MessageHelper;
import com.bcx.wind.workflow.message.MsgConstant;

import java.util.List;

/**
 * 历史流程实例
 *
 * @author zhanglei
 */
public class OrderHistoryServiceImpl extends AccessService implements OrderHistoryService {

    private static final String ORDER_HISTORY = "orderHistory";

    public OrderHistoryServiceImpl(Access access) {
        super(access);
    }

    @Override
    public List<OrderHistoryInstance> queryList(QueryFilter filter) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w001),filter);
        return access.selectOrderHistoryInstanceList(filter);
    }

    @Override
    public List<OrderHistoryInstance> queryList(QueryFilter filter, FlowPage<OrderHistoryInstance> page) {

        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w001),filter);
        return access.selectOrderHistoryInstanceList(filter,page);
    }

    @Override
    public OrderHistoryInstance queryOne(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ORDER_HISTORY),id);
        return access.getOrderHistoryInstanceById(id);
    }

    @Override
    public int insert(OrderHistoryInstance orderHistoryInstance) {
        return access.insertOrderHistoryInstance(orderHistoryInstance);
    }

    @Override
    public int update(OrderHistoryInstance orderHistoryInstance) {
        return access.updateOrderHistoryInstance(orderHistoryInstance);
    }


    @Override
    public int deleteById(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ORDER_HISTORY),id);
        return access.removeOrderHistoryInstanceById(id);
    }
}
