package com.bcx.wind.workflow.access.db;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.entity.TaskActor;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class TaskActorAccessTest extends BaseTest {

    @Test
    public void add(){
        TaskActor actor = new TaskActor()
                .setTaskId(ObjectHelper.primaryKey())
                .setTaskActorId("10001");
        int ret = access.insertTaskActor(actor);
        assert ret >= 1;
        commit();
    }

    @Test
    public void remove(){
        TaskActor actor = new TaskActor()
                .setTaskId(ObjectHelper.primaryKey())
                .setTaskActorId("10001");
        access.insertTaskActor(actor);
        int ret = access.removeTaskActor(actor.getTaskId(),actor.getTaskActorId());
        assert ret >= 1;
    }

    @Test
    public void removeByTaskId(){
        TaskActor actor = new TaskActor()
                .setTaskId(ObjectHelper.primaryKey());
        access.insertTaskActor(actor);
        int ret = access.removeTaskActorByTaskId(actor.getTaskId());
        assert ret >= 1;
    }

    @Test
    public void removeByTaskIds(){
        TaskActor actor = new TaskActor()
                .setTaskId(ObjectHelper.primaryKey());
        access.insertTaskActor(actor);

        List<String> taskIds = new LinkedList<>();
        taskIds.add(actor.getTaskId());

        int ret = access.removeTaskActorByTaskIds(taskIds);
        assert ret >= 1;
    }

    @Test
    public void query(){
        QueryFilter filter = new QueryFilter();
        List<TaskActor>  actors = access.selectTaskActorList(filter);
        System.out.println(actors.size());
    }
}
