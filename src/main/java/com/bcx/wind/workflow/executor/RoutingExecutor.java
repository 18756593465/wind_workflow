package com.bcx.wind.workflow.executor;

import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.handler.*;
import com.bcx.wind.workflow.core.pojo.Workflow;
import com.bcx.wind.workflow.core.pojo.WorkflowVariable;
import com.bcx.wind.workflow.exception.WorkflowException;

import static com.bcx.wind.workflow.core.constant.WorkflowOperateConstant.*;

/**
 * @author zhanglei
 */
public class RoutingExecutor implements Executor{

    /**
     * 实际的handler
     */
    private Executor executor;

    public RoutingExecutor(Actuator actuator) {

        String operate = actuator.getOperate().name();
        switch (operate){
            case SUBMIT:
                this.executor = new SubmitExecutor(actuator);
                break;
            case REJECT:
                this.executor = new RejectExecutor(actuator);
                break;
            case BUILD:
                this.executor = new BuildExecutor(actuator);
                break;
            case COMPLETE:
                this.executor = new CompleteExecutor(actuator);
                break;
            case WITHDRAW:
                this.executor = new WithdrawExecutor(actuator);
                break;
            case SCRIBE:
                this.executor = new SubmitExecutor(actuator);
                break;
            case  REVOKE:
                this.executor = new RevokeExecutor(actuator);
                break;
                default:
                    throw new WorkflowException("workflow operate "+operate+" is not exist !");

        }
    }


    @Override
    public Workflow buildWorkflow(WorkflowVariable variable) {
        return executor.buildWorkflow(variable);
    }

    @Override
    public <T extends BuildHandler> Workflow buildWorkflow(WorkflowVariable variable, T handler) {
        return executor.buildWorkflow(variable,handler);
    }

    @Override
    public Workflow submit(WorkflowVariable variable) {
        return executor.submit(variable);
    }


    @Override
    public <T extends SubmitHandler> Workflow submit(WorkflowVariable variable, T handler) {
        return executor.submit(variable,handler);
    }

    @Override
    public Workflow reject(WorkflowVariable variable) {
        return executor.reject(variable);
    }


    @Override
    public <T extends RejectHandler> Workflow reject(WorkflowVariable variable, T handler) {
        return executor.reject(variable,handler);
    }


    @Override
    public Workflow withDraw(WorkflowVariable variable) {
        return executor.withDraw(variable);
    }

    @Override
    public <T extends WithdrawHandler> Workflow withDraw(WorkflowVariable variable, T handler) {
        return executor.withDraw(variable,handler);
    }

    @Override
    public Workflow revoke(WorkflowVariable variable) {
        return executor.revoke(variable);
    }

    @Override
    public <T extends RevokeHandler> Workflow revoke(WorkflowVariable variable, T handler) {
        return executor.revoke(variable,handler);
    }

    @Override
    public Workflow complete(WorkflowVariable variable) {
        return executor.complete(variable);
    }

    @Override
    public <T extends CompleteHandler> Workflow complete(WorkflowVariable variable, T handler) {
        return executor.complete(variable,handler);
    }


}
