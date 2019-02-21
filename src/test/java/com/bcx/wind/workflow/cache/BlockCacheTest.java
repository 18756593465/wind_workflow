package com.bcx.wind.workflow.cache;

import org.junit.Test;

public class BlockCacheTest  {

    @Test
    public void blockCacheTest(){

        Cache<String,Object> cache = new BlockingCache<>();

        cache.put("123","abc");
        Object a = cache.get("123");
        System.out.println();

        cache.isEmpty();
        cache.size();
        cache.remove("123");
        cache.clear();
    }
}
