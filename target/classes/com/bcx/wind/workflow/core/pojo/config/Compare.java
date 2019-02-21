package com.bcx.wind.workflow.core.pojo.config;

public enum  Compare{
    //等于
    EQ("eq"),
    //不等于
    UEQ("ueq"),
    //相似
    LIKE("like"),
    //不相似
    ULIKE("ulike"),
    //包含
    IN("in"),
    //不包含
    UIN("uin"),
    //数字等于
    NUMBER_EQ("number_eq"),
    //数字不等于
    NUMBER_UEQ("number_ueq"),
    //数组小于
    NUMBER_LT("number_lt"),
    //数字小于等于
    NUMBER_LT_EQ("number_lt_eq"),
    //数字大于
    NUMBER_GT("number_gt"),
    //数字大于等于
    NUMBER_GT_EQ("number_gt_eq");

    private String value;

    Compare(String value){
        this.value = value;
    }

    public String  value(){
        return value;
    }

}