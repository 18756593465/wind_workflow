package com.bcx.wind.workflow;

/**
 * access
 */
public abstract class AccessService {

    /**
     * access
     */
    protected Access access;

    public AccessService(Access access){
        this.access = access;
    }

    protected WorkflowEngine engine(){
        return access.engine();
    }

    public Access getAccess(){
        return this.access;
    }

}
