package com.bcx.wind.workflow.access.db;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.OrderBusiness;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class OrderBusinessAccessTest extends BaseTest {


    @Test
    public void orderBusinessInsert(){

        OrderBusiness business = new OrderBusiness()
                .setOrderId(ObjectHelper.primaryKey())
                .setBusinessId(ObjectHelper.primaryKey())
                .setSystem("DMS");
        int ret = access.insertOrderBusiness(business);
        assert ret >= 1;
        commit();
    }


    @Test
    public void insertList() {

        OrderBusiness business = new OrderBusiness()
                .setOrderId(ObjectHelper.primaryKey())
                .setBusinessId(ObjectHelper.primaryKey())
                .setSystem("DMS");
        OrderBusiness business1 = new OrderBusiness()
                .setOrderId(ObjectHelper.primaryKey())
                .setBusinessId(ObjectHelper.primaryKey())
                .setSystem("DMS");

        List<OrderBusiness> businessList = new LinkedList<>();
        businessList.add(business);
        businessList.add(business1);

        int ret = access.insertOrderBusiness(businessList);

        assert ret >= 1;
    }

    @Test
    public void removeOrderId(){
        OrderBusiness business = new OrderBusiness()
                .setOrderId(ObjectHelper.primaryKey())
                .setBusinessId(ObjectHelper.primaryKey())
                .setSystem("DMS");

        access.insertOrderBusiness(business);

        int ret = access.removeOrderBusinessByOrderId(business.getOrderId());
        assert ret >= 1;
    }

    @Test
    public void removeBusinessId(){
        OrderBusiness business = new OrderBusiness()
                .setOrderId(ObjectHelper.primaryKey())
                .setBusinessId(ObjectHelper.primaryKey())
                .setSystem("DMS");

        access.insertOrderBusiness(business);

        int ret = access.removeOrderBusinessByBusinessId(business.getBusinessId(),business.getSystem());

        assert ret >= 1;
    }

    @Test
    public void search(){
        QueryFilter filter = new QueryFilter();
        List<OrderBusiness> orderBusinesses = access.selectOrderBusinessList(filter);
        System.out.println(orderBusinesses.size());
    }
}
