package com.bcx.wind.workflow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanContext implements Context {

    private final Map<String,Object>  map = new ConcurrentHashMap<>();


    @Override
    public void put(String key, Object value) {
        map.put(key,value);
    }

    @Override
    public void put(String key, Class value) {
        map.put(key,value);
    }

    @Override
    public boolean exist(String key) {
        return map.containsKey(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(Class<T> clazz) {
        for(Map.Entry<String,Object> obj : this.map.entrySet()){
            Object object = obj.getValue();
            if(clazz.isInstance(object)){
                return (T) object;
            }
        }
        return null;
    }

    @Override
    public Object getValue(String key) {
         return map.get(key);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Object remove(String key) {
        return this.map.remove(key);
    }


}
