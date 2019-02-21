package com.bcx.wind.workflow.access.db;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.List;

public class OrderInstanceAccessTest extends BaseTest {

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

        OrderInstance instance = instance();

        int ret = access.insertOrderInstance(instance);
        assert ret >= 1;
    }


    @Test
    public void update(){
        OrderInstance instance = instance();
        access.insertOrderInstance(instance);

        instance.setSystem("eqms");
        int ret = access.updateOrderInstance(instance);
        assert ret >= 1;
    }

    @Test
    public void delete(){
        OrderInstance instance = instance();
        access.insertOrderInstance(instance);

        int ret = access.removeOrderInstanceById(instance.getId());
        assert ret >= 1;
    }

    @Test
    public void queryOne(){
        OrderInstance instance = instance();
        access.insertOrderInstance(instance);

        OrderInstance orderInstance = access.getOrderInstanceById(instance.getId());
        assert orderInstance!=null;
    }

    @Test
    public void query(){
        List<OrderInstance> instances = access.selectOrderInstanceList(new QueryFilter());
        System.out.println(instances.size());
    }

    @Test
    public void queryPage(){
        FlowPage<OrderInstance> page = new FlowPage<>();
        List<OrderInstance> instances = access.selectOrderInstanceList(new QueryFilter(),page);
        System.out.println(instances.size());
    }
}
