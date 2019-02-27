package com.bcx.wind.workflow;

import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.db.paging.Paging;
import com.bcx.wind.workflow.entity.*;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * 数据库操作中间层服务
 *
 * @author zhanglei082319
 *
 * createTime: 2018-12-20
 */
public abstract class AbstractAccess implements Access {

    //流程定义
    private static final String PROCESS_DEFINITION_INSERT = " insert into wind_process_definition(id,process_name,display_name,status,version,parent_id,create_time,content,system) values(?,?,?,?,?,?,?,?,?);";

    private static final String PROCESS_DEFINITION_UPDATE = " update wind_process_definition set  process_name=? , display_name=? , status=? , version=? , parent_id=? , create_time=? , content=? , system=? where id=?";

    private static final String PROCESS_DEFINITION_DELETE = " delete from wind_process_definition where ";

    private static final String PROCESS_DEFINITION_SELECT = " select * from wind_process_definition where ";

    private static final String PROCESS_DEFINITION_COUNT = " select count(*) from wind_process_definition where";


    //正在执行履历
    private static final String ACTIVE_HISTORY_INSERT = " insert into wind_active_history(id,task_id,task_name,task_display_name,process_id,order_id,process_name,process_display_name,operate,suggest,approve_time,actor_id,actor_name,approve_id,approve_name,create_time,approve_user_variable,task_type,system,submit_user_variable) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    private static final String ACTIVE_HISTORY_UPDATE = " update wind_active_history set task_id=? , task_name=? , task_display_name=? , process_id=? , order_id=? , process_name=? , process_display_name=? , operate=? , suggest=? , approve_time=? , actor_id=? , actor_name=? ,approve_id=? , approve_name=? , create_time=? , approve_user_variable=?,task_type=? , system=? , submit_user_variable=?  where id=?";

    private static final String ACTIVE_HISTORY_DELETE = " delete from wind_active_history where ";

    private static final String ACTIVE_HISTORY_SELECT = " select * from wind_active_history where ";

    private static final String ACTIVE_HISTORY_COUNT = " select count(*) from wind_active_history where ";


    //执行完毕履历
    private static final String COMPLETE_HISTORY_INSERT = " insert into wind_complete_history(id,task_id,task_name,task_display_name,process_id,order_id,process_name,process_display_name,operate,suggest,approve_time,actor_id,actor_name,approve_id,approve_name,create_time,approve_user_variable,task_type,system,submit_user_variable) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    private static final String COMPLETE_HISTORY_INSERTS = " insert into wind_complete_history (id,task_id,task_name,task_display_name,process_id,order_id,process_name,process_display_name,operate,suggest,approve_time,actor_id,actor_name,approve_id,approve_name,create_time,approve_user_variable,task_type,system,submit_user_variable) values ";

    private static final String COMPLETE_HISTORY_UPDATE = " update wind_complete_history set task_id=? , task_name=? , task_display_name=? , process_id=? , order_id=? , process_name=? , process_display_name=? , operate=? , suggest=? , approve_time=? , actor_id=? , actor_name=? ,approve_id=? , approve_name=? ,  create_time=? , approve_user_variable=?,task_type=? , system=? , submit_user_variable=? where id=?";

    private static final String COMPLETE_HISTORY_DELETE = " delete from wind_complete_history where ";

    private static final String COMPLETE_HISTORY_SELECT = " select * from wind_complete_history where ";

    private static final String COMPLETE_HISTORY_COUNT = " select count(*) from wind_complete_history where ";


    //流程实例
    private static final String ORDER_INSTANCE_INSERT = " insert into wind_order_instance(id,process_id,status,create_user,create_time,expire_time,parent_id,version,variable,data,system) values(?,?,?,?,?,?,?,?,?,?,?); ";

    private static final String ORDER_INSTANCE_UPDATE = " update wind_order_instance set process_id=? , status=? , create_user=?, create_time=?, expire_time=?, parent_id=?, version=version+1, variable=?, data=? , system=? where id=? and version=?; ";

    private static final String ORDER_INSTANCE_DELETE = " delete from wind_order_instance where ";

    private static final String ORDER_INSTANCE_SELECT = " select * from wind_order_instance where ";

    private static final String ORDER_INSTANCE_COUNT = " select count(*) from wind_order_instance where";



    //业务数据
    private static final String ORDER_BUSINESS_INSERT = " insert into wind_business_id(order_id,business_id,system)values(?,?,?) ";

    private static final String ORDER_BUSINESS_INSERT_LIST = " insert into wind_business_id(order_id,business_id,system)values ";

    private static final String ORDER_BUSINESS_REMOVE = " delete from wind_business_id where ";

    private static final String ORDER_BUSINESS_SELECT = " select * from wind_business_id where ";


    //流程实例历史
    private static final String ORDER_HISTORY_INSERT = " insert into wind_history_order_instance(id,process_id,status,create_user,create_time,expire_time,parent_id,version,variable,data,system) values(?,?,?,?,?,?,?,?,?,?,?); ";

    private static final String ORDER_HISTORY_UPDATE = " update wind_history_order_instance set process_id=? , status=? , create_user=?, create_time=?, expire_time=?, parent_id=?, version=?, variable=?, data=? , system=? where id=? ; ";

    private static final String ORDER_HISTORY_DELETE = " delete from wind_history_order_instance where ";

    private static final String ORDER_HISTORY_SELECT = " select * from wind_history_order_instance where ";

    private static final String ORDER_HISTORY_COUNT = " select count(*) from wind_history_order_instance where";


    //流程配置
    private static final String PROCESS_CONFIG_INSERT = " insert into wind_process_config(id,process_id,config_name,process_name,node_id,condition,node_config,approve_user,sort,level,create_time,logic,business_config) values(?,?,?,?,?,?,?,?,?,?,?,?,?); ";

    private static final String PROCESS_CONFIG_UPDATE = " update wind_process_config set process_id=? , config_name=? , process_name=? , node_id=? , condition=? , node_config=? , approve_user=? , sort=? , level=?, create_time=? , logic=? , business_config=? where id=?; ";

    private static final String PROCESS_CONFIG_DELETE = " delete from wind_process_config where ";

    private static final String PROCESS_CONFIG_SELECT = " select * from wind_process_config where ";

    private static final String PROCESS_CONFIG_UPDATE_SORT = "update wind_process_config set sort=sort+? where id in ( ";


    //任务审批人
    private static final String TASK_ACTOR_INSERT = " insert into wind_task_actor(task_id,task_actor_id,actor_variable)values(?,?,?); ";

    private static final String TASK_ACTOR_DELETE = " delete from wind_task_actor where ";

    private static final String TASK_ACTOR_SELECT = " select * from wind_task_actor where ";

    private static final String TASK_ACTOR_DELETE_ACTOR = " delete from wind_task_actor where task_id = ? and task_actor_id=? ";


    //任务实例
    private static final String TASK_INSTANCE_INSERT = " insert into wind_task_instance(id,task_name,display_name,task_type,approve_user,create_time,approve_count,status,order_id,process_id,variable,parent_id,version,position)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?); ";

    private static final String TASK_INSTANCE_UPDATE = " update wind_task_instance set task_name=?, display_name=?,task_type=?,approve_user=?,create_time=?,approve_count=?,status=?,order_id=?,process_id=?,variable=?,parent_id=?,version=version+1 ,position=? where id=? and version=?; ";

    private static final String TASK_INSTANCE_DELETE = " delete from wind_task_instance where ";

    private static final String TASK_INSTANCE_SELECT = " select * from wind_task_instance ";

    private static final String TASK_INSTANCE_COUNT = " select count(*) from wind_task_instance ";

    //分页语句增强器
    protected Paging paging;

    //引擎
    protected WorkflowEngine engine;

    @Override
    public void engine(WorkflowEngine engine){
        this.engine = engine;
    }


    @Override
    public WorkflowEngine engine(){
        return this.engine;
    }


    /**
     * 调用最终持久层更新方法
     */
    public abstract int  saveOrUpdate(String sql,Object[] args);


    /**
     * 调用最终持久层查询方法
     * @param sql    sql查询语句
     * @param args   查询参数
     * @param <T>    返回类型
     * @return       list
     */
    public abstract <T>List<T> query(String sql,Object[] args,Class<T> clazz);


    /**
     * 查询数量
     *
     * @param sql  sql语句
     * @param args 参数
     * @return     数量
     */
    public abstract long queryCount(String sql,Object[] args);



    /**
     * 查询单个数据
     *
     * @param sql     sql查询语句
     * @param args    参数
     * @param clazz   返回值类型
     * @param <T>     类型
     * @return        T
     */
    public abstract <T>T  getSingleton(String sql,Object[] args,Class<T> clazz);


    @Override
    public int queryMaxVersionProcess(String processName) {
        String sql = PROCESS_DEFINITION_SELECT+" process_name = ? ";
        Object[] args = new Object[]{processName};
        List<ProcessDefinition> result =  query(sql,args,ProcessDefinition.class);
        if(!ObjectHelper.isEmpty(result)){
            result.sort(Comparator.comparing(ProcessDefinition::getVersion));
            return result.get(0).getVersion();
        }
        throw new WorkflowException("search processDefinition by processName result is null !");
    }

    @Override
    public List<ProcessDefinition> selectProcessList(QueryFilter filter) {
         return selectProcessList(filter,null);
    }

    @Override
    public ProcessDefinition getProcessDefinitionById(String processId) {
        String sql = PROCESS_DEFINITION_SELECT + " id = ? ";
        Object[] args = new Object[]{processId};
        List<ProcessDefinition> result =  query(sql,args,ProcessDefinition.class);
        if(!ObjectHelper.isEmpty(result)){
            return result.get(0);
        }
        throw new WorkflowException("search processDefinition by processId result is null !");
    }


    @Override
    public int addProcess(ProcessDefinition processDefinition) {
        Object[] args = new Object[]{processDefinition.getId(),processDefinition.getProcessName(),processDefinition.getDisplayName(),processDefinition.getStatus(),
                processDefinition.getVersion(),processDefinition.getParentId(),processDefinition.getCreateTime(),processDefinition.getContent(),processDefinition.getSystem()};
        return saveOrUpdate(PROCESS_DEFINITION_INSERT,args);
    }


    @Override
    public int updateProcess(ProcessDefinition processDefinition) {
        Object[] args = new Object[]{processDefinition.getProcessName(),processDefinition.getDisplayName(),processDefinition.getStatus(),
                                  processDefinition.getVersion(),processDefinition.getParentId(),processDefinition.getCreateTime(),processDefinition.getContent(),processDefinition.getSystem()
                                        ,processDefinition.getId()};
        return saveOrUpdate(PROCESS_DEFINITION_UPDATE,args);
    }



    /**
     * 通过模型ID删除模型
     *
     * @param processId 模型ID
     */
    public int removeProcessById(String processId){
        Assert.notEmpty("remove process operate must be args processId !",processId);
        String sql = PROCESS_DEFINITION_DELETE+" id = ? ";
        return saveOrUpdate(sql,new Object[]{processId});
    }


    @Override
    public List<ProcessDefinition> selectProcessList(QueryFilter filter, FlowPage<ProcessDefinition> page) {
        StringBuilder sql = new StringBuilder(PROCESS_DEFINITION_SELECT);
        Object[] args = createProcessDefinitionSql(filter,page,sql);

        List<ProcessDefinition> result =  query(sql.toString(),args,ProcessDefinition.class);
        //构建分页数据信息
        if(!ObjectHelper.isEmpty(page)) {
            buildFlowPage(result, filter, page,PROCESS_DEFINITION_COUNT);
        }
        return result;
    }

    /**
     * 查询数据数量
     *
     * @param list     查询的数据集合
     * @param filter   过滤条件
     * @param flowPage 分页实体
     * @param <T>      数据类型
     */
    private <T>void buildFlowPage(List<T> list,QueryFilter filter,FlowPage<T> flowPage,String countSql){
        StringBuilder sql = new StringBuilder(countSql);
        Object[] args = createProcessDefinitionSql(filter,null,sql);
        long count =  queryCount(sql.toString(),args);
        flowPage.result(list);
        flowPage.setTotal(count);
    }



    @Override
    public int insertActiveHistory(ActiveHistory activeHistory) {
        Object[] args = new Object[]{activeHistory.getId(),activeHistory.getTaskId(),activeHistory.getTaskName(),activeHistory.getTaskDisplayName(),
                activeHistory.getProcessId(),activeHistory.getOrderId(),activeHistory.getProcessName(),activeHistory.getProcessDisplayName(),
                activeHistory.getOperate(),activeHistory.getSuggest(),activeHistory.getApproveTime(),activeHistory.getActorId(),activeHistory.getActorName(),
                activeHistory.getApproveId(),activeHistory.getApproveName(),
                activeHistory.getCreateTime(),activeHistory.getApproveUserVariable(),activeHistory.getTaskType(),activeHistory.getSystem(),activeHistory.getSubmitUserVariable()};

        return saveOrUpdate(ACTIVE_HISTORY_INSERT, args);
    }

    @Override
    public int removeActiveHistoryById(String id) {
        Object[] args = new Object[]{id};
        String sql = ACTIVE_HISTORY_DELETE+" id = ? ";
        return saveOrUpdate(sql,args);
    }

    @Override
    public int removeActiveHistoryByIds(List<String> ids) {
        Object[] args = ids.toArray();
        StringBuilder sql = new StringBuilder(ACTIVE_HISTORY_DELETE+" id in ( ");
        return saveOrUpdate(buildInSql(ids,sql),args);
    }


    @Override
    public int updateActiveHistory(ActiveHistory activeHistory) {
        Object[] args = new Object[]{activeHistory.getTaskId(),activeHistory.getTaskName(),activeHistory.getTaskDisplayName(),
                activeHistory.getProcessId(),activeHistory.getOrderId(),activeHistory.getProcessName(),activeHistory.getProcessDisplayName(),
                activeHistory.getOperate(),activeHistory.getSuggest(),activeHistory.getApproveTime(),activeHistory.getActorId(),activeHistory.getActorName(),
                activeHistory.getApproveId(),activeHistory.getApproveName(),
                activeHistory.getCreateTime(),activeHistory.getApproveUserVariable(),activeHistory.getTaskType(),activeHistory.getSystem(),activeHistory.getSubmitUserVariable(),activeHistory.getId()};
        return saveOrUpdate(ACTIVE_HISTORY_UPDATE,args);
    }


    @Override
    public List<ActiveHistory> selectActiveHistoryList(QueryFilter filter) {
        StringBuilder sql = new StringBuilder(ACTIVE_HISTORY_SELECT);
        Object[] args = createActiveHistorySql(filter,null,sql);
        return query(sql.toString(),args,ActiveHistory.class);
    }

    @Override
    public List<ActiveHistory> selectActiveHistoryList(QueryFilter filter, FlowPage<ActiveHistory> flowPage) {
        StringBuilder sql = new StringBuilder(ACTIVE_HISTORY_SELECT);
        Object[] args = createActiveHistorySql(filter,flowPage,sql);
        List<ActiveHistory> result =  query(sql.toString(),args,ActiveHistory.class);
        if(!ObjectHelper.isEmpty(flowPage)) {
            buildFlowPage(result, filter, flowPage,ACTIVE_HISTORY_COUNT);
        }
        return result;
    }


    @Override
    public ActiveHistory getActiveHistoryById(String id) {
        String sql = ACTIVE_HISTORY_SELECT+" id = ? ";
        Object[] args = new Object[]{id};
        return getSingleton(sql,args,ActiveHistory.class);
    }


    @Override
    public int insertCompleteHistory(CompleteHistory completeHistory) {
        Object[] args = new Object[]{completeHistory.getId(),completeHistory.getTaskId(),completeHistory.getTaskName(),completeHistory.getTaskDisplayName(),
                completeHistory.getProcessId(),completeHistory.getOrderId(),completeHistory.getProcessName(),completeHistory.getProcessDisplayName(),
                completeHistory.getOperate(),completeHistory.getSuggest(),completeHistory.getApproveTime(),completeHistory.getActorId(),completeHistory.getActorName(),
                completeHistory.getApproveId(),completeHistory.getApproveName(),
                completeHistory.getCreateTime(),completeHistory.getApproveUserVariable(),completeHistory.getTaskType(),completeHistory.getSystem(),completeHistory.getSubmitUserVariable()};

        return saveOrUpdate(COMPLETE_HISTORY_INSERT, args);
    }

    @Override
    public int insertCompleteHistoryList(List<ActiveHistory> activeHistories) {
        StringBuilder builder = new StringBuilder(COMPLETE_HISTORY_INSERTS);
        LinkedList<Object> list = new LinkedList<>();
        if(!ObjectHelper.isEmpty(activeHistories)){
            for(int i=0 ; i<activeHistories.size() ; i++){
                ActiveHistory activeHistory = activeHistories.get(i);
                if(i<activeHistories.size()-1) {
                    builder.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?), ");
                }else{
                    builder.append(" (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); ");
                }
                Object[] args = new Object[]{activeHistory.getId(),activeHistory.getTaskId(),activeHistory.getTaskName(),activeHistory.getTaskDisplayName(),
                        activeHistory.getProcessId(),activeHistory.getOrderId(),activeHistory.getProcessName(),activeHistory.getProcessDisplayName(),
                        activeHistory.getOperate(),activeHistory.getSuggest(),activeHistory.getApproveTime(),activeHistory.getActorId(),activeHistory.getActorName(),
                        activeHistory.getApproveId(),activeHistory.getApproveName(),
                        activeHistory.getCreateTime(),activeHistory.getApproveUserVariable(),activeHistory.getTaskType(),activeHistory.getSystem(),activeHistory.getSubmitUserVariable()};

                list.addAll(Arrays.asList(args));
            }
            return saveOrUpdate(builder.toString(),list.toArray());
        }
        return 0;
    }


    @Override
    public int removeCompleteHistoryById(String id) {
        Object[] args = new Object[]{id};
        String sql = COMPLETE_HISTORY_DELETE+" id=? ";
        return saveOrUpdate(sql,args);
    }

    @Override
    public int removeCompleteHistoryIds(List<String> ids) {
        Object[] args = ids.toArray();
        StringBuilder sql = new StringBuilder(COMPLETE_HISTORY_DELETE+" id in (");
        return saveOrUpdate(buildInSql(ids,sql),args);
    }


    @Override
    public int updateCompleteHistory(CompleteHistory completeHistory) {
        Object[] args = new Object[]{completeHistory.getTaskId(),completeHistory.getTaskName(),completeHistory.getTaskDisplayName(),
                completeHistory.getProcessId(),completeHistory.getOrderId(),completeHistory.getProcessName(),completeHistory.getProcessDisplayName(),
                completeHistory.getOperate(),completeHistory.getSuggest(),completeHistory.getApproveTime(),completeHistory.getActorId(),completeHistory.getActorName(),
                completeHistory.getApproveId(),completeHistory.getApproveName(),
                completeHistory.getCreateTime(),completeHistory.getApproveUserVariable(),completeHistory.getTaskType(),completeHistory.getSystem(),completeHistory.getSubmitUserVariable(),completeHistory.getId()};
        return saveOrUpdate(COMPLETE_HISTORY_UPDATE,args);
    }

    @Override
    public List<CompleteHistory> selectCompleteHistoryList(QueryFilter filter) {
        StringBuilder sql = new StringBuilder(COMPLETE_HISTORY_SELECT);
        Object[] args = createActiveHistorySql(filter,null,sql);
        return query(sql.toString(),args,CompleteHistory.class);
    }

    @Override
    public List<CompleteHistory> selectCompleteHistoryList(QueryFilter filter, FlowPage<CompleteHistory> flowPage) {
        StringBuilder sql = new StringBuilder(COMPLETE_HISTORY_SELECT);
        Object[] args = createActiveHistorySql(filter,flowPage,sql);
        List<CompleteHistory> result =  query(sql.toString(),args,CompleteHistory.class);
        if(!ObjectHelper.isEmpty(flowPage)) {
            buildFlowPage(result, filter, flowPage, COMPLETE_HISTORY_COUNT);
        }
        return result;
    }

    @Override
    public CompleteHistory getCompleteHistoryById(String id) {
        String sql = COMPLETE_HISTORY_SELECT+" id=? ";
        Object[] args = new Object[]{id};
        return getSingleton(sql,args,CompleteHistory.class);
    }


    @Override
    public int insertOrderInstance(OrderInstance orderInstance) {
        Object[] args = new Object[]{orderInstance.getId(),orderInstance.getProcessId(),orderInstance.getStatus(),
                orderInstance.getCreateUser(),orderInstance.getCreateTime(),orderInstance.getExpireTime(),orderInstance.getParentId(),orderInstance.getVersion(),
                orderInstance.getVariable(),orderInstance.getData(),orderInstance.getSystem()};
        return saveOrUpdate(ORDER_INSTANCE_INSERT,args);
    }

    @Override
    public int updateOrderInstance(OrderInstance orderInstance) {
        Object[] args = new Object[]{orderInstance.getProcessId(),orderInstance.getStatus(),
                orderInstance.getCreateUser(),orderInstance.getCreateTime(),orderInstance.getExpireTime(),orderInstance.getParentId(),
                orderInstance.getVariable(),orderInstance.getData(),orderInstance.getSystem(),orderInstance.getId(),orderInstance.getVersion()};
        return saveOrUpdate(ORDER_INSTANCE_UPDATE,args);
    }

    @Override
    public int removeOrderInstanceById(String id) {
        String sql = ORDER_INSTANCE_DELETE+ " id=? ";
        Object[] args = new Object[]{id};
        return saveOrUpdate(sql,args);
    }

    @Override
    public int removeOrderInstanceByParentId(String parentOrderId) {
        String sql = ORDER_INSTANCE_DELETE+ " parent_id=? ";
        Object[] args = new Object[]{parentOrderId};
        return saveOrUpdate(sql,args);
    }

    @Override
    public OrderInstance getOrderInstanceById(String id) {
        String sql = ORDER_INSTANCE_SELECT+ " id=? ";
        Object[] args = new Object[]{id};
        return getSingleton(sql,args,OrderInstance.class);
    }

    @Override
    public List<OrderInstance> selectOrderInstanceList(QueryFilter filter) {
        StringBuilder sql = new StringBuilder(ORDER_INSTANCE_SELECT);
        Object[] args = createOrderInstanceSql(filter,null,sql);
        return query(sql.toString(),args,OrderInstance.class);
    }

    @Override
    public int insertOrderBusiness(OrderBusiness orderBusiness) {
        Object[] args = new Object[]{orderBusiness.getOrderId(),orderBusiness.getBusinessId(),orderBusiness.getSystem()};

        return saveOrUpdate(ORDER_BUSINESS_INSERT,args);
    }


    @Override
    public int insertOrderBusiness(List<OrderBusiness> businesses) {
        StringBuilder sql = new StringBuilder(ORDER_BUSINESS_INSERT_LIST);

        if(ObjectHelper.isEmpty(businesses)){
            return 0;
        }

        LinkedList<Object> args = new LinkedList<>();
        for(int i=0 ; i<businesses.size() ; i++){
            OrderBusiness business = businesses.get(i);
            if(i<businesses.size()-1) {
                sql.append(" (?,?,?), ");
                args.addLast(business.getOrderId());
                args.addLast(business.getBusinessId());
                args.addLast(business.getSystem());
                continue;
            }
            sql.append(" (?,?,?); ");
            args.addLast(business.getOrderId());
            args.addLast(business.getBusinessId());
            args.addLast(business.getSystem());
        }

        return saveOrUpdate(sql.toString(),args.toArray());
    }

    @Override
    public int removeOrderBusinessByOrderId(String orderId) {
        Object[] args = new Object[]{orderId};

        String sql = ORDER_BUSINESS_REMOVE+" order_id=? ";
        return saveOrUpdate(sql,args);
    }

    @Override
    public int removeOrderBusinessByBusinessId(String businessId,String system) {
        LinkedList<Object> variable = new LinkedList<>();
        variable.addLast(businessId);
        StringBuilder sql = new StringBuilder(ORDER_BUSINESS_REMOVE+" business_id=? ");

        if(!ObjectHelper.isEmpty(system)){
            variable.addLast(system);
            sql.append(" and system=? ");
        }

        return saveOrUpdate(sql.toString(),variable.toArray());
    }

    @Override
    public List<OrderBusiness> selectOrderBusinessList(QueryFilter filter) {
        StringBuilder sql = new StringBuilder(ORDER_BUSINESS_SELECT);

        Object[] args = createOrderBusinessSql(filter,sql);
        return query(sql.toString(),args,OrderBusiness.class);
    }

    @Override
    public List<OrderInstance> selectOrderInstanceList(QueryFilter filter, FlowPage<OrderInstance> flowPage) {
        StringBuilder sql = new StringBuilder(ORDER_INSTANCE_SELECT);
        Object[] args = createOrderInstanceSql(filter,flowPage,sql);
        List<OrderInstance> result = query(sql.toString(),args,OrderInstance.class);
        if(!ObjectHelper.isEmpty(flowPage)){
            buildFlowPage(result,filter,flowPage,ORDER_INSTANCE_COUNT);
        }
        return result;
    }

    @Override
    public int insertOrderHistoryInstance(OrderHistoryInstance orderHistoryInstance) {
        Object[] args = new Object[]{orderHistoryInstance.getId(),orderHistoryInstance.getProcessId(),orderHistoryInstance.getStatus(),
                orderHistoryInstance.getCreateUser(),orderHistoryInstance.getCreateTime(),orderHistoryInstance.getExpireTime(),orderHistoryInstance.getParentId(),orderHistoryInstance.getVersion(),
                orderHistoryInstance.getVariable(),orderHistoryInstance.getData(),orderHistoryInstance.getSystem()};
        return saveOrUpdate(ORDER_HISTORY_INSERT,args);
    }

    @Override
    public int updateOrderHistoryInstance(OrderHistoryInstance orderHistoryInstance) {
        Object[] args = new Object[]{orderHistoryInstance.getProcessId(),orderHistoryInstance.getStatus(),
                orderHistoryInstance.getCreateUser(),orderHistoryInstance.getCreateTime(),orderHistoryInstance.getExpireTime(),orderHistoryInstance.getParentId(),orderHistoryInstance.getVersion(),
                orderHistoryInstance.getVariable(),orderHistoryInstance.getData(),orderHistoryInstance.getSystem(),orderHistoryInstance.getId()};
        return saveOrUpdate(ORDER_HISTORY_UPDATE,args);
    }

    @Override
    public int removeOrderHistoryInstanceById(String id) {
        String sql = ORDER_HISTORY_DELETE+ " id=? ";
        Object[] args = new Object[]{id};
        return saveOrUpdate(sql,args);
    }

    @Override
    public OrderHistoryInstance getOrderHistoryInstanceById(String id) {
        String sql = ORDER_HISTORY_SELECT+ " id=? ";
        Object[] args = new Object[]{id};
        return getSingleton(sql,args,OrderHistoryInstance.class);
    }

    @Override
    public List<OrderHistoryInstance> selectOrderHistoryInstanceList(QueryFilter filter) {
        StringBuilder sql = new StringBuilder(ORDER_HISTORY_SELECT);
        Object[] args = createOrderInstanceSql(filter,null,sql);
        return query(sql.toString(),args,OrderHistoryInstance.class);
    }


    @Override
    public List<OrderHistoryInstance> selectOrderHistoryInstanceList(QueryFilter filter, FlowPage<OrderHistoryInstance> flowPage) {
        StringBuilder sql = new StringBuilder(ORDER_HISTORY_SELECT);
        Object[] args = createOrderInstanceSql(filter,flowPage,sql);
        List<OrderHistoryInstance> result = query(sql.toString(),args,OrderHistoryInstance.class);
        if(!ObjectHelper.isEmpty(flowPage)){
            buildFlowPage(result,filter,flowPage,ORDER_HISTORY_COUNT);
        }
        return result;
    }



    @Override
    public int insertProcessConfig(ProcessConfig processConfig) {
        Object[] args = new Object[]{processConfig.getId(),processConfig.getProcessId(),processConfig.getConfigName(),processConfig.getProcessName(),
                  processConfig.getNodeId(),processConfig.getCondition(),processConfig.getNodeConfig(),processConfig.getApproveUser(),processConfig.getSort(),
                  processConfig.getLevel(),processConfig.getCreateTime(),processConfig.getLogic(),processConfig.getBusinessConfig()};
        return saveOrUpdate(PROCESS_CONFIG_INSERT,args);
    }

    @Override
    public int updateProcessConfig(ProcessConfig processConfig) {
        Object[] args = new Object[]{processConfig.getProcessId(),processConfig.getConfigName(),processConfig.getProcessName(),
                processConfig.getNodeId(),processConfig.getCondition(),processConfig.getNodeConfig(),processConfig.getApproveUser(),processConfig.getSort(),
                processConfig.getLevel(),processConfig.getCreateTime(),processConfig.getLogic(),processConfig.getBusinessConfig(),processConfig.getId()};
        return saveOrUpdate(PROCESS_CONFIG_UPDATE,args);
    }

    @Override
    public int removeProcessConfigById(String id) {
        String sql = PROCESS_CONFIG_DELETE + " id=? ";
        Object[] args = new Object[]{id};
        return saveOrUpdate(sql,args);
    }

    @Override
    public int removeProcessConfigByProcessId(String processId){
        String sql = PROCESS_CONFIG_DELETE + " process_id=? ";
        Object[] args = new Object[]{processId};
        return saveOrUpdate(sql,args);
    }

    @Override
    public ProcessConfig getProcessConfigById(String id) {
        String sql = PROCESS_CONFIG_SELECT + " id=? ";
        Object[] args = new Object[]{id};
        return getSingleton(sql,args,ProcessConfig.class);
    }

    @Override
    public int  updateSort(List<String> ids,int add){
        StringBuilder sql = new StringBuilder(PROCESS_CONFIG_UPDATE_SORT);

        LinkedList<Object> args = new LinkedList<>();
        args.addFirst(add);
        args.addAll(ids);
        return saveOrUpdate(buildInSql(ids,sql),args.toArray());
    }

    @Override
    public List<ProcessConfig> selectProcessConfigList(QueryFilter filter) {
        StringBuilder sql = new StringBuilder(PROCESS_CONFIG_SELECT);
        Object[] args = createProcessConfigSql(filter,null,sql);
        return query(sql.toString(),args,ProcessConfig.class);
    }

    @Override
    public int insertTaskActor(TaskActor taskActor) {
        Object[] args = new Object[]{taskActor.getTaskId(),taskActor.getTaskActorId(),taskActor.getActorVariable()};
        return saveOrUpdate(TASK_ACTOR_INSERT,args);
    }

    @Override
    public int removeTaskActorByTaskId(String taskId) {
        String sql = TASK_ACTOR_DELETE+" task_id=? ";
        Object[] args = new Object[]{taskId};
        return saveOrUpdate(sql,args);
    }

    @Override
    public int removeTaskActorByTaskIds(List<String> taskIds) {
        StringBuilder sql = new StringBuilder(TASK_ACTOR_DELETE+" task_id in ( ");

        return saveOrUpdate(buildInSql(taskIds,sql),taskIds.toArray());
    }

    @Override
    public List<TaskActor> selectTaskActorList(QueryFilter filter) {
        StringBuilder sql = new StringBuilder(TASK_ACTOR_SELECT);
        Object[] args = createTaskActorSql(filter,sql);
        return query(sql.toString(),args,TaskActor.class);
    }

    @Override
    public int insertTaskInstance(TaskInstance taskInstance) {
        Object[] args = new Object[]{taskInstance.getId(),taskInstance.getTaskName(),taskInstance.getDisplayName(),taskInstance.getTaskType(),taskInstance.getApproveUser(),
                  taskInstance.getCreateTime(),taskInstance.getApproveCount(),taskInstance.getStatus(),taskInstance.getOrderId(),taskInstance.getProcessId(),
                  taskInstance.getVariable(),taskInstance.getParentId(),taskInstance.getVersion(), taskInstance.getPosition()};
        return saveOrUpdate(TASK_INSTANCE_INSERT,args);
    }

    @Override
    public int removeTaskActor(String taskId, String taskActor) {
        StringBuilder sql = new StringBuilder(TASK_ACTOR_DELETE_ACTOR);
        Object[] args = new Object[]{taskId,taskActor};
        return saveOrUpdate(sql.toString(),args);
    }

    @Override
    public int removeTaskByOrderId(String orderId) {
        StringBuilder sql = new StringBuilder(TASK_INSTANCE_DELETE);
        sql.append(" order_id=? ");
        Object[] args = new Object[]{orderId};
        return saveOrUpdate(sql.toString(),args);
    }

    @Override
    public int removeTaskByTaskIds(List<String> taskIds) {
        StringBuilder sql = new StringBuilder(TASK_INSTANCE_DELETE);
        sql.append(" task_id in ( ");
        return saveOrUpdate(buildInSql(taskIds,sql),taskIds.toArray());
    }

    @Override
    public int updateTaskInstance(TaskInstance taskInstance) {
        Object[] args = new Object[]{taskInstance.getTaskName(),taskInstance.getDisplayName(),taskInstance.getTaskType(),taskInstance.getApproveUser(),
                taskInstance.getCreateTime(),taskInstance.getApproveCount(),taskInstance.getStatus(),taskInstance.getOrderId(),taskInstance.getProcessId(),
                taskInstance.getVariable(),taskInstance.getParentId(),taskInstance.getPosition(),taskInstance.getId(),taskInstance.getVersion()};
        return saveOrUpdate(TASK_INSTANCE_UPDATE,args);
    }

    @Override
    public int removeTaskInstanceById(String id) {
        String sql = TASK_INSTANCE_DELETE+" id=? ";
        Object[] args = new Object[]{id};
        return saveOrUpdate(sql,args);
    }

    @Override
    public TaskInstance getTaskInstanceById(String id) {
        String sql = TASK_INSTANCE_SELECT+" where id=? ";
        Object[] args = new Object[]{id};
        return getSingleton(sql,args,TaskInstance.class);
    }

    @Override
    public List<TaskInstance> selectTaskInstanceList(QueryFilter queryFilter) {
        StringBuilder sql = new StringBuilder(TASK_INSTANCE_SELECT);
        Object[] args = createTaskInstanceSql(queryFilter,null,sql);
        return query(sql.toString(),args,TaskInstance.class);
    }

    @Override
    public List<TaskInstance> selectTaskInstanceList(QueryFilter queryFilter, FlowPage<TaskInstance> flowPage) {
        StringBuilder sql = new StringBuilder(TASK_INSTANCE_SELECT);
        Object[] args = createTaskInstanceSql(queryFilter,flowPage,sql);
        List<TaskInstance> result =  query(sql.toString(),args,TaskInstance.class);
        if(!ObjectHelper.isEmpty(flowPage)){
            buildFlowPage(result,queryFilter,flowPage,TASK_INSTANCE_COUNT);
        }
        return result;
    }


    /**
     * 创建增强履历相关查询sql语句
     *
     * @param filter   过滤条件
     * @param flowPage 分页条件
     * @param sql      原始sql语句
     * @return         增强后的sql语句
     */
    private Object[]  createActiveHistorySql(QueryFilter filter,FlowPage flowPage,StringBuilder sql){
        LinkedList<Object> args = new LinkedList<>();
        sql.append(" 1=1 ");

        String taskId = filter.getTaskId();
        if(!ObjectHelper.isEmpty(taskId)){
            sql.append(" and task_id=? ");
            args.addLast(taskId);
        }

        String taskName = filter.getTaskName();
        if(!ObjectHelper.isEmpty(taskName)){
            sql.append(" and task_name=? ");
            args.addLast(taskName);
        }

        String processId = filter.getProcessId();
        if(!ObjectHelper.isEmpty(processId)){
            sql.append(" and process_id=? ");
            args.addLast(processId);
        }

        String orderId = filter.getOrderId();
        if(!ObjectHelper.isEmpty(orderId)){
            sql.append(" and order_id=? ");
            args.addLast(orderId);
        }

        String processName = filter.getProcessName();
        if (!ObjectHelper.isEmpty(processName)) {
            sql.append(" and process_name=? ");
            args.addLast(processName);
        }

        String system = filter.getSystem();
        if(!ObjectHelper.isEmpty(system)){
            sql.append(" and system=? ");
            args.addLast(system);
        }

        String[] actorId = filter.getTaskActorId();
        if(!ObjectHelper.isEmpty(actorId)){
            sql.append(" and (");
            for(String id : actorId){
                sql.append(" actor_id=? or ");
                args.addLast(id);
            }
            sql.append(" 1!=1) ");
        }

        String[] approveId = filter.getTaskApproveId();
        if(!ObjectHelper.isEmpty(approveId)){
            sql.append(" and (");
            for(String id : approveId){
                sql.append(" approve_id=? or ");
                args.addLast(id);
            }
            sql.append(" 1!=1) ");
        }

        String[] taskIds = filter.getTaskIds();
        if(!ObjectHelper.isEmpty(taskIds)){
            sql.append(" and (");
            for(String id : taskIds){
                sql.append(" task_id=? or ");
                args.addLast(id);
            }
            sql.append(" 1!=1) ");
        }

        String[] orderIds = filter.getOrderIds();
        if(!ObjectHelper.isEmpty(orderIds)){
            sql.append(" and (");
            for(String id : orderIds){
                sql.append(" order_id=? or ");
                args.addLast(id);
            }
            sql.append(" 1!=1) ");
        }

        String createTimeStart = filter.getCreateTimeStart();
        String createTimeEnd = filter.getCreateTimeEnd();
        if(!ObjectHelper.hasEmpty(createTimeEnd,createTimeStart)){
            sql.append(" and create_time between ? and ? ");
            args.addLast(createTimeStart);
            args.addLast(createTimeEnd);
        }

        if(!ObjectHelper.isEmpty(flowPage)){
            paging.buildPageSql(sql,flowPage);
        }

        return args.toArray();
    }


    /**
     * 创建增强流程实例查询sql语句
     *
     * @param filter   过滤条件
     * @param flowPage 分页条件
     * @param sql      原始sql
     * @return         增强后的sql语句
     */
    private Object[] createOrderInstanceSql(QueryFilter filter,FlowPage flowPage, StringBuilder sql){
        LinkedList<Object> args = new LinkedList<>();

        sql.append(" 1=1 ");

        String orderId = filter.getOrderId();
        if(!ObjectHelper.isEmpty(orderId)){
            sql.append(" and id=? ");
            args.addLast(orderId);
        }

        String[] orderIds = filter.getOrderIds();
        if(!ObjectHelper.isEmpty(orderIds)){
            sql.append(" and (");
            for(String id : orderIds){
                sql.append(" order_id=? or ");
                args.addLast(id);
            }
            sql.append(" 1!=1) ");
        }

        String processId = filter.getProcessId();
        if(!ObjectHelper.isEmpty(processId)){
            sql.append(" and process_id=? ");
            args.addLast(processId);
        }

        String status = filter.getStatus();
        if(!ObjectHelper.isEmpty(status)){
            sql.append(" and status = ? ");
            args.addLast(status);
        }
        String createTimeStart = filter.getCreateTimeStart();
        String createTimeEnd = filter.getCreateTimeEnd();
        if(!ObjectHelper.hasEmpty(createTimeEnd,createTimeStart)){
            sql.append(" and create_time between ? and ? ");
            args.addLast(createTimeStart);
            args.addLast(createTimeEnd);
        }

        String system = filter.getSystem();
        if(!ObjectHelper.isEmpty(system)){
            sql.append(" and system=? ");
            args.addLast(system);
        }


        String variable = filter.getVariable();
        if(!ObjectHelper.isEmpty(variable)){
            sql.append(" and variable like '%?%'");
            args.addLast(variable);
        }

        if(!ObjectHelper.isEmpty(flowPage)){
            paging.buildPageSql(sql,flowPage);
        }

        return args.toArray();
    }



    /**
     * 创建增强流程配置查询sql
     *
     * @param queryFilter     过滤条件
     * @param flowPage        分页条件
     * @param sql             原始sql
     * @return                增强后的sql
     */
    private Object[]  createProcessConfigSql(QueryFilter queryFilter,FlowPage flowPage,StringBuilder sql){
        LinkedList<Object> args = new LinkedList<>();

        sql.append(" 1=1 ");
        String processId = queryFilter.getProcessId();
        if(!ObjectHelper.isEmpty(processId)){
            sql.append(" and process_id = ? ");
            args.addLast(processId);
        }

        String processName = queryFilter.getProcessName();
        if(!ObjectHelper.isEmpty(processName)){
            sql.append(" and process_name = ? ");
            args.addLast(processName);
        }

        String nodeId = queryFilter.getNodeId();
        if(!ObjectHelper.isEmpty(nodeId)){
            sql.append(" and node_id = ? ");
            args.addLast(nodeId);
        }

        String createTimeStart = queryFilter.getCreateTimeStart();
        String createTimeEnd = queryFilter.getCreateTimeEnd();
        if(!ObjectHelper.hasEmpty(createTimeEnd,createTimeStart)){
            sql.append(" and create_time between ? and ? ");
            args.addLast(createTimeStart);
            args.addLast(createTimeEnd);
        }
        if(!ObjectHelper.isEmpty(flowPage)){
            paging.buildPageSql(sql,flowPage);
        }

        return args.toArray();
    }


    private Object[] createTaskActorSql(QueryFilter queryFilter,StringBuilder sql){
            LinkedList<Object> args = new LinkedList<>();

        sql.append(" 1=1 ");

        String taskId = queryFilter.getTaskId();
        if(!ObjectHelper.isEmpty(taskId)){
            sql.append(" and task_id=? ");
            args.addLast(taskId);
        }

        String[] taskIds = queryFilter.getTaskIds();
        if(!ObjectHelper.isEmpty(taskIds)){
            sql.append(" and (");
            for(String id : taskIds){
                sql.append(" task_id=? or ");
                args.addLast(id);
            }
            sql.append(" 1!=1) ");
        }

        String[] approveUsers = queryFilter.getTaskActorId();
        if(!ObjectHelper.isEmpty(approveUsers)){
            sql.append(" and( ");
            for(String  user :approveUsers){
                sql.append(" task_actor_id=? or ");
                args.addLast(user);
            }
            sql.append(" 1!=1) ");
        }

        return args.toArray();
    }

    private Object[] createOrderBusinessSql(QueryFilter filter,StringBuilder sql){
        LinkedList<Object> args = new LinkedList<>();

        sql.append(" 1=1 ");

        String orderId = filter.getOrderId();
        if(!ObjectHelper.isEmpty(orderId)){
            sql.append(" and order_id=? ");
            args.addLast(orderId);
        }

        List<String> businessIds = filter.getBusinessId();
        if(!ObjectHelper.isEmpty(businessIds)){
            sql.append(" and business_id in  ( ");
            buildInSql(businessIds,sql);
            for(String businessId : businessIds){
                args.addLast(businessId);
            }
        }

        String system = filter.getSystem();
        if(!ObjectHelper.isEmpty(system)){
            sql.append(" and system=? ");
            args.addLast(system);
        }

        return args.toArray();
    }


    private Object[] createTaskInstanceSql(QueryFilter queryFilter,FlowPage page,StringBuilder sql){
        LinkedList<Object> args = new LinkedList<>();

        String[] approveUsers = queryFilter.getTaskActorId();
        if(!ObjectHelper.isEmpty(approveUsers)){
           sql.append("  task , wind_task_actor actor where task.id=actor.task_id and actor.task_actor_id in ( ");
            for(int i=0 ; i<approveUsers.length ; i++){
                if(i < approveUsers.length-1) {
                    sql.append(" ?, ");
                    args.addLast(approveUsers[i]);
                }else if(i == approveUsers.length-1){
                    sql.append(" ? ");
                    args.addLast(approveUsers[i]);
                }
            }
            sql.append(" ) and ");
        }else{
            sql.append(" where ");
        }

        sql.append(" 1=1 ");

        String taskName = queryFilter.getTaskName();
        if(!ObjectHelper.isEmpty(taskName)){
            sql.append(" and task_name=? ");
            args.addLast(taskName);
        }


        String status = queryFilter.getStatus();
        if(!ObjectHelper.isEmpty(status)){
            sql.append(" and status = ? ");
            args.addLast(status);
        }

        String position = queryFilter.getPosition();
        if (!ObjectHelper.isEmpty(position)) {
            sql.append(" and local = ? ");
            args.addLast(position);
        }


        String createTimeStart = queryFilter.getCreateTimeStart();
        String createTimeEnd = queryFilter.getCreateTimeEnd();
        if(!ObjectHelper.hasEmpty(createTimeEnd,createTimeStart)){
            sql.append(" and create_time between ? and ? ");
            args.addLast(createTimeStart);
            args.addLast(createTimeEnd);
        }

        String orderId = queryFilter.getOrderId();
        if(!ObjectHelper.isEmpty(orderId)){
            sql.append(" and order_id=? ");
            args.addLast(orderId);
        }

        String[] orderIds = queryFilter.getOrderIds();
        if(!ObjectHelper.isEmpty(orderIds)){
            sql.append(" and (");
            for(String id : orderIds){
                sql.append(" order_id=? or ");
                args.addLast(id);
            }
            sql.append(" 1!=1) ");
        }


        String processId = queryFilter.getProcessId();
        if(!ObjectHelper.isEmpty(processId)){
            sql.append(" and process_id=? ");
            args.addLast(processId);
        }

        String variable = queryFilter.getVariable();
        if(!ObjectHelper.isEmpty(variable)){
            sql.append(" and variable like '%?%'");
            args.addLast(variable);
        }

        String parentId = queryFilter.getParentId();
        if(!ObjectHelper.isEmpty(parentId)){
            sql.append(" and parent_id = ? ");
            args.addLast(parentId);
        }

        String taskType = queryFilter.getTaskType();
        if(!ObjectHelper.isEmpty(taskType)){
            sql.append(" and task_type=? ");
            args.addLast(taskType);
        }


        int version = queryFilter.getVersion();
        if (version != 0) {
            sql.append(" version=? ");
            args.addLast(version);
        }

        if(!ObjectHelper.isEmpty(page)){
            paging.buildPageSql(sql,page);
        }

        return args.toArray();
    }

    /**
     * 创建增强流程定义查询sql语句
     *
     * @param queryFilter  过滤条件
     * @param flowPage     分页条件
     * @param sql          sql语句
     * @return             增强后的sql语句
     */
    private Object[]  createProcessDefinitionSql(QueryFilter queryFilter, FlowPage flowPage, StringBuilder sql){
        LinkedList<Object> args = new LinkedList<>();

        sql.append(" 1=1 ");
        String processId = queryFilter.getProcessId();
        if(!ObjectHelper.isEmpty(processId)){
            sql.append(" and id = ? ");
            args.addLast(processId);
        }

        String processName = queryFilter.getProcessName();
        if(!ObjectHelper.isEmpty(processName)){
            sql.append(" and process_name = ? ");
            args.addLast(processName);
        }

        String status = queryFilter.getStatus();
        if(!ObjectHelper.isEmpty(status)){
            sql.append(" and status = ? ");
            args.addLast(status);
        }

        Integer version = queryFilter.getVersion();
        if(!ObjectHelper.isEmpty(version) && version!=0){
            sql.append(" and version = ? ");
            args.addLast(version);
        }

        String parentId = queryFilter.getParentId();
        if(!ObjectHelper.isEmpty(parentId)){
            sql.append(" and parent_id = ? ");
            args.addLast(parentId);
        }

        String system = queryFilter.getSystem();
        if(!ObjectHelper.isEmpty(system)){
            sql.append(" and system=? ");
            args.addLast(system);
        }


        String createTimeStart = queryFilter.getCreateTimeStart();
        String createTimeEnd = queryFilter.getCreateTimeEnd();
        if(!ObjectHelper.hasEmpty(createTimeEnd,createTimeStart)){
            sql.append(" and create_time between ? and ? ");
            args.addLast(createTimeStart);
            args.addLast(createTimeEnd);
        }

        if(!ObjectHelper.isEmpty(flowPage)){
            paging.buildPageSql(sql,flowPage);
        }

        return args.toArray();
    }



    private String buildInSql(List<String> ids,StringBuilder sql){
        for(int i=0 ; i<ids.size() ; i++){
            if(i < ids.size()-1) {
                sql.append(" ?, ");
            }else if(i == ids.size()-1){
                sql.append(" ? ");
            }
        }
        sql.append(" ) ");
        return sql.toString();
    }



}
