package com.bcx.wind.workflow.core.lock;

import java.util.LinkedList;
import java.util.List;

/**
 * 被锁的对象
 *
 * @author zhanglei
 */
public class Content {

    //被锁的数据
    private List<String> objects = new LinkedList<>();

    //5秒钟 后删除
    private long timeOut = 5000;

    //创建时间
    private long createTime;

    public Content(List<String> objects){
        this.createTime = System.currentTimeMillis();
        this.objects = objects;
    }

    public List<String> getObjects() {
        return objects;
    }

    public Content setObjects(List<String> objects) {
        this.objects = objects;
        return this;
    }

    public long getCreateTime() {
        return createTime;
    }

    public Content setCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }



    public long getTimeOut() {
        return timeOut;
    }

    public Content setTimeOut(long timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Content){
            List<String> objectList = ((Content) obj).getObjects();
            for(Object o : this.objects){
                for(Object o1 : objectList){
                    if(o1.equals(o)){
                        return true;
                    }
                }
            }
        }
        return super.equals(obj);
    }

}
