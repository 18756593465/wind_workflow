package com.bcx.wind.workflow;

import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.*;

import java.sql.Connection;
import java.util.List;

/**
 * 针对数据库操作
 *
 * @author zhanglei
 */
public interface Access {


    /*--------------------流程定义-----------------*/


    /**
     * 通过流程定义名称查询最高版本
     *
     * @param processName  流程定义名称
     * @return             版本
     */
    int  queryMaxVersionProcess(String processName);

    /**
     * 新增流程定义
     * @param processDefinition  流程定义
     */
    int  addProcess(ProcessDefinition processDefinition);


    /**
     * 更新流程定义
     * @param processDefinition   流程定义
     */
    int  updateProcess(ProcessDefinition processDefinition);


    /**
     * 通过模型ID删除模型
     *
     * @param processId  模型ID
     */
    int  removeProcessById(String processId);


    /**
     * 查询流程定义
     * @param filter  过滤条件
     * @param page    分页查询
     * @return        流程定义集合
     */
    List<ProcessDefinition>  selectProcessList(QueryFilter filter, FlowPage<ProcessDefinition> page);


    /**
     * 查询流程定义
     *
     * @param filter  过滤条件
     * @return        流程定义集合
     */
    List<ProcessDefinition>  selectProcessList(QueryFilter filter);


    /**
     * 通过流程定义ID 查询流程定义
     *
     * @param processId  流程定义ID
     * @return           流程定义
     */
    ProcessDefinition  getProcessDefinitionById(String processId);


    /*--------------------流程定义-----------------*/


    /*--------------------正在执行履历-----------------*/


    /**
     * 新增正在执行履历
     *
     * @param activeHistory   正在执行履历
     * @return   新增结果
     */
    int  insertActiveHistory(ActiveHistory activeHistory);


    /**
     * 通过id 删除正在执行履历
     *
     * @param id  主键ID
     * @return    删除结果
     */
    int  removeActiveHistoryById(String id);


    /**
     * 通过id数组删除执行履历
     *
     * @param ids id数组
     * @return    删除结果
     */
    int  removeActiveHistoryByIds(List<String> ids);


    /**
     * 更新正在执行履历
     *
     * @param activeHistory  正在执行履历实体信息
     *
     * @return  更新结果
     */
    int  updateActiveHistory(ActiveHistory activeHistory);


    /**
     * 查询正在执行履历
     *
     * @param filter   查询条件
     * @return         查询结果
     */
    List<ActiveHistory>  selectActiveHistoryList(QueryFilter filter);


    /**
     * 分页查询正在执行履历
     *
     * @param filter    查询过滤条件
     * @param flowPage  分页条件
     * @return          查询结果
     */
    List<ActiveHistory>  selectActiveHistoryList(QueryFilter filter, FlowPage<ActiveHistory> flowPage);


    /**
     * 通过ID主键 查询单条正在执行履历信息
     *
     * @param id  主键
     * @return    单条信息
     */
    ActiveHistory  getActiveHistoryById(String id);


    /*--------------------正在执行履历-----------------*/



    /*--------------------执行完毕履历-----------------*/
    /**
     * 新增执行完毕履历
     *
     * @param completeHistory 执行完毕履历
     * @return   新增结果
     */
    int  insertCompleteHistory(CompleteHistory completeHistory);


    /**
     * 批量新增
     * @param activeHistories   执行履历
     * @return 新增结果
     */
    int  insertCompleteHistoryList(List<ActiveHistory> activeHistories);


    /**
     * 通过id 删除执行完毕履历
     *
     * @param id  主键ID
     * @return    删除结果
     */
    int  removeCompleteHistoryById(String id);


    /**
     * 通过id数组删除完毕履历
     *
     * @param ids   ids
     * @return      删除结果
     */
    int  removeCompleteHistoryIds(List<String> ids);


    /**
     * 更新执行完毕履历
     *
     * @param completeHistory  执行完毕履历实体信息
     *
     * @return  更新结果
     */
    int  updateCompleteHistory(CompleteHistory completeHistory);


    /**
     * 查询执行完毕履历
     *
     * @param filter   查询条件
     * @return         查询结果
     */
    List<CompleteHistory>  selectCompleteHistoryList(QueryFilter filter);


    /**
     * 分页查询执行完毕履历
     *
     * @param filter    查询过滤条件
     * @param flowPage  分页条件
     * @return          查询结果
     */
    List<CompleteHistory>  selectCompleteHistoryList(QueryFilter filter, FlowPage<CompleteHistory> flowPage);


    /**
     * 通过ID主键 查询单条执行完毕履历信息
     *
     * @param id  主键
     * @return    单条信息
     */
    CompleteHistory getCompleteHistoryById(String id);

    /*--------------------执行完毕履历-----------------*/


    /*--------------------流程执行实例-----------------*/

    /**
     * 新增流程执行实例
     *
     * @param orderInstance  流程执行实例实体
     * @return      新增结果
     */
    int insertOrderInstance(OrderInstance orderInstance);


    /**
     * 更新流程执行实例
     *
     * @param orderInstance  流程执行实例
     * @return               更新结果
     */
    int updateOrderInstance(OrderInstance orderInstance);


    /**
     * 添加流程实例对象的业务数据
     *
     * @param orderBusiness  业务数据对象
     * @return               添加结果
     */
    int insertOrderBusiness(OrderBusiness orderBusiness);


    /**
     * 批量新增业务数据
     *
     * @param businesses  业务关联流程实例集合
     * @return            添加结果
     */
    int insertOrderBusiness(List<OrderBusiness> businesses);


    /**
     *
     * @param orderId  流程实例号
     * @return         删除结果
     */
    int removeOrderBusinessByOrderId(String orderId);


    /**
     * 通过业务主键删除业务
     *
     * @param businessId  业务主键
     * @return            删除结果
     */
    int removeOrderBusinessByBusinessId(String businessId,String system);


    /**
     * 过滤查询业务
     *
     * @param filter  过滤条件
     * @return        查询结果
     */
    List<OrderBusiness>  selectOrderBusinessList(QueryFilter filter);

    /**
     * 通过主键删除流程执行实例
     *
     * @param id  主键
     * @return    删除结果
     */
    int removeOrderInstanceById(String id);


    /**
     * 通过流程实例父Id删除流程实例
     * @param parentOrderId   父ID
     * @return   删除结果
     */
    int removeOrderInstanceByParentId(String parentOrderId);


    /**
     * 通过流程实例主键查询流程实例
     *
     * @param id  主键
     * @return    单条流程执行实例
     */
    OrderInstance getOrderInstanceById(String id);



    /**
     * 通过条件查询流程执行实例
     *
     * @param filter  过滤查询条件
     * @return        流程执行实例集合
     */
    List<OrderInstance>  selectOrderInstanceList(QueryFilter filter);


    /**
     * 分页查询流程执行实例
     *
     * @param filter     过滤条件
     * @param flowPage   分页条件
     * @return           流程执行实例集合
     */
    List<OrderInstance>  selectOrderInstanceList(QueryFilter filter, FlowPage<OrderInstance> flowPage);


    /*--------------------流程执行实例-----------------*/



    /*--------------------流程历史执行实例-----------------*/

    /**
     * 新增流程执行实例
     *
     * @param orderHistoryInstance  流程历史执行实例实体
     * @return      新增结果
     */
    int insertOrderHistoryInstance(OrderHistoryInstance orderHistoryInstance);


    /**
     * 更新流程执行实例
     *
     * @param orderHistoryInstance  流程历史执行实例实体
     * @return               更新结果
     */
    int updateOrderHistoryInstance(OrderHistoryInstance orderHistoryInstance);


    /**
     * 通过主键删除流程执行实例
     *
     * @param id  主键
     * @return    删除结果
     */
    int removeOrderHistoryInstanceById(String id);


    /**
     * 通过流程实例主键查询流程实例
     *
     * @param id  主键
     * @return    单条流程执行实例
     */
    OrderHistoryInstance getOrderHistoryInstanceById(String id);


    /**
     * 通过条件查询流程执行实例
     *
     * @param filter  过滤查询条件
     * @return        流程执行实例集合
     */
    List<OrderHistoryInstance>  selectOrderHistoryInstanceList(QueryFilter filter);


    /**
     * 分页查询流程执行实例
     *
     * @param filter     过滤条件
     * @param flowPage   分页条件
     * @return           流程执行实例集合
     */
    List<OrderHistoryInstance>  selectOrderHistoryInstanceList(QueryFilter filter, FlowPage<OrderHistoryInstance> flowPage);


    /*--------------------流程历史执行实例-----------------*/








    /*--------------------流程配置-----------------*/

    /**
     * 新增流程配置
     *
     * @param processConfig  流程配置
     * @return               新增结果
     */
    int  insertProcessConfig(ProcessConfig processConfig);


    /**
     * 更新配置信息
     *
     * @param processConfig  配置实体
     * @return               更新结果
     */
    int  updateProcessConfig(ProcessConfig processConfig);


    /**
     * 通过主键删除流程配置
     *
     * @param id 主键
     * @return   删除结果
     */
    int  removeProcessConfigById(String id);


    /**
     * 通过流程id  删除所属配置数据
     *
     * @param processId  流程ID
     * @return           删除结果
     */
    int  removeProcessConfigByProcessId(String processId);


    /**
     * 通过主键查询单条流程配置信息
     *
     * @param id   主键
     * @return     单条流程配置信息
     */
    ProcessConfig getProcessConfigById(String id);


    /**
     * 更新排序规则
     * @param ids   需要更新的配置ID集合
     * @param add   在原有的基础上更新多少
     * @return      更新结果
     */
    int  updateSort(List<String> ids,int add);


    /**
     * 通过过滤条件查询流程配置
     *
     * @param filter  过滤条件
     * @return        流程配置集合
     */
    List<ProcessConfig>   selectProcessConfigList(QueryFilter filter);


    /*--------------------流程配置-----------------*/


    /*--------------------任务审批人-----------------*/


    /**
     * 新增任务审批人
     *
     * @param taskActor   任务审批人对象
     * @return            新增结果
     */
    int insertTaskActor(TaskActor taskActor);




    /**
     * 通过任务id删除 任务审批人
     *
     * @param taskId  任务ID
     * @return        删除结果
     */
    int removeTaskActorByTaskId(String taskId);


    /**
     * 通过任务id集合，删除任务审批人
     *
     * @param taskIds  任务id集合
     * @return         删除结果
     */
    int removeTaskActorByTaskIds(List<String> taskIds);


    /**
     * 通过任务ID 和审批人ID删除审批人
     * @param taskId      任务id
     * @param taskActor   审批人ID
     * @return            删除结果
     */
    int removeTaskActor(String taskId,String taskActor);


    /**
     * 通过过滤条件查询任务审批人
     *
     * @param filter  过滤条件
     * @return        任务审批人
     */
    List<TaskActor>  selectTaskActorList(QueryFilter filter);


    /*--------------------任务审批人-----------------*/



    /*--------------------任务实例-----------------*/

    /**
     * 新增任务实例
     *
     * @param taskInstance   任务实例实体
     * @return               新增结果
     */
    int  insertTaskInstance(TaskInstance taskInstance);


    /**
     * 更新任务实例实体
     *
     * @param taskInstance   任务实例实体
     * @return               更新结果
     */
    int  updateTaskInstance(TaskInstance taskInstance);


    /**
     * 通过任务ID删除任务实例
     *
     *
     * @param id   任务ID
     * @return     删除结果
     */
    int  removeTaskInstanceById(String id);


    /**
     * 通过流程实例号删除所属任务
     *
     * @param orderId  流程实例号
     * @return         删除结果
     */
    int  removeTaskByOrderId(String orderId);


    /**
     * 通过任务ID集合删除任务
     *
     * @param taskIds   任务id集合
     * @return          删除结果
     */
    int  removeTaskByTaskIds(List<String> taskIds);




    /**
     * 通过任务ID 查询单条任务实例
     *
     * @param id  任务ID
     * @return    单条任务实例
     */
    TaskInstance getTaskInstanceById(String id);


    /**
     * 通过过滤条件查询任务实例
     *
      * @param queryFilter  过滤条件
     * @return              任务实例集合
     */
    List<TaskInstance> selectTaskInstanceList(QueryFilter queryFilter);


    /**
     * 分页查询任务实例
     *
     * @param queryFilter   过滤条件
     * @param flowPage      分页条件
     * @return              任务实例集合
     */
    List<TaskInstance> selectTaskInstanceList(QueryFilter queryFilter, FlowPage<TaskInstance> flowPage);

    /*--------------------任务实例-----------------*/




    /**
     * 获取数据库连接
     *
     * @return  连接
     */
    Connection getConnection();


    /**
     * 获取引擎
     *
     * @return  工作流引擎
     */
    WorkflowEngine engine();

    /**
     * 设置引擎
     * @param engine  引擎
     */
    void engine(WorkflowEngine engine);

}
