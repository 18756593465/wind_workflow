package com.bcx.wind.workflow.core;

import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.TaskInstance;

import java.util.List;

/**
 * 执行任务模块
 *
 * @author zhanglei
 */
public interface TaskService {

    /**
     * 新增任务实例
     *
     * @param taskInstance   任务实例
     * @return               任务I
     */
    String   createNewTask(TaskInstance taskInstance);



    /**
     * 更新任务实例
     * @param taskInstance   任务实例
     * @return               更新结果
     */
    int   updateTask(TaskInstance taskInstance);


    /**
     * 为指定任务实例添加任务变量
     *
     * @param key      键
     * @param value    值
     * @return         添加结果
     */
    int   addTaskVariable(String taskId,String key, Object value);


    /**
     * 为指定任务删除任务变量
     * @param taskId   任务ID
     * @param key      键
     * @return         删除结果
     */
    int   removeTaskVariable(String taskId,String key);


    /**
     * 通过任务ID 删除任务实例，包含删除任务审批人
     * @param taskId    任务ID
     * @return          删除结果
     */
    int   removeById(String taskId);


    /**
     * 通过流程实例号 删除任务  包括审批人
     *
     * @param orderId     流程实例号
     * @return            删除结果
     */
    int   removeByOrderId(String orderId);


    /**
     * 通过任务Ids 删除多个任务  包含审批人
     *
     * @param taskIds   任务Id集合
     * @return          删除结果
     */
    int   removeByTaskIds(List<String> taskIds);


    /**
     * 通过任务ID 删除 所属审批人
     * @param taskId     任务ID
     * @return           删除结果
     */
    int   removeActorByTaskId(String taskId);


    /**
     * 通过任务IDs 删除 所属审批人
     * @param taskIds    任务ids
     * @return           删除结果
     */
    int   removeActorByTaskIds(List<String> taskIds);


    /**
     * 对指定任务，添加审批人  更新也用这个接口进行操作
     *
     * @param taskId   任务ID
     * @param actor    任务审批人
     * @return         添加结果
     */
    int   addActor(String taskId,List<String> actor);


    /**
     * 对指定任务添加单个审批人
     *
     * @param taskId   任务ID
     * @param actor    审批人
     * @return         添加结果
     */
    int   addActor(String taskId,String actor);


    /**
     * 通过任务id  和审批人ID 删除审批人
     *
     * @param taskId   任务ID
     * @param actor    审批人
     * @return         删除结果
     */
    int   removeActor(String taskId,String actor);



    /**
     * 通过任务ID查询审批人集合
     *
     * @param taskId  任务ID
     * @return        审批人集合
     */
    List<String>  getActorByTaskId(String taskId);

    /**
     * 通过任务id集合查询任务审批人集合
     *
     * @param taskIds  任务id集合
     * @return  任务审批人集合
     */
    List<String>  getActorByTaskIds(String[] taskIds);



    /**
     * 通过任务id查询任务
     *
     * @param taskId  任务id
     * @return        任务实例
     */
    TaskInstance  getTaskById(String taskId);


    /**
     * 过滤查询任务
     *
     * @param filter  过滤条件
     * @return        任务集合
     */
    List<TaskInstance>  queryList(QueryFilter filter);


    /**
     * 分页查询任务实例
     *
     * @param filter  过滤条件
     * @param page    分页条件
     * @return        查询结果
     */
    List<TaskInstance>  queryList(QueryFilter filter, FlowPage<TaskInstance> page);



}


