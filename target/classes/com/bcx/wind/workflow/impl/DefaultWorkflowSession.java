package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.core.Actuator;
import com.bcx.wind.workflow.core.WorkflowSession;
import com.bcx.wind.workflow.core.constant.ErrorContant;
import com.bcx.wind.workflow.core.constant.WorkflowOperate;
import com.bcx.wind.workflow.core.handler.*;
import com.bcx.wind.workflow.core.lock.MsgContent;
import com.bcx.wind.workflow.core.pojo.*;
import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.executor.Executor;
import com.bcx.wind.workflow.executor.RoutingExecutor;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 工作流核心实现
 *
 * @author zhanglei
 */
public  class DefaultWorkflowSession  implements WorkflowSession {

    private static final Logger logger = LoggerFactory.getLogger(DefaultWorkflowSession.class);

    /**
     * 秘书
     */
    private Executor executor;

    /**
     * 执行数据
     */
    private Actuator actuator;

    public DefaultWorkflowSession(Actuator actuator) {
       this.actuator = actuator;
    }

    public Executor getExecutor() {
        return executor;
    }

    public DefaultWorkflowSession setExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }

    public Actuator getActuator() {
        return actuator;
    }

    public DefaultWorkflowSession setActuator(Actuator actuator) {
        this.actuator = actuator;
        return this;
    }

    /**
     * 校验构建工作流参数
     * @param variable  参数
     */
    private void checkBuildArgs(WorkflowVariable variable){
        Assert.notEmpty("buildWorkflow require parameters processName user businessId has null!",variable);
        Assert.isTrue("buildWorkflow require parameters processName user businessId has null!",
              ObjectHelper.hasEmpty(variable.getBusinessId())
                   || ObjectHelper.isEmpty(variable.getUser()) || ObjectHelper.isEmpty(variable.getUser().userId()));
    }

    @Override
    public Workflow buildWorkflow(WorkflowVariable variable) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"build workflow");
        //校验参数
        checkBuildArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap())
                .setOperate(WorkflowOperate.build).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.buildWorkflow(variable);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public Workflow buildWorkflow( DefaultUser user, List<String> businessId) {
        WorkflowVariable variable = new WorkflowVariable()
                .setProcessName(this.actuator.getProcessModel().getName())
                .setUser(user)
                .setBusinessId(businessId);
        return buildWorkflow(variable);
    }

    @Override
    public Workflow buildWorkflow(DefaultUser user, List<String> businessId, String suggest) {
        WorkflowVariable variable = new WorkflowVariable()
                .setProcessName(this.actuator.getProcessModel().getName())
                .setUser(user)
                .setBusinessId(businessId)
                .setSuggest(suggest);
        return buildWorkflow(variable);
    }

    @Override
    public <T extends BuildHandler> Workflow buildWorkflow(WorkflowVariable variable, T handler) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"build workflow");
        checkBuildArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.build).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.buildWorkflow(variable, handler);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public <T extends BuildHandler> Workflow buildWorkflow(DefaultUser user, List<String> businessId, T handler) {
        WorkflowVariable variable = new WorkflowVariable()
                .setProcessName(this.actuator.getProcessModel().getName())
                .setUser(user)
                .setBusinessId(businessId);
        return buildWorkflow(variable,handler);
    }

    @Override
    public Workflow submit(WorkflowVariable variable) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"submit workflow");
        checkSubmitArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.submit).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.submit(variable);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }


    /**
     * 校验提交工作流参数
     * @param variable  参数
     */
    private void checkSubmitArgs(WorkflowVariable variable){
        Assert.notEmpty("submitWorkflow require parameters orderId user approveUsers has null!",variable);
             Assert.isTrue("submitWorkflow require parameters orderId user approveUsers has null!",
                ObjectHelper.isEmpty(variable.getOrderId())
                        || ObjectHelper.isEmpty(variable.getUser()) || ObjectHelper.isEmpty(variable.getUser().userId()));


        for(ApproveUser user : variable.getApproveUsers()){
            String nodeId = user.getNodeId();
            List<DefaultUser> users = user.getApproveUsers();
            Assert.notEmpty("submitWorkflow require parameters approve nodeId is null",nodeId);
            Assert.hasEmpty("submitWorkflow require parameters approveUsers has null",users);
            for(User u :users){
                String userId = u.userId();
                Assert.notEmpty("submitWorkflow require parameters approveUser userId has null",userId);
            }
        }
    }



    @Override
    public Workflow submit(String orderId, DefaultUser user, List<ApproveUser> approveUsers) {
        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(user)
                .setApproveUsers(approveUsers);
        return submit(variable);
    }

    @Override
    public <T extends SubmitHandler> Workflow submit(WorkflowVariable variable, T handler) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"submit workflow");
        checkSubmitArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.submit).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.submit(variable, handler);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public <T extends SubmitHandler> Workflow submit(String orderId, DefaultUser user, List<ApproveUser> approveUsers, T handler) {
        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(user)
                .setApproveUsers(approveUsers);

        return submit(variable,handler);
    }

    private void checkRejectArgs(WorkflowVariable variable){
        Assert.notEmpty("rejectWorkflow require parameters orderId user has null!",variable);
        Assert.isTrue("rejectWorkflow require parameters orderId user has null!",ObjectHelper.isEmpty(variable.getOrderId()) ||
                    ObjectHelper.isEmpty(variable.getUser()) || ObjectHelper.isEmpty(variable.getUser().userId()));
    }

    @Override
    public Workflow reject(WorkflowVariable variable) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"reject workflow");
        checkRejectArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.reject).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.reject(variable);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public Workflow reject(String orderId,String returnNode, DefaultUser user) {
        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(user)
                .setSubmitNode(returnNode);
        return reject(variable);
    }

    @Override
    public <T extends RejectHandler> Workflow reject(WorkflowVariable variable, T handler) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"reject workflow");
        checkRejectArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.reject).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.reject(variable, handler);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public <T extends RejectHandler> Workflow reject(String orderId,String returnNode, DefaultUser user, T handler) {
        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(user)
                .setSubmitNode(returnNode);
        return reject(variable,handler);
    }

    private void checkWithdrawArgs(WorkflowVariable variable){
        Assert.notEmpty("withdrawWorkflow require parameters businessId user has null",variable);
        Assert.isTrue("withdrawWorkflow require parameters businessId user has null",ObjectHelper.isEmpty(variable.getBusinessId())
                || ObjectHelper.isEmpty(variable.getUser()) || ObjectHelper.isEmpty(variable.getUser().userId()));

        for(String businessId : variable.getBusinessId()){
            Assert.notEmpty("withdrawWorkflow require parameters businessId has null",businessId);
        }
    }

    @Override
    public Workflow withDraw(WorkflowVariable variable) {

        MsgContent.getInstance().setContent(ErrorContant.Operate,"withDraw workflow");
        checkWithdrawArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.withdraw).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.withDraw(variable);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public Workflow withDraw(List<String> businessId, DefaultUser user) {
        WorkflowVariable variable = new WorkflowVariable()
                .setBusinessId(businessId)
                .setUser(user);
        return withDraw(variable);
    }

    @Override
    public <T extends WithdrawHandler> Workflow withDraw(WorkflowVariable variable, T handler) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"withDraw workflow");
        checkWithdrawArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.withdraw).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.withDraw(variable, handler);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public <T extends WithdrawHandler> Workflow withDraw(List<String> businessId, DefaultUser user, T handler) {
        WorkflowVariable variable = new WorkflowVariable()
                .setBusinessId(businessId)
                .setUser(user);
        return withDraw(variable,handler);
    }

    @Override
    public Workflow complete(WorkflowVariable variable) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"complete workflow");
        checkCompleteArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.complete).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.complete(variable);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    private void checkCompleteArgs(WorkflowVariable variable){
        Assert.notEmpty("completeWorkflow require parameters orderId user has null",variable);
        Assert.isTrue("completeWorkflow require parameters orderId user has null",
                ObjectHelper.isEmpty(variable.getOrderId()) || ObjectHelper.isEmpty(variable.getUser())
                      || ObjectHelper.isEmpty(variable.getUser().userId()));
    }

    @Override
    public Workflow complete(String orderId, DefaultUser user) {
        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(user);
        return complete(variable);
    }

    @Override
    public <T extends CompleteHandler> Workflow complete(WorkflowVariable variable, T handler) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"complete workflow");
        checkCompleteArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.withdraw).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.complete(variable, handler);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public <T extends CompleteHandler> Workflow complete(String orderId, DefaultUser user, T handler) {
        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(user);
        return complete(variable,handler);
    }

    @Override
    public Workflow subScribe(WorkflowVariable variable) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"subScribe workflow");
        checkCompleteArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.scribe).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.submit(variable);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public Workflow subScribe(String orderId, DefaultUser user) {
        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(user);
        return subScribe(variable);
    }

    @Override
    public <T extends SubmitHandler> Workflow subScribe(WorkflowVariable variable, T handler) {
        MsgContent.getInstance().setContent(ErrorContant.Operate,"subScribe workflow");
        checkCompleteArgs(variable);
        try {
            this.actuator.setDataMap(variable.getDataMap()).setOperate(WorkflowOperate.scribe).setVariable(variable);
            this.executor = new RoutingExecutor(actuator);
            return this.executor.submit(variable, handler);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public <T extends SubmitHandler> Workflow subScribe(String orderId, DefaultUser user, T handler) {
        WorkflowVariable variable = new WorkflowVariable()
                .setOrderId(orderId)
                .setUser(user);
        return subScribe(variable,handler);
    }


}
