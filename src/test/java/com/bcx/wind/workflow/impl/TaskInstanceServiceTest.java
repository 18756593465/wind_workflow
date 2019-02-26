package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.constant.TaskStatus;
import com.bcx.wind.workflow.core.constant.TaskType;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TaskInstanceServiceTest extends BaseTest {

    private TaskInstance taskInstance(){
        return new TaskInstance()
                .setId(ObjectHelper.primaryKey())
                .setTaskName("edit")
                .setDisplayName("起草")
                .setTaskType(TaskType.ANY)
                .setApproveUser("{}")
                .setCreateTime(TimeHelper.getNow())
                .setApproveCount(1)
                .setStatus(TaskStatus.RUN)
                .setOrderId(ObjectHelper.primaryKey())
                .setProcessId(ObjectHelper.primaryKey())
                .setVariable("{}")
                .setVersion(1)
                .setPosition("/abc/123");
    }

    @Test
    public void add(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);
        assert taskId != null;
        commit();
    }

    @Test
    public void addVariable(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        int ret = engine.runtimeService().taskService().addTaskVariable(taskId,"abc","123");
        assert ret >= 1;
    }

    @Test
    public void removeTaskVariable(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        engine.runtimeService().taskService().addTaskVariable(taskId,"abc","123");

        int ret = engine.runtimeService().taskService().removeTaskVariable(taskId,"abc");
        assert ret >= 1;
    }

    @Test
    public void  getTaskById(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        TaskInstance taskInstance = engine.runtimeService().taskService().getTaskById(taskId);
        assert taskInstance != null;
    }

    @Test
    public void addActor(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        int ret = engine.runtimeService().taskService().addActor(taskId,new DefaultUser().setUserId("10001"));
        assert ret >= 1;
    }


    @Test
    public void addActors(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        List<DefaultUser> actors = new LinkedList<>();
        actors.add(new DefaultUser().setUserId("10001"));
        actors.add(new DefaultUser().setUserId("10001"));

        int ret = engine.runtimeService().taskService().addActor(taskId,actors);
        assert ret >= 1;
    }


    @Test
    public void getActorByTaskId(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        engine.runtimeService().taskService().addActor(taskId,new DefaultUser().setUserId("10001"));
        List<DefaultUser> actors = engine.runtimeService().taskService().getActorByTaskId(taskId);
        System.out.println(actors.size());
        assert actors.size()>=1;
    }


    @Test
    public void removeById(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        int ret = engine.runtimeService().taskService().removeById(taskId);
        assert ret >= 1;
    }

    @Test
    public void removeByOrderId(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        int ret = engine.runtimeService().taskService().removeByOrderId(instance.getOrderId());
        assert ret >= 1;
    }

    @Test
    public void removeActorByTaskIds(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        engine.runtimeService().taskService().addActor(taskId,new DefaultUser().setUserId("10001"));

        int ret = engine.runtimeService().taskService().removeActorByTaskIds(Collections.singletonList(taskId));
        assert ret >= 1;
    }

    @Test
    public void query(){
        List<TaskInstance> taskInstances = engine.runtimeService().taskService().queryList(new QueryFilter());
        System.out.println(taskInstances.size());
    }


    @Test
    public void queryPage(){
        FlowPage<TaskInstance> page = new FlowPage<>();
        List<TaskInstance> taskInstances = engine.runtimeService().taskService().queryList(new QueryFilter(),page);
        System.out.println(taskInstances.size());
    }


    @Test
    public void updateTask(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);
        instance.setTaskType(TaskType.ALL);

        int ret = engine.runtimeService().taskService().updateTask(instance);
        assert ret >= 1;
    }

    @Test
    public void getActorByTaskIds(){
        TaskInstance instance = taskInstance();
        String taskId = engine.runtimeService().taskService().createNewTask(instance);

        engine.runtimeService().taskService().addActor(taskId,new DefaultUser().setUserId("10001"));

        List<DefaultUser> actors = engine.runtimeService().taskService().getActorByTaskIds(new String[]{taskId});
        System.out.println(actors.size());
    }


}
