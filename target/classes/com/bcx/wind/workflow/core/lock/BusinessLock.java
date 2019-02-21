package com.bcx.wind.workflow.core.lock;

/**
 * 业务主键锁定
 *
 * @author zhanglei
 */
public class BusinessLock {

    private BusinessLock(){

    }

    private static LockData lockData = new LockData();

    public static void execute(Content content){
        lockData.execute(content);
    }
}
