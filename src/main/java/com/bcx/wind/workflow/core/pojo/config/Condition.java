package com.bcx.wind.workflow.core.pojo.config;


public class Condition {

    private String key;

    private Compare condition;

    private Object value;

    private String logic;

    public Condition(){}

    public Condition(String key,Compare condition,Object value){
        this.key = key;
        this.condition = condition;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Compare getCondition() {
        return condition;
    }

    public void setCondition(Compare condition) {
        this.condition = condition;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }
}
