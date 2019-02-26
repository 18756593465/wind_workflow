package com.bcx.wind.workflow.core.flow;

public class Node {

    /**
     * 节点名称
     */
    protected String name;

    /**
     * 节点显示名称
     */
    protected String displayName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
