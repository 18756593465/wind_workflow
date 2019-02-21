package com.bcx.wind.workflow.access.db;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.OrderHistoryInstance;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.List;

public class HistoryOrderInstanceAccessTest extends BaseTest {

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
    public void  add(){
        OrderHistoryInstance historyInstance =instance();

        int ret = access.insertOrderHistoryInstance(historyInstance);
        assert ret >= 1;
    }


    @Test
    public void  update(){
        OrderHistoryInstance historyInstance =instance();

        access.insertOrderHistoryInstance(historyInstance);
        historyInstance.setSystem("eqms");

        int ret = access.updateOrderHistoryInstance(historyInstance);
        assert ret >= 1;
    }

    @Test
    public void  remove(){
        OrderHistoryInstance historyInstance =instance();

        access.insertOrderHistoryInstance(historyInstance);
        int ret = access.removeOrderHistoryInstanceById(historyInstance.getId());

        assert ret >= 1;
    }

    @Test
    public void queryOne(){
        OrderHistoryInstance historyInstance =instance();

        access.insertOrderHistoryInstance(historyInstance);
        OrderHistoryInstance instance = access.getOrderHistoryInstanceById(historyInstance.getId());

        assert instance!=null;
    }

    @Test
    public void query(){
        List<OrderHistoryInstance> instanceList = access.selectOrderHistoryInstanceList(new QueryFilter());
        System.out.println(instanceList.size());
    }

    @Test
    public void queryPage(){
        FlowPage<OrderHistoryInstance> page = new FlowPage<>();

        List<OrderHistoryInstance> instances = access.selectOrderHistoryInstanceList(new QueryFilter(),page);

        System.out.println(instances.size());
    }
}
