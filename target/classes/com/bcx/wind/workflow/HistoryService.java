package com.bcx.wind.workflow;

import com.bcx.wind.workflow.core.ActiveHistoryService;
import com.bcx.wind.workflow.core.CompleteHistoryService;
import com.bcx.wind.workflow.core.OrderHistoryService;

/**
 * 历史模块
 *
 * @author zhanglei
 */
public interface HistoryService {

    /**
     * 执行历史模块
     * @return ActiveHistoryService
     */
    ActiveHistoryService activeHistoryService();



    /**
     * 完成历史模块
     * @return CompleteHistoryService
     */
    CompleteHistoryService completeHistoryService();


    /**
     * 历史流程履历模块
     * @return  流程流程履历
     */
    OrderHistoryService  orderHistoryService();
}
