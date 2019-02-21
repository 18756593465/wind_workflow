package com.bcx.wind.workflow.core.flow;

/**
 * 任务模型接口
 *
 * @author zhanglei
 */
public interface TaskModel extends NodeModel{


    /**
     * 父节点
     * @return  父节点
     */
    NodeModel parentNode();
}
