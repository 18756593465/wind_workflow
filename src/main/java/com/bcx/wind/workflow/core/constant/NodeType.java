package com.bcx.wind.workflow.core.constant;

/**
 * 节点类型
 */
public enum  NodeType {

    START("start"),
    END("end"),
    TASK("task"),
    DISC("disc"),
    AND("and"),
    OR("or"),
    AND_JOIN("andJoin"),
    OR_JOIN("orJoin"),
    ROUTER("router"),
    PROCESS("process"),
    SCRIBE("scribe"),
    SCRIBE_TASK("scribeTask");

    private String value;

    private NodeType(String value){
        this.value = value;
    }

    public String  value(){
        return this.value;
    }

    public boolean exist(String value){
        return contain(value) != null;
    }

    public NodeType contain(String value){
        NodeType[] nodeTypes = NodeType.values();
        for(NodeType type : nodeTypes){
            if(type.value.equals(value)){
                return type;
            }
        }
        return null;
    }
}
