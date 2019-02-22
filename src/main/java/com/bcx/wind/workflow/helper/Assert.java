package com.bcx.wind.workflow.helper;

import com.bcx.wind.workflow.exception.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * Assert  class
 *
 * @author zhanglei
 */
public class Assert {

    private static final Logger logger = LoggerFactory.getLogger(Assert.class);

    /**
     * 断言不可为空
     *
     * @param msg    异常信息
     * @param object 不可为空的对象
     */
    public static void notEmpty(String msg , Object object){
        if(ObjectHelper.isEmpty(object)){
            if(logger.isDebugEnabled()){
                logger.debug(msg);
            }
            throw new WorkflowException(msg);
        }
    }


    /**
     * 断言存在空
     *
     * @param msg       异常信息
     * @param objects   不可为空对象数组
     */
    public static void hasEmpty(String msg,Object ... objects){
        notEmpty(msg,objects);
        for(Object object : objects){
            notEmpty(msg,object);
        }
    }

    public static void hasEmpty(String msg,List<Object> objects){
        notEmpty(msg,objects);
        for(Object object : objects){
            notEmpty(msg,object);
        }
    }

    /**
     * 断言全部为空
     *
     * @param msg       异常信息
     * @param objects   不可全部为空的对象数组
     */
    public static void allEmpty(String msg,Object ... objects){
        notEmpty(msg,objects);
        boolean empty = true;
        for(Object object : objects){
            if(!ObjectHelper.isEmpty(object)){
                empty = false;
                break;
            }
        }
        isTrue(msg,empty);
    }


    /**
     * 断言boolean类型不为true
     *
     * @param msg      异常信息
     * @param result   boolean 数据
     */
    public static void isTrue(String msg,boolean result){
        if(result){
            if(logger.isDebugEnabled()){
                logger.debug(msg);
            }
            throw new WorkflowException(msg);
        }
    }


    /**
     * 断言不可为空的值
     *
     * @param objects  不可为空的值数组
     */
    public static void nonEmpty(Object ...objects){
        hasEmpty("the  args is must not be empty!",objects);
    }


}
