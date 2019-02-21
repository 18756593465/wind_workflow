package com.bcx.wind.workflow.access.db;

import com.bcx.wind.workflow.BaseTest;
import com.bcx.wind.workflow.access.FlowPage;
import com.bcx.wind.workflow.access.QueryFilter;
import com.bcx.wind.workflow.core.constant.TaskStatus;
import com.bcx.wind.workflow.entity.TaskInstance;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;
import org.junit.Test;

import java.util.List;

public class TaskInstanceAccessTest extends BaseTest {

    private TaskInstance taskInstance(){
        return new TaskInstance()
                .setId(ObjectHelper.primaryKey())
                .setTaskName("approve")
                .setDisplayName("审批")
                .setTaskType("all")
                .setCreateTime(TimeHelper.getNow())
                .setApproveCount(0)
                .setStatus(TaskStatus.RUN)
                .setOrderId(ObjectHelper.primaryKey())
                .setProcessId(ObjectHelper.primaryKey())
                .setVariable("{}")
                .setVersion(1)
                .setPosition("/ac/d");
    }

    @Test
    public void add(){
        TaskInstance taskInstance = taskInstance();

        int ret = access.insertTaskInstance(taskInstance);
        assert ret >= 1;
    }


    @Test
    public void update(){
        TaskInstance taskInstance = taskInstance();

        access.insertTaskInstance(taskInstance);

        taskInstance.setPosition("/dsf/s");
        int ret = access.updateTaskInstance(taskInstance);
        assert ret >= 1;
    }

    @Test
    public void remove(){
        TaskInstance taskInstance = taskInstance();

        access.insertTaskInstance(taskInstance);

        int ret = access.removeTaskInstanceById(taskInstance.getId());
        assert ret >= 1;
    }

    @Test
    public void queryOne(){
        TaskInstance taskInstance = taskInstance();

        access.insertTaskInstance(taskInstance);

        TaskInstance instance = access.getTaskInstanceById(taskInstance.getId());
        assert instance!=null;
    }

    @Test
    public void query(){
        List<TaskInstance> list = access.selectTaskInstanceList(new QueryFilter());
        System.out.println(list.size());
    }

    @Test
    public void queryPage(){
        FlowPage<TaskInstance> page = new FlowPage<>();
        List<TaskInstance> list = access.selectTaskInstanceList(new QueryFilter(),page);

        System.out.println(list.size());
    }

}
