package com.bcx.wind.workflow.core.lock;

import java.util.HashMap;
import java.util.Map;

/**
 * 信息容器
 *
 * @author zhanglei
 */
public class MsgContent {

    private final ThreadLocal<Map<String,String>> content = new ThreadLocal<>();


    private MsgContent(){

    }

    private static class MsgContentInstance{
        private MsgContentInstance(){}
        private static MsgContent content = new MsgContent();
    }


    public static MsgContent getInstance(){
        return MsgContentInstance.content;
    }


    public void clear(){
        this.content.remove();
    }

    public Map<String,String>  getMap(){
        Map<String,String>  msg = content.get();
        if(msg==null){
            content.set(new HashMap<>());
        }
        return content.get();
    }


    public void setContent(String key, String value){
        getMap().put(key,value);
    }

    public String getValue(String key){
        return getMap().get(key);
    }
}
