package com.bcx.wind.workflow.core.constant;

public enum  WorkflowOperate {

    submit("提交"),
    reject("驳回"),
    build("创建"),
    scribe("订阅"),
    complete("完结"),
    withdraw("撤回"),
    transfer("转办");

    private String value;

    private WorkflowOperate(String value){
        this.value = value;
    }

    public  String value(){
        return this.value;
    }

    public static void main(String[] args) {
        System.out.println();
    }
}
