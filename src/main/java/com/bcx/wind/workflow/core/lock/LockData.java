package com.bcx.wind.workflow.core.lock;

import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.message.MessageHelper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.bcx.wind.workflow.message.MsgConstant.w010;

/**
 * 指定时间内，锁定某个参数
 */
public class LockData {

    //被锁定的数据集合
    private List<Content>  data = new LinkedList<>();


    public boolean execute(Content content){
        long now = System.currentTimeMillis();
        Iterator<Content> datas = this.data.iterator();
        while(datas.hasNext()){
            Content con = datas.next();

            long createTime = con.getCreateTime();
            if(now-createTime > con.getTimeOut()){
                datas.remove();
            }
        }

        synchronized (data){
            if(data.contains(content)){
                throw new WorkflowException(MessageHelper.getMsg(w010));
            }
            data.add(content);
        }

        return true;
    }


}
