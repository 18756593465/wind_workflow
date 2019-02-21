package com.bcx.wind.workflow.core.flow;


/**
 * 路线
 */
public class Path extends Node {

    //路线后
    private String to;

    //表达式
    private String expr;


    //后续节点
    private NodeModel nextNode;


    //后续任务节点
    private TaskModel nextTaskNode ;

    //上一个节点
    private NodeModel preNode;


    public Path(String name,String displayName){
        super();
        this.name = name;
        this.displayName = displayName;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public NodeModel getNextNode() {
        return nextNode;
    }

    public void setNextNode(NodeModel nextNode) {
        this.nextNode = nextNode;
    }

    public TaskModel getNextTaskNode() {
        return nextTaskNode;
    }

    public void setNextTaskNode(TaskModel nextTaskNode) {
        this.nextTaskNode = nextTaskNode;
    }

    public String getTo() {
        return to;
    }


    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public NodeModel getPreNode() {
        return preNode;
    }

    public void setPreNode(NodeModel preNode) {
        this.preNode = preNode;
    }
}
