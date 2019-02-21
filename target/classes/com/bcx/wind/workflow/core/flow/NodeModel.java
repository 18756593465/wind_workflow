package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.NodeType;
import com.bcx.wind.workflow.core.pojo.Task;

import java.util.List;

/**
 * 节点模型
 *
 * @author zhanglei
 */
public interface NodeModel {

    /**
     * 节点名称
     *
     * @return  名称
     */
    String name();


    /**
     * 节点显示名称
     *
     * @return  显示名称
     */
    String  displayName();


    /**
     * 是否在子流程中
     * @return  boolean
     */
    void inRouter(boolean inRouter);


    /**
     * 节点类型
     * @return  节点类型
     */
    NodeType nodeType();


    /**
     * 获取提交路线
     *
     * @return  路线集合
     */
    List<Path>  path();


    /**
     * 后续节点
     *
     * @return  后续节点模型
     */
    List<NodeModel> nextNodes();


    /**
     * 前面节点
     */
    List<NodeModel>  lastNodes();


    /**
     * 回去后续任务节点
     * @return  后续任务节点
     */
    List<NodeModel>   nextTaskNodes();


    /**
     * 获取前面任务节点
     * @return   前面任务节点
     */
    List<NodeModel>   lastTaskNodes();


    /**
     * 通过提交路线查询路线后的任务节点
     *
     * @param submitLine  提交路线
     * @return            路线后的任务节点
     */
    List<NodeModel>   getSubmitLineNodes(String submitLine);


    /**
     * 执行
     */
    void execute();


    /**
     * 下一个
     */
    void next(String submitLine);



    /**
     * 设置执行数据
     * @param actuator  流程执行器数据
     */
    void actuator(Actuator actuator);


    /**
     * 设置正在执行的任务
     * @param task    任务
     */
    void  task(Task task);


    /**
     * 父节点
     * @return  父节点
     */
    NodeModel parentNode();
}
