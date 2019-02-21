package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.OrderBusiness;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.*;

public class OrderServiceTest extends BaseTest {

    private OrderInstance instance(){
        return new OrderInstance()
                .setId(ObjectHelper.primaryKey())
                .setProcessId(ObjectHelper.primaryKey())
                .setStatus("run")
                .setCreateUser("admin")
                .setCreateTime("2019-01-02 12:23:12")
                .setExpireTime("")
                .setVersion(1)
                .setVariable("{}")
                .setData("{}")
                .setSystem("dms");
    }

    @Test
    public void add(){
        OrderInstance orderInstance = instance();
        String orderId = engine.runtimeService().orderService().insert(orderInstance);
        assert orderId!=null;
    }

    @Test
    public void update(){
        OrderInstance orderInstance = instance();
        engine.runtimeService().orderService().insert(orderInstance);

        orderInstance.setSystem("eqms");
        int ret = engine.runtimeService().orderService().update(orderInstance);
        assert ret >= 1;
    }


    @Test
    public void  delete(){
        OrderInstance orderInstance = instance();
        engine.runtimeService().orderService().insert(orderInstance);

        int ret = engine.runtimeService().orderService().deleteById(orderInstance.getId());
        assert ret >= 1;
    }

    @Test
    public void queryOne(){
        OrderInstance orderInstance = instance();
        engine.runtimeService().orderService().insert(orderInstance);

        OrderInstance instance = engine.runtimeService().orderService().queryOne(orderInstance.getId());
        assert instance!=null;
    }


    @Test
    public void query(){
        engine.runtimeService().orderService().queryList(new QueryFilter());
    }

    @Test
    public void queryPage(){
        FlowPage<OrderInstance> page = new FlowPage<>();
        List<OrderInstance> list = engine.runtimeService().orderService().queryList(new QueryFilter(),page);
        System.out.println(list.size());
    }

    @Test
    public void insertOrderBusiness(){
        int ret = engine.runtimeService().orderService().insertOrderBusiness(Collections.singletonList("100141"),"2492424","dms");
        assert ret>=1;
    }

    @Test
    public void removeOrderVariable(){
        OrderInstance orderInstance = instance();
        Map<String,Object> args = new HashMap<>();
        args.put("123","abc");
        orderInstance.setVariableMap(args);
        engine.runtimeService().orderService().insert(orderInstance);

        int ret = engine.runtimeService().orderService().removeOrderVariable(orderInstance.getId(),"123");
        assert ret >= 1;
    }

    @Test
    public void removeBusinessByOrderId(){
        engine.runtimeService().orderService().insertOrderBusiness(Collections.singletonList("100141"),"2492424","dms");

        int ret = engine.runtimeService().orderService().removeOrderBusinessByOrderId("2492424");
        assert ret >= 1;
    }


    @Test
    public void addOrderVariable(){
        OrderInstance orderInstance = instance();
        engine.runtimeService().orderService().insert(orderInstance);

        int ret= engine.runtimeService().orderService().addOrderVariable(orderInstance.getId(),"123","abc");
        assert ret >= 1;
    }

    @Test
    public void queryOrderBusiness(){
        List<OrderBusiness> businesses = engine.runtimeService().orderService().queryOrderBusiness(new QueryFilter());
        System.out.println(businesses.size());
    }






}
