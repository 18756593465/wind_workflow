package com.bcx.wind.workflow.core.pojo.user;

public class UserObject {

    /**
     * 用户属性
     */
    private String key;


    /**
     * 属性值
     */
    private  UserField field;


    public String getKey() {
        return key;
    }

    public UserObject setKey(String key) {
        this.key = key;
        return this;
    }

    public UserField getField() {
        return field;
    }

    public UserObject setField(UserField field) {
        this.field = field;
        return this;
    }
}
