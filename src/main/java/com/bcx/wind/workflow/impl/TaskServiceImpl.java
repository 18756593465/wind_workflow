package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.Access;
import com.bcx.wind.workflow.AccessService;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.TaskService;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.User;
import com.bcx.wind.workflow.entity.TaskActor;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import com.bcx.wind.workflow.message.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.Constant.JSON;
import static com.bcx.wind.workflow.message.MsgConstant.*;

/**
 * 任务实例模块
 */
public class TaskServiceImpl extends AccessService implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static final String TASK_INSTANCE = "taskInstance";

    public TaskServiceImpl(Access access) {
        super(access);
    }

    @Override
    public String createNewTask(TaskInstance taskInstance) {
        Assert.notEmpty("taskInstance is null! operate fail",taskInstance);
        if(ObjectHelper.isEmpty(taskInstance.getId())){
            taskInstance.setId(ObjectHelper.primaryKey());
        }
        //校验数据完整
        checkTaskInstance(taskInstance);

        //设置默认值
        addDefaultPro(taskInstance);

        //新增
        access.insertTaskInstance(taskInstance);

        return taskInstance.getId();
    }

    @Override
    public int updateTask(TaskInstance taskInstance) {
        Assert.notEmpty(MessageHelper.getMsg(w002,TASK_INSTANCE),taskInstance.getId());

        //校验数据完整
        checkTaskInstance(taskInstance);

        //更新
        return access.updateTaskInstance(taskInstance);
    }


    private void checkTaskInstance(TaskInstance taskInstance){
        Assert.hasEmpty(MessageHelper.getMsg(w003,TASK_INSTANCE), taskInstance.getTaskName(),
                taskInstance.getDisplayName(),taskInstance.getTaskType(),taskInstance.getStatus(),taskInstance.getOrderId(),
                taskInstance.getProcessId());
    }


    /**
     * 添加默认值
     * @param taskInstance  任务实例
     */
    private  void  addDefaultPro(TaskInstance taskInstance){
        taskInstance
                .setCreateTime(TimeHelper.getNow())
                .setVersion(1)
                .setApproveCount(0);

        if(!ObjectHelper.isEmpty(taskInstance.getVariable())){
            taskInstance.setVariable(JSON);
        }
    }

    @Override
    public int addTaskVariable(String taskId, String key, Object value) {
        Assert.hasEmpty("add taskVariable key or value is null!",key,value);

        TaskInstance taskInstance = getTaskInstance(taskId);
        taskInstance.addVariable(key,value);
        return access.updateTaskInstance(taskInstance);
    }


    private TaskInstance getTaskInstance(String taskId){
        Assert.notEmpty(MessageHelper.getMsg(w002,TASK_INSTANCE),taskId);

        TaskInstance taskInstance = getTaskById(taskId);
        Assert.notEmpty("taskId is not exist!",taskInstance);
        return taskInstance;
    }

    @Override
    public int removeTaskVariable(String taskId, String key) {
        Assert.hasEmpty("add taskVariable key  is null!",key);
        TaskInstance taskInstance = getTaskInstance(taskId);
        taskInstance.removeKey(key);
        return access.updateTaskInstance(taskInstance);
    }

    @Override
    public int removeById(String taskId) {
        Assert.notEmpty(MessageHelper.getMsg(w002,TASK_INSTANCE),taskId);

        return access.removeTaskInstanceById(taskId);
    }

    @Override
    public int removeByOrderId(String orderId) {
        Assert.notEmpty("orderId is null,removeTask fail!",orderId);
        return access.removeTaskByOrderId(orderId);
    }

    @Override
    public int removeByTaskIds(List<String> taskIds) {
        Assert.hasEmpty("remove task by ids ,but ids has empty,remove fail",taskIds);
        return access.removeTaskByTaskIds(taskIds);
    }

    @Override
    public int removeActorByTaskId(String taskId) {
        Assert.notEmpty("remove actor by taskId , but taskId is null! remove fail!",taskId);
        return access.removeTaskActorByTaskId(taskId);
    }

    @Override
    public int removeActorByTaskIds(List<String> taskIds) {
        Assert.hasEmpty("remove actor by taskIds , but taskIds has null! remove fail!",taskIds);

        return access.removeTaskActorByTaskIds(taskIds);
    }

    @Override
    public int addActor(String taskId, List<DefaultUser> actors) {
        getTaskInstance(taskId);
        Assert.hasEmpty("add taskActor ,but actors has empty",actors);

        for(User actor : actors){
           access.insertTaskActor(new TaskActor()
                  .setTaskId(taskId)
                  .setTaskActorId(actor.userId())
                  .setActorVariable(JsonHelper.toJson(actor)));
        }
        return 1;
    }

    @Override
    public int addActor(String taskId, User user) {
        getTaskInstance(taskId);
        Assert.isTrue("add actor for task, but actor is null",ObjectHelper.isEmpty(user) || ObjectHelper.isEmpty(user.userId()) );

        return access.insertTaskActor(new TaskActor()
                  .setTaskId(taskId)
                  .setTaskActorId(user.userId())
                  .setActorVariable(JsonHelper.toJson(user)));
    }

    @Override
    public int removeActor(String taskId, String actor) {
        Assert.hasEmpty("remove actor by taskId and actor ,but taskId or actor has null,remove fail",taskId,actor);
        return access.removeTaskActor(taskId,actor);
    }

    @Override
    public List<DefaultUser> getActorByTaskId(String taskId) {
        Assert.notEmpty("search taskActor by taskId, but taskId is null! search fail",taskId);
        List<TaskActor> actors = access.selectTaskActorList(new QueryFilter().setTaskId(taskId));
        return getUsersByActorVariable(actors);
    }


    @Override
    public List<DefaultUser> getActorByTaskIds(String[] taskIds) {
        Assert.hasEmpty("search taskActor by taskIds, but taskIds has null! search fail",taskIds);
        List<TaskActor> actors = access.selectTaskActorList(new QueryFilter().setTaskIds(taskIds));
        return getUsersByActorVariable(actors);
    }


    private List<DefaultUser>  getUsersByActorVariable(List<TaskActor> actors){
        List<DefaultUser> users = new LinkedList<>();
        if(!ObjectHelper.isEmpty(actors)){
            for(TaskActor actor : actors){
                Map<String,Object> userMap = actor.getActorVariableMap();

                if(!ObjectHelper.isEmpty(userMap)){
                    DefaultUser user = JsonHelper.coverObject(userMap,DefaultUser.class);
                    users.add(user);
                }
            }
            return users;
        }
        return  Collections.emptyList();
    }

    @Override
    public TaskInstance getTaskById(String taskId) {
        Assert.notEmpty(MessageHelper.getMsg(w002,TASK_INSTANCE),taskId);

        return access.getTaskInstanceById(taskId);
    }

    @Override
    public List<TaskInstance> queryList(QueryFilter filter) {
        Assert.notEmpty(MessageHelper.getMsg(w001),filter);

        return access.selectTaskInstanceList(filter);
    }

    @Override
    public List<TaskInstance> queryList(QueryFilter filter, FlowPage<TaskInstance> page) {
        Assert.notEmpty(MessageHelper.getMsg(w001),filter);

        return access.selectTaskInstanceList(filter,page);
    }
}
