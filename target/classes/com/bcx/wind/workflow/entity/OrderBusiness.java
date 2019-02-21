package com.bcx.wind.workflow.entity;

/**
 * 工作流业务数据
 *
 * @author zhanglei
 */
public class OrderBusiness {

    /**
     * 流程实例号
     */
    private String orderId;

    /**
     * 业务数据
     */
    private String businessId;

    private String system;

    public String getSystem() {
        return system;
    }

    public OrderBusiness setSystem(String system) {
        this.system = system;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderBusiness setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getBusinessId() {
        return businessId;
    }

    public OrderBusiness setBusinessId(String businessId) {
        this.businessId = businessId;
        return this;
    }
}
