package com.bcx.wind.workflow;

import com.bcx.wind.workflow.core.ActiveHistoryService;
import com.bcx.wind.workflow.core.CompleteHistoryService;
import com.bcx.wind.workflow.core.OrderHistoryService;

/**
 * 历史模块
 */
public class HistoryServiceImpl implements HistoryService {

    private ActiveHistoryService activeHistoryService;
    private CompleteHistoryService completeHistoryService;
    private OrderHistoryService orderHistoryService;

    public HistoryServiceImpl(ActiveHistoryService activeHistoryService, CompleteHistoryService completeHistoryService,
                              OrderHistoryService orderHistoryService){
        super();
        this.activeHistoryService = activeHistoryService;
        this.completeHistoryService = completeHistoryService;
        this.orderHistoryService = orderHistoryService;
    }

    @Override
    public ActiveHistoryService activeHistoryService() {
        return activeHistoryService;
    }

    @Override
    public CompleteHistoryService completeHistoryService() {
        return completeHistoryService;
    }

    @Override
    public OrderHistoryService orderHistoryService() {
        return orderHistoryService;
    }
}
