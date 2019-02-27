package com.bcx.wind.workflow.core;

import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.TransferResult;
import com.bcx.wind.workflow.core.pojo.TransferVariable;

import java.util.List;

public interface WorkflowService {
    /**
     * 全局转办  ，所有流程数据中的审批人同时转办
     *
     * @param oldUser   旧审批人
     * @param newUser   新审批人
     * @return          转办结果  1成功  2失败
     */
    TransferResult transfer(DefaultUser oldUser , DefaultUser newUser);


    /**
     * 将指定流程定义下所有的流程数据中的审批人进行转办
     *
     * @param processId   流程定义ID
     * @param oldUser     旧审批人
     * @param newUser     新审批人
     * @return            转办结果
     */
    TransferResult   transfer(String processId , DefaultUser oldUser , DefaultUser newUser);


    /**
     * 将指定业务数据下的流程实例中审批人进行转办处理
     *
     * @param businessIds  业务数据ID集合
     * @param oldUser      旧审批人
     * @param newUser      新审批人
     * @return             转办结果
     */
    TransferResult   transfer(List<String> businessIds , DefaultUser oldUser , DefaultUser newUser);


    /**
     * 通过自定义参数进行审批人转办
     *
     * @param variable   转办参数
     * @return           转办结果
     */
    TransferResult   transfer(TransferVariable variable);

}
