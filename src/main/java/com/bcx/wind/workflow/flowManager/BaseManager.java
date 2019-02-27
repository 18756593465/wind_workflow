package com.bcx.wind.workflow.flowManager;

import com.bcx.wind.workflow.WorkflowEngine;
import com.bcx.wind.workflow.core.pojo.ManagerData;
import com.bcx.wind.workflow.core.pojo.TransferResult;

public abstract class BaseManager implements Manager{

    protected ManagerData managerData;


    public BaseManager(ManagerData managerData){
        this.managerData = managerData;
    }

    @Override
    public TransferResult transfer() {
        return null;
    }

    protected WorkflowEngine engine(){
        return this.managerData.getEngine();
    }


}
