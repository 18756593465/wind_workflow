package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.AccessService;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.OrderService;
import com.bcx.wind.workflow.core.constant.OrderType;
import com.bcx.wind.workflow.entity.OrderBusiness;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import com.bcx.wind.workflow.message.MessageHelper;
import com.bcx.wind.workflow.message.MsgConstant;

import java.util.LinkedList;
import java.util.List;

import static com.bcx.wind.workflow.core.constant.Constant.JSON;
import static com.bcx.wind.workflow.message.MsgConstant.w002;
import static com.bcx.wind.workflow.message.MsgConstant.w003;

/**
 * 流程实例模块
 */
public class OrderServiceImpl extends AccessService implements OrderService {

    private static final String ORDER = "order";

    public OrderServiceImpl(Access access) {
        super(access);
    }

    @Override
    public List<OrderInstance> queryList(QueryFilter filter) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w001),filter);
        return access.selectOrderInstanceList(filter);
    }

    @Override
    public List<OrderInstance> queryList(QueryFilter filter, FlowPage<OrderInstance> page) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w001),filter);
        return access.selectOrderInstanceList(filter,page);
    }

    @Override
    public OrderInstance queryOne(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ORDER),id);
        return access.getOrderInstanceById(id);
    }

    @Override
    public OrderInstance queryRunOne(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ORDER),id);
        QueryFilter filter = new QueryFilter()
                .setOrderId(id)
                .setStatus(OrderType.RUN);
        List<OrderInstance> orderInstances = access.selectOrderInstanceList(filter);
        if(!ObjectHelper.isEmpty(orderInstances)){
            return orderInstances.get(0);
        }
        return null;
    }

    @Override
    public OrderInstance queryStopOne(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ORDER),id);
        QueryFilter filter = new QueryFilter()
                .setOrderId(id)
                .setStatus(OrderType.STOP);
        List<OrderInstance> orderInstances = access.selectOrderInstanceList(filter);
        if(!ObjectHelper.isEmpty(orderInstances)){
            return orderInstances.get(0);
        }
        return null;
    }

    @Override
    public String insert(OrderInstance orderInstance) {
        checkOrderInstance(orderInstance);
        if(ObjectHelper.isEmpty(orderInstance.getId())) {
            orderInstance.setId(ObjectHelper.primaryKey());
        }
        orderInstance.setCreateTime(TimeHelper.getNow());
        orderInstance.setVersion(1);
        orderInstance.setVariable(JSON);
        orderInstance.setData(JSON);

        access.insertOrderInstance(orderInstance);
        return orderInstance.getId();
    }

    @Override
    public int update(OrderInstance orderInstance) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ORDER),orderInstance.getId());
        checkOrderInstance(orderInstance);
        if(ObjectHelper.isEmpty(orderInstance.getVariable())){
            orderInstance.setVariable(JSON);
        }
        if(ObjectHelper.isEmpty(orderInstance.getData())){
            orderInstance.setData(JSON);
        }

        return access.updateOrderInstance(orderInstance);
    }

    @Override
    public int addOrderVariable(String orderId, String key, Object value) {
        Assert.hasEmpty("orderInstance variable key or value is null!",key,value);

        OrderInstance orderInstance = checkOrderId(orderId);
        orderInstance.addValue(key,value);
        return access.updateOrderInstance(orderInstance);
    }

    @Override
    public int removeOrderVariable(String orderId, String key) {
        Assert.hasEmpty("orderInstance variable key is null!",key);

        OrderInstance orderInstance = checkOrderId(orderId);
        orderInstance.removeKey(key);

        return access.updateOrderInstance(orderInstance);
    }


    /**
     * 校验orderId
     */
    private OrderInstance  checkOrderId(String orderId){
        Assert.notEmpty(MessageHelper.getMsg(w002,"orderInstance"),orderId);

        OrderInstance orderInstance = queryOne(orderId);
        Assert.notEmpty("orderId is not exist",orderInstance);
        return orderInstance;
    }

    @Override
    public int deleteById(String id) {
        Assert.notEmpty(MessageHelper.getMsg(MsgConstant.w002,ORDER),id);
        return access.removeOrderInstanceById(id);
    }

    @Override
    public int insertOrderBusiness(List<String> businessIds, String orderId,String system) {
        List<OrderBusiness> businesses = new LinkedList<>();
        for(String businessId : businessIds){
            businesses.add(new OrderBusiness().setBusinessId(businessId).setOrderId(orderId).setSystem(system));
        }
        return access.insertOrderBusiness(businesses);
    }


    @Override
    public int removeOrderBusinessByOrderId(String orderId) {
        return access.removeOrderBusinessByOrderId(orderId);
    }

    @Override
    public List<OrderBusiness> queryOrderBusiness(QueryFilter filter) {
        return access.selectOrderBusinessList(filter);
    }

    @Override
    public int deleteByParentId(String parentOrderId) {
        return access.removeOrderInstanceByParentId(parentOrderId);
    }

    private void checkOrderInstance(OrderInstance instance){
        Assert.hasEmpty(MessageHelper.getMsg(w003,ORDER),instance.getProcessId(),
                instance.getStatus(),instance.getCreateUser());

    }
}
