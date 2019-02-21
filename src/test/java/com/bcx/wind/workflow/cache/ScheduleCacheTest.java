package com.bcx.wind.workflow.cache;

import org.junit.Test;

public class ScheduleCacheTest {

    @Test
    public void scheduleTest(){
        Cache<String,String> cache = new ScheduleClearCache<>();


        cache.put("123","abc");
        Object a = cache.get("123");
        System.out.println();

        cache.isEmpty();
        cache.size();
        cache.remove("123");
        cache.clear();
    }
}
