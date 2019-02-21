package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.helper.ObjectHelper;

/**
 * 并且分支缓存，用于区分节点是否在分支中
 *
 * @author zhanglei
 */
public class AndCache {

    private ThreadLocal<Integer> inAnd = new ThreadLocal<>();

    private AndCache(){}

    private static class AndCacheInstance {
        private AndCacheInstance(){}
        private static AndCache andCache = new AndCache();
    }

    public static AndCache getInstance(){
        return AndCacheInstance.andCache;
    }

    public boolean inAnd(){
        Integer integer = this.inAnd.get();
        return !ObjectHelper.isEmpty(integer) && integer==1;
    }

    public  void  inAnd(Integer inAnd){
        this.inAnd.set(inAnd);
    }
}
