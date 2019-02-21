package com.bcx.wind.workflow.core;

import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.ProcessDefinition;

import java.io.InputStream;
import java.util.List;

/**
 * 流程定义模块
 *
 * @author zhanglei
 */
public interface ProcessService {


    /**
     * 更新添加
     * @param processContent   工作流定义xml
     * @return                 流程定义ID
     */
    String  deploy(String processContent,String system);


    /**
     * 更新添加
     *
     * @param inputStream   工作流定义xml输入流
     * @return              流程定义ID
     */
    String  deploy(InputStream inputStream,String system);


    /**
     * 通过模型名称获取最高版本模型
     *
     * @param processName  模型名称
     * @return             最高版本流程模型
     */
    ProcessDefinition getProcessDefinitionByName(String processName);


    /**
     * 查询模型
     *
     *
     * @param queryFilter         流程定义查询参数
     * @return                    流程定义集合
     */
    List<ProcessDefinition>  queryList(QueryFilter queryFilter, FlowPage<ProcessDefinition> flowPage);


    /**
     * 过滤查询
     *
     * @param queryFilter  过滤器
     * @return             查询的流程定义集合
     */
    List<ProcessDefinition>  queryList(QueryFilter queryFilter);


    /**
     * 通过流程定义ID 查询流程定义
     *
     * @param processId  流程定义ID
     * @return           流程定义
     */
    ProcessDefinition getProcessById(String processId);


    /**
     * 通过流程定义ID 删除流程定义
     * @param processId     流程定义ID
     * @return              删除结果
     */
    int   deleteById(String processId);


    /**
     * 通过模型ID 发布流程
     * @param processId    流程定义ID
     * @return             发布结果
     */
    int release(String processId);


    /**
     * 回收流程定义
     * @param processId  流程定义ID
     * @return           回收结果
     */
    int recovery(String processId);
}
