package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.OrderHistoryInstance;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.List;

public class OrderHistoryServiceTest extends BaseTest {

    private OrderHistoryInstance instance(){
        return new OrderHistoryInstance()
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
        OrderHistoryInstance instance = instance();

        int ret = engine.historyService().orderHistoryService().insert(instance);
        assert ret >= 1;
    }

    @Test
    public void update(){
        OrderHistoryInstance instance  = instance();
        engine.historyService().orderHistoryService().insert(instance);

        instance.setSystem("eqms");
        int ret = engine.historyService().orderHistoryService().update(instance);
        assert ret >= 1;
    }

    @Test
    public void delete(){
        OrderHistoryInstance instance  = instance();
        engine.historyService().orderHistoryService().insert(instance);

        int ret = engine.historyService().orderHistoryService().deleteById(instance.getId());
        assert ret >= 1;
    }

    @Test
    public void queryOne(){
        OrderHistoryInstance instance  = instance();
        engine.historyService().orderHistoryService().insert(instance);

        OrderHistoryInstance orderHistoryInstance = engine.historyService().orderHistoryService().queryOne(instance.getId());
        assert orderHistoryInstance!=null;
    }

    @Test
    public void queryList(){
        List<OrderHistoryInstance> instances = engine.historyService().orderHistoryService().queryList(new QueryFilter());
        System.out.println(instances.size());
    }


    @Test
    public void queryPage(){
        FlowPage<OrderHistoryInstance> page = new FlowPage<>();

        List<OrderHistoryInstance> list = engine.historyService().orderHistoryService().queryList(new QueryFilter(),page);
        System.out.println(list.size());
    }
}
