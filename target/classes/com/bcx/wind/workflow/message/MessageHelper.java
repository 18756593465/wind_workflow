package com.bcx.wind.workflow.message;

/**
 * 消息帮助
 *
 * @author zhanglei
 */
public class MessageHelper {

    /**
     * 获取消息
     * @param code       消息代码
     * @param variable   消息模板参数
     * @return           消息
     */
    public static String getMsg(String code,String...variable){
        String msg = WorkflowMessage.getInstance().getMsg(code);
        return replaceMsg(msg,variable);
    }


    private static String replaceMsg(String msg,String...variable){
        String message = msg;
        if(variable!=null && variable.length>0){
            for(int i=0 ; i<variable.length ; i++){
                String var = variable[i];
                message = message.replaceAll("\\$\\{"+i+"\\}",var);
            }
        }
        return message;
    }
}
