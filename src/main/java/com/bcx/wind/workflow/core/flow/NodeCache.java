package com.bcx.wind.workflow.core.flow;

import com.bcx.wind.workflow.helper.ObjectHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhanglei
 */
public class NodeCache {

    private NodeCache(){}

    private  ThreadLocal<Map<String,NodeModel>> allNodes = new ThreadLocal<>();

    private static class NodeCacheInstance{
        private NodeCacheInstance(){}
        private static NodeCache cache = new NodeCache();
    }

    public static NodeCache getInstance(){
        return NodeCacheInstance.cache;
    }

    public void put(String nodeName , NodeModel nodeModel){
        if(ObjectHelper.isEmpty(allNodes.get())){
            Map<String,NodeModel> nodeModelMap = new HashMap<>();
            nodeModelMap.put(nodeName,nodeModel);
            allNodes.set(nodeModelMap);
        }

        allNodes.get().put(nodeName,nodeModel);
    }

    public void clear(){
        this.allNodes.remove();
    }

    public NodeModel get(String nodeName){
        if(ObjectHelper.isEmpty(allNodes.get())){
            return null;
        }
        return allNodes.get().get(nodeName);
    }

    public void remove(String nodeName){
        if(!ObjectHelper.isEmpty(allNodes.get())){
            allNodes.get().remove(nodeName);
        }
    }

    public boolean contain(String nodeName){
        return !ObjectHelper.isEmpty(allNodes.get()) && allNodes.get().containsKey(nodeName);
    }
}
