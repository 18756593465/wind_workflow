package com.bcx.wind.workflow.impl;

import com.bcx.wind.workflow.core.WorkflowService;
import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.ManagerData;
import com.bcx.wind.workflow.core.pojo.TransferResult;
import com.bcx.wind.workflow.core.pojo.TransferVariable;
import com.bcx.wind.workflow.flowManager.Manager;
import com.bcx.wind.workflow.flowManager.TransferManager;
import com.bcx.wind.workflow.helper.Assert;
import com.bcx.wind.workflow.helper.ObjectHelper;
import com.bcx.wind.workflow.message.MessageHelper;

import java.util.List;

import static com.bcx.wind.workflow.message.MsgConstant.w028;

public class WorkflowServiceImpl implements WorkflowService {

    private Manager manager;

    private ManagerData managerData;


    public WorkflowServiceImpl(ManagerData managerData){
        this.managerData = managerData;
    }

    public Manager getManager() {
        return manager;
    }

    public WorkflowServiceImpl setManager(Manager manager) {
        this.manager = manager;
        return this;
    }

    public ManagerData getManagerData() {
        return managerData;
    }

    public WorkflowServiceImpl setManagerData(ManagerData managerData) {
        this.managerData = managerData;
        return this;
    }

    @Override
    public TransferResult transfer(DefaultUser oldUser, DefaultUser newUser) {
        return transfer(new TransferVariable().setOldUser(oldUser).setNewUser(newUser));
    }

    @Override
    public TransferResult transfer(String processId, DefaultUser oldUser, DefaultUser newUser) {
        return transfer(new TransferVariable().setOldUser(oldUser).setNewUser(newUser).setProcessId(processId));
    }

    @Override
    public TransferResult transfer(List<String> businessIds, DefaultUser oldUser, DefaultUser newUser) {
        return transfer(new TransferVariable().setOldUser(oldUser).setNewUser(newUser).setBusinessIds(businessIds));
    }

    @Override
    public TransferResult transfer(TransferVariable variable) {
        checkTransferVariable(variable);
        this.manager = new TransferManager(this.managerData).setVariable(variable);
        return this.manager.transfer();
    }

    private void checkTransferVariable(TransferVariable variable){
        DefaultUser oldUser = variable.getOldUser();
        DefaultUser newUser = variable.getNewUser();
        Assert.isTrue(MessageHelper.getMsg(w028),ObjectHelper.isEmpty(oldUser)
                || ObjectHelper.isEmpty(oldUser.getUserId()));
        Assert.notEmpty(MessageHelper.getMsg(w028),ObjectHelper.isEmpty(newUser)
                || ObjectHelper.isEmpty(newUser.getUserId()));
        Assert.isTrue(MessageHelper.getMsg(w028),oldUser.getUserId().equals(oldUser.getUserName()));
    }
}
