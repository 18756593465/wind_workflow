package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.helper.ObjectHelper;

/**
 * 或者分支缓存，用于区分任务是否在或者分支中
 *
 * @author zhanglei
 */
public class OrCache {

    private ThreadLocal<Integer>  inOr = new ThreadLocal<>();

    private OrCache(){}


    private static class OrCacheInstance{
        private OrCacheInstance(){}
        private static OrCache orCache = new OrCache();
    }

    public static OrCache getInstance(){
        return OrCacheInstance.orCache;
    }

    public boolean inOr(){
        Integer integer =  this.inOr.get();
        return !ObjectHelper.isEmpty(integer) && integer==1;
    }

    public void  inOr(int inOr){
        this.inOr.set(inOr);
    }
}
