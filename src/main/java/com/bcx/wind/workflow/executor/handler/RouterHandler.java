package com.bcx.wind.workflow.executor.handler;

import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.constant.OrderType;
import com.bcx.wind.workflow.core.flow.NodeModel;
import com.bcx.wind.workflow.core.flow.RouterNode;
import com.bcx.wind.workflow.core.flow.TaskNode;
import com.bcx.wind.workflow.core.pojo.Task;
import com.bcx.wind.workflow.entity.OrderInstance;
import com.bcx.wind.workflow.helper.JsonHelper;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.helper.TimeHelper;

import static com.bcx.wind.workflow.core.constant.Constant.JSON;

/**
 * @author zhanglei
 */
public class RouterHandler extends BaseHandler implements Handler {


    public RouterHandler(Actuator actuator, NodeModel now, Task task) {
        super(actuator, now, task);
    }

    @Override
    public void handler() {
        createChildOrder();
        TaskNode taskNode = (TaskNode) getNow().getFirstTaskNode();
        this.handler(new TaskHandler(this.actuator,taskNode,this.task));
    }


    private void handler(Handler handler){
       handler.handler();
    }


    /**
     * 构建子流程实例
     */
    private void createChildOrder(){
        OrderInstance orderInstance = new OrderInstance()
                .setId(ObjectHelper.primaryKey())
                .setProcessId(actuator.getProcessModel().getProcessId())
                .setStatus(OrderType.RUN)
                .setCreateUser(user().userId())
                .setCreateTime(TimeHelper.getNow())
                .setParentId(workflow().getOrderId())
                .setVersion(1)
                .setVariable(JSON)
                .setData(JsonHelper.toJson(workflow().getDataMap()))
                .setSystem(workflow().getSystem());
        engine().runtimeService().orderService().insert(orderInstance);

        //设置执行数据
        workflow().getChildOrderInstance().add(orderInstance);
    }

    public RouterNode getNow() {
        return (RouterNode) now;
    }


    public RouterHandler setNow(RouterNode now) {
        this.now = now;
        return this;
    }

    public Task getTask() {
        return task;
    }

    public RouterHandler setTask(Task task) {
        this.task = task;
        return this;
    }
}
