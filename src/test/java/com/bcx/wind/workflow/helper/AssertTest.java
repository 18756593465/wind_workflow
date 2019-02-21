package com.bcx.wind.workflow.helper;

import org.junit.Test;

import java.util.Collections;

public class AssertTest {

    @Test
    public void assertTest(){
        Assert.notEmpty("测试不能为空","abc");

        Assert.hasEmpty("测试不能有空值","abc","123");

        Assert.nonEmpty("abc","123");

        Assert.hasEmpty("测试集合中不能有空值",Collections.singletonList("abc"));

        Assert.isTrue("测试结果不可为真",1!=1);

        Assert.allEmpty("而是不可全部为空","abc","123");
    }
}
