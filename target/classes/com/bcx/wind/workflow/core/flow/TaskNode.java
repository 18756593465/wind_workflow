package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.constant.NodeName;
import com.bcx.wind.workflow.core.constant.NodeType;
import com.bcx.wind.workflow.core.pojo.ApproveUser;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.Task;
import com.bcx.wind.workflow.core.pojo.User;
import com.bcx.wind.workflow.entity.ActiveHistory;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.executor.handler.TaskHandler;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import com.bcx.wind.workflow.message.MessageHelper;
import org.w3c.dom.Element;

import java.util.*;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.Constant.AND;
import static com.bcx.wind.workflow.core.constant.Constant.*;
import static com.bcx.wind.workflow.core.constant.Constant.OR;
import static com.bcx.wind.workflow.core.constant.NodeName.*;
import static com.bcx.wind.workflow.core.constant.OrderVariableKey.TASK_APPROVE_USER;
import static com.bcx.wind.workflow.core.constant.TaskStatus.RUN;
import static com.bcx.wind.workflow.core.constant.TaskType.ALL;
import static com.bcx.wind.workflow.core.constant.TaskType.ANY;
import static com.bcx.wind.workflow.core.constant.TaskVariableKey.MIN_COUNT;
import static com.bcx.wind.workflow.core.constant.TaskVariableKey.TASK_SUBMIT_USER;
import static com.bcx.wind.workflow.core.constant.WorkflowOperateConstant.SUBMIT;
import static com.bcx.wind.workflow.message.MsgConstant.*;

/**
 * 任务节点
 *
 * @author zhanglei
 */
public class TaskNode extends BaseNode implements TaskModel{

    /**
     * 是否为会签
     */
    private boolean jointly;

    /**
     * 拦截器
     */
    private boolean interceptor;

    /**
     * 是否在or分支中
     */
    private boolean inOr;

    /**
     * 是否在and分支中
     */
    private boolean inAnd;



    /**
     * 当前任务是否往后执行
     */
    private boolean next = false;


    /**
     * 审核人
     */
    private List<DefaultUser>  approveUsers;

    public TaskNode(String name, String displayName) {
        super(name, displayName);
        this.nodeType = NodeType.TASK;
    }

    @Override
    public void executor() {
        //完成当前任务
        complete();

        String submitNode = variable().getSubmitNode();
        if(!ObjectHelper.isEmpty(submitNode) && this.next){
            submitNodeNext(submitNode);
            return;
        }
        //执行后续
        if(this.next) {
            next(null);
        }
    }


    private  void  submitNodeNext(String submitNode){
        NodeModel nodeModel = getTaskModel(submitNode);
        TaskNode taskNode = (TaskNode)nodeModel;
        this.handler(new TaskHandler(this.actuator,taskNode,this.task));
    }


    private void complete(){
        //是否会签
        int joint = nodeConfig().getJointly();
        if(joint==1 || (this.jointly && joint==0)){
            //会签
            jointlyExec();
        }else if(this.inOr){
            //或者分支
            inOrExec();
        }else if(this.inAnd){
            //并且分支
            inAndExec();
        }else{
            simpleExec();
        }
    }


    /**
     * 会签执行
     */
    private void jointlyExec(){
        //当前流程所有任务
        getTaskInstance();
        //审批率配置
        int approveTime = getApproveTime();
        //最少审批人
        int minCount = getMinCount();

        //当前已经审批次数
        int approveCount = task.getTaskInstance().getApproveCount();

        //最少审批人配置有限
        if(minCount>=1){
            jointlyComplete(minCount,approveCount);

        }else if(approveTime >= 1){
            //审批率
            jointlyComplete(approveTime,approveCount);

        }else{
            //一般会签，必须完成所有任务
            if(getTaskInstance().size() > 1){
                completeUserTask(0);
                addTaskApproveUserToOrderVariable(false);
            }else{
                completeTask();
                addTaskApproveUserToOrderVariable(true);
                if(!this.inAnd) {
                    this.next = true;
                }else{
                    this.next = this.actuator.getNowAllTasks().size() == 1;
                }
            }
        }


        //会签合并审批人
        if(!ObjectHelper.isEmpty(this.actuator.getTaskInstance())) {

            int approveUserMergeSet = nodeConfig().getJointlyApproveUserSet();
            if (approveUserMergeSet == 1) {
                submitMergeUserBuild();
            }
        }
    }


    /**
     * 处理会签审批人合并配置
     */
    private void  submitMergeUserBuild(){
        List<ApproveUser> users = workflow().getVariable().getApproveUsers();
        String freeSubmitNode = workflow().getVariable().getSubmitNode();

        Map<String,Object> variable = task.getTaskInstance().getVariableMap();
        Object submitUserObject = variable.get(TASK_SUBMIT_USER);
        for(ApproveUser approveUser : users) {
            String nodeId = approveUser.getNodeId();

            if (!ObjectHelper.isEmpty(freeSubmitNode)) {
                TaskModel taskNode = processModel().getTaskModel(freeSubmitNode);
                Assert.notEmpty(MessageHelper.getMsg(w017, freeSubmitNode), taskNode);
                Assert.isTrue(MessageHelper.getMsg(w017, nodeId), !nodeId.equals(freeSubmitNode));

                addSubmitMergeUsers(submitUserObject, freeSubmitNode);
                break;
            }

            List<NodeModel> taskNodes = this.nextTaskNodes();
            Assert.isTrue(MessageHelper.getMsg(w017, nodeId),
                    taskNodes.stream().noneMatch(t -> t.name().equals(nodeId)));

            addSubmitMergeUsers(submitUserObject, nodeId);
        }

    }



    @SuppressWarnings("unchecked")
    private void  addSubmitMergeUsers(Object submitUserObject,String nodeId){
        if(!ObjectHelper.isEmpty(submitUserObject)) {
            Map<String, List<String>> submitUsers = JsonHelper.coverObject(submitUserObject, Map.class, String.class, List.class);
            List<String> actors = submitUsers.get(nodeId);
            if(!ObjectHelper.isEmpty(actors)){
                actors.addAll(approveUsers.stream().map(DefaultUser::getUserId).collect(Collectors.toList()));
            }
            actors = approveUsers.stream().map(DefaultUser::getUserId).collect(Collectors.toList());
            submitUsers.put(nodeId,actors);
            task.getTaskInstance().addVariable(TASK_SUBMIT_USER,submitUsers);

        }else{

            Map<String, List<String>> submitUsers = new HashMap<>();
            submitUsers.put(nodeId,approveUsers.stream().map(DefaultUser::getUserId).collect(Collectors.toList()));
            task.getTaskInstance().addVariable(TASK_SUBMIT_USER,submitUsers);
        }
    }



    private void inOrExec(){
        completeTask();
        this.next = true;
    }


    private void inAndExec(){
        getTaskInstance();
        List<NodeModel> nextNodes = this.nextNodes;
        if(!ObjectHelper.isEmpty(nextNodes)){
            //查看下一个节点是否为聚合
            NodeModel nodeModel = nextNodes.get(0);
            if(AND_JOIN.equals(nodeModel.nodeType().value())){
                completeUserTask(0);
                this.next = this.actuator.getNowAllTasks().size()==1;

                //会签合并审批人
                if(!this.next) {
                    int approveUserMergeSet = nodeConfig().getJointlyApproveUserSet();
                    if (approveUserMergeSet == 1) {
                        submitMergeUserBuild();
                    }
                }
            }else{
                completeTask();
                this.next = true;
            }
        }
    }


    private  void simpleExec(){
        completeTask();
        addTaskApproveUserToOrderVariable(false);
        this.next = true;
    }



    private void  jointlyComplete(int minCount,int approveCount){
        //审批率配置
        if(minCount - approveCount == 1) {
            completeTask();
            addTaskApproveUserToOrderVariable(true);
            if(!this.inAnd) {
                this.next = true;
            }else{
                if(this.actuator.getNowAllTasks().size()==1){
                    this.next = true;
                }
            }
        }else {
            completeUserTask(minCount);
            addTaskApproveUserToOrderVariable(false);
        }
    }


    /**
     * 获取最小审批次数
     * @return  审批次数
     */
    private int  getMinCount(){
        Object min = task.getTaskInstance().getVariableMap().get(MIN_COUNT);
        if(!ObjectHelper.isEmpty(min)){
            return (int)min;
        }
        int minCount = nodeConfig().getJointlyApproveUserMinCount();
        if(minCount==0){
            return 0;
        }
        int userCount = getTaskInstance().size();
        minCount = minCount < 0 ? userCount : minCount;
        minCount = minCount > userCount ? userCount : minCount;
        return minCount;
    }



    /**
     * 获取会签任务审批率计算出来的最少审批人数量
     * @return                 最少审批人刷量
     */
    private int  getApproveTime(){
        Object object = task.getTaskInstance().getVariableMap().get(MIN_COUNT);
        if(!ObjectHelper.isEmpty(object)){
            return (int)object;
        }

        //审批率配置
        String approveTime = nodeConfig().getJointlyApproveTimeSet();
        if(ObjectHelper.isEmpty(approveTime)){
            return 0;
        }

        try {
            Float f = Float.parseFloat(approveTime);
            int taskSize = getTaskInstance().size();
            int time =  (int) (taskSize * f);
            return time < 1 ? 1 : time;
        }catch (Exception e){
            throw new WorkflowException(e.getMessage());
        }
    }


    /**
     * 查询当前任务审批人
     * @return  任务审批人
     */
    private List<TaskInstance> getTaskInstance(){
        List<TaskInstance> instance = this.actuator.getTaskInstance();
        List<TaskInstance>  allTasks = this.actuator.getNowAllTasks();
        if(ObjectHelper.isEmpty(instance)){
            QueryFilter filter = new QueryFilter()
                    .setOrderId(workflow().getOrderId())
                    .setTaskName(this.name);
            instance = engine().runtimeService().taskService().queryList(filter);

            if(!ObjectHelper.isEmpty(instance)){
                List<String> taskIds = instance.stream().map(TaskInstance::getId)
                        .collect(Collectors.toList());
                List<String> actors = engine().runtimeService().taskService().getActorByTaskIds(taskIds.toArray(new String[taskIds.size()]));
                this.actuator.setActors(actors);
            }

            this.actuator.setTaskInstance(instance);
        }

        if(ObjectHelper.isEmpty(allTasks)){
            QueryFilter filter = new QueryFilter()
                    .setOrderId(workflow().getOrderId());
            allTasks = engine().runtimeService().taskService().queryList(filter);
            this.actuator.setNowAllTasks(allTasks);
        }
        return instance;
    }


    /**
     * 添加审批人到流程实例中，为后期退回操作做数据铺垫
     *
     * @param over   当前任务是否全部完成
     */
    @SuppressWarnings("unchecked")
    private  void  addTaskApproveUserToOrderVariable(boolean over){

        //如果是子流程
        if(ROUTER.equals(this.parentNode.nodeType().value())){
            List<OrderInstance> childs = workflow().getChildOrderInstance();
            for(OrderInstance child : childs){

                Object approveUser = child.getVariableMap().get(TASK_APPROVE_USER);
                addTaskApproveUserToOrderVariable(approveUser,over,child);
            }
        }else{
            OrderInstance instance = workflow().getOrderInstance();
            Object approveUser = instance.getVariableMap().get(TASK_APPROVE_USER);
            addTaskApproveUserToOrderVariable(approveUser,over,instance);
        }
    }




    @SuppressWarnings("unchecked")
    private void addTaskApproveUserToOrderVariable(Object approveUser,boolean over,OrderInstance orderInstance){
        //用户
        User user = workflow().getUser();

        if(ObjectHelper.isEmpty(approveUser)){
            Map<String,List<String>> taskApproveUser = new HashMap<>();
            if(over){
                List<String> actors = this.actuator.getActors();
                taskApproveUser.put(this.name,actors);
            }else{
                taskApproveUser.put(this.name,Collections.singletonList(user.userId()));
            }
            orderInstance.addValue(TASK_APPROVE_USER,taskApproveUser);

        }else{

            Map<String,List<String>> taskApproveUser = JsonHelper.coverObject(approveUser,Map.class,String.class,List.class);
            List<String> users = taskApproveUser.get(this.name);
            if(over){
                List<String> actors = this.actuator.getActors();
                if(users!=null){
                    users.addAll(actors);
                }
                taskApproveUser.put(this.name,users);
            }
            if(users!=null) {
                users.add(user.userId());
            }
            taskApproveUser.put(this.name,users);
            orderInstance.addValue(TASK_APPROVE_USER,taskApproveUser);
        }
        engine().runtimeService().orderService().update(orderInstance);
    }


    /**
     * 完成结束当前任务
     */
    private  void  completeTask(){
        //完成任务
        String orderId = workflow().getOrderId();
        //流程下所有任务
        QueryFilter filter = new QueryFilter().setOrderId(orderId);
        List<TaskInstance> taskInstances = engine().runtimeService().taskService().queryList(filter);

        List<String> taskIds = new LinkedList<>();
        for(TaskInstance task : taskInstances){
            taskIds.add(task.getId());
        }

        //删除审批人
        engine().runtimeService().taskService().removeActorByTaskIds(taskIds);
        //删除任务
        engine().runtimeService().taskService().removeByOrderId(orderId);
        //更新履历
        updateActiveHistory();
    }




    /**
     * 完成当前用户的任务，即删除当前任务的一个审批人
     */
    private   void  completeUserTask(int minCount){
        //完成任务
        User user = workflow().getUser();
        String taskId = this.task.getTaskInstance().getId();
        engine().runtimeService().taskService().removeById(taskId);
        engine().runtimeService().taskService().removeActor(taskId,user.userId());

        //更新履历
        updateActiveHistory();

        //任务已经审批次数
        TaskInstance taskInstance = this.task.getTaskInstance();
        taskInstance.setApproveCount(taskInstance.getApproveCount()+1);
        if(minCount != 0) {
            taskInstance.addVariable(MIN_COUNT, minCount);
        }
        engine().runtimeService().taskService().updateTask(taskInstance);
    }

    /**
     * 完成任务后，更新履历信息
     */
    private  void  updateActiveHistory(){
        User user = workflow().getUser();
        QueryFilter queryFilter = new QueryFilter()
                .setTaskId(task.getTaskInstance().getId());
        List<ActiveHistory> histories = engine().historyService().activeHistoryService().queryList(queryFilter);
        if(!ObjectHelper.isEmpty(histories)){
            ActiveHistory history = histories.get(0);
            history.setOperate(operate())
                    .setSuggest(workflow().getVariable().getSuggest())
                    .setApproveTime(TimeHelper.getNow())
                    .setActorId(user.userId())
                    .setActorName(user.userName())
                    .setSubmitUserVariable(JsonHelper.toJson(user));

            engine().historyService().activeHistoryService().update(history);
        }
    }



    /**
     * 创建任务
     *
     * @param taskNode     任务节点
     * @param processId    模型ID
     * @return             任务实例
     */
    private TaskInstance createTask(TaskNode taskNode,String processId){
        String taskId = ObjectHelper.primaryKey();
        TaskInstance taskInstance = new TaskInstance()
                .setId(taskId)
                .setApproveCount(0)
                .setCreateTime(TimeHelper.getNow())
                .setVariable(JSON)
                .setVersion(1)
                .setTaskType(taskNode.isJointly() ? ALL : ANY)
                .setApproveUser(JSON)
                .setTaskName(taskNode.getName())
                .setDisplayName(taskNode.getDisplayName())
                .setOrderId(this.actuator.getWorkflow().getOrderId())
                .setStatus(RUN)
                .setProcessId(processId);

        engine().runtimeService().taskService().createNewTask(taskInstance);

        //审批人
        List<DefaultUser> users = new LinkedList<>();
        //添加审批人
        if(SUBMIT.equals(this.actuator.getOperate())){
            List<ApproveUser> approveUsers = this.actuator.getWorkflow().getVariable().getApproveUsers();
            Assert.notEmpty(MessageHelper.getMsg(w011),approveUsers);

            for(ApproveUser approveUser : approveUsers){
                String nodeId = approveUser.getNodeId();
                if(taskInstance.getTaskName().equals(nodeId)){
                    users = approveUser.getApproveUsers();

                    List<String> actors = new LinkedList<>();
                    for(User user : users){
                        actors.add(user.userId());
                    }
                    engine().runtimeService().taskService().addActor(taskId,actors);
                    break;
                }
            }

        }

        //添加执行历史履历
        addActiveResume(taskInstance,users);

        return taskInstance;
    }


    /**
     * 添加执行历史履历  该任务尚未完成
     *
     * @param taskInstance  任务实例
     * @param users         审批人
     */
    private  void addActiveResume(TaskInstance taskInstance,List<DefaultUser> users){
        ActiveHistory activeHistory = new ActiveHistory()
                .setId(ObjectHelper.primaryKey())
                .setApproveUserVariable(JsonHelper.toJson(users))
                .setCreateTime(TimeHelper.getNow())
                .setOrderId(taskInstance.getOrderId())
                .setProcessDisplayName(this.actuator.getProcessModel().getDisplayName())
                .setProcessId(this.actuator.getProcessModel().getProcessId())
                .setProcessName(this.actuator.getProcessModel().getName())
                .setSystem(this.actuator.getWorkflow().getVariable().getSystem())
                .setTaskDisplayName(taskInstance.getDisplayName())
                .setTaskId(taskInstance.getId())
                .setTaskName(taskInstance.getTaskName())
                .setTaskType(taskInstance.getTaskType());

        engine().historyService().activeHistoryService().insert(activeHistory);
    }


    public void build(){
        buildPaths(now);
        //构建节点指针
        createNodePointer();
    }

    public Element getNow() {
        return now;
    }

    public void setNow(Element now) {
        this.now = now;
    }

    public boolean isJointly() {
        return jointly;
    }

    public void setJointly(boolean jointly) {
        this.jointly = jointly;
    }

    @Override
    public List<NodeModel> getSubmitLineNodes(String submitLine) {
        for(Path path : this.paths){

            if(submitLine.equals(path.getName())){
                return this.nextTasks;

            }else{
                NodeModel nodeModel = path.getNextNode();
                if(DISC.equals(nodeModel.nodeType().value())){
                    List<Path> discPaths = nodeModel.path();
                    List<NodeModel> nextNodes = getSubmitLineNodes(submitLine,discPaths);
                    if(!ObjectHelper.isEmpty(nextNodes)){
                        return nextNodes;
                    }
                }
            }
        }
        return super.getSubmitLineNodes(submitLine);
    }


    private List<NodeModel> getSubmitLineNodes(String submitLine, List<Path> paths) {
        for(Path path : paths){

            if(submitLine.equals(path.getName())){
                NodeModel nodeModel = path.getNextNode();
                if(TASK.equals(nodeModel.nodeType().value())){
                    return Collections.singletonList(nodeModel);
                }else if(AND.equals(nodeModel.nodeType().value())){
                    return nodeModel.nextTaskNodes();
                }else if(OR.equals(nodeModel.nodeType().value())){
                    return nodeModel.nextTaskNodes();
                }else if(DISC.equals(nodeModel.nodeType().value())){
                    return nodeModel.nextTaskNodes();
                }else{
                    return Collections.emptyList();
                }
            }else{
                NodeModel nodeModel = path.getNextNode();
                if(DISC.equals(nodeModel.nodeType().value())){
                    List<Path> discPaths = nodeModel.path();
                    return getSubmitLineNodes(submitLine,discPaths);
                }
            }
        }
        return Collections.emptyList();
    }


    public boolean isInterceptor() {
        return interceptor;
    }

    public void setInterceptor(boolean interceptor) {
        this.interceptor = interceptor;
    }

    public List<DefaultUser> getApproveUsers() {
        return approveUsers;
    }

    public void setApproveUsers(List<DefaultUser> approveUsers) {
        this.approveUsers = approveUsers;
    }

    public boolean isInOr() {
        return inOr;
    }

    public void setInOr(boolean inOr) {
        this.inOr = inOr;
    }

    public boolean isInAnd() {
        return inAnd;
    }

    public void setInAnd(boolean inAnd) {
        this.inAnd = inAnd;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }


    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    @Override
    public NodeModel parentNode() {
        return this.parentNode;
    }
}
