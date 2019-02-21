package com.bcx.wind.workflow.exception;

/**
 * 工作流异常
 *
 * @author zhanglei
 */
public class WorkflowException extends RuntimeException {

    private String msg;

    private int code;

    public WorkflowException(String msg){
        super(msg);
        this.msg = msg;
        this.code = -1;
        this.printStackTrace();
    }

    public WorkflowException(String msg,int code){
        this(msg);
        this.code = code;
        this.printStackTrace();
    }

    public WorkflowException(Throwable e){
        super(e);
        this.msg = e.getMessage();
        this.code = -1;
        this.printStackTrace();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
