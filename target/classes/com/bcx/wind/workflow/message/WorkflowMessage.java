package com.bcx.wind.workflow.message;

import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 国际化
 *
 * @author zhanglei
 */
public class WorkflowMessage {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowMessage.class);


    private WorkflowMessage(){
        setLang("zh");
    }

    /**
     * 消息模板地址
     */
    private String local = "com/bcx/wind/workflow/message/msg";

    //语言  默认中文 使用.properties文件
    private String lang = "zh";

    private static WorkflowMessage message = new WorkflowMessage();

    private Properties msg = new Properties();

    public static WorkflowMessage getInstance(){
        return message;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
        InputStream stream = null;
        try {
            stream  =  Resources.getResourceAsStream(this.local+"/"+lang+".properties");
            msg.clear();
            msg.load(new InputStreamReader(stream,"UTF-8"));
        } catch (Exception e) {
            if(logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
            throw new WorkflowException("workflow message properties read error !");
        }finally {
            if(stream!=null){
                try {
                    stream.close();
                } catch (IOException e) {
                    if(logger.isDebugEnabled()){
                        logger.debug(e.getMessage(),e);
                    }
                }
            }
        }
    }

    public String getMsg(String code){
        Object object = this.msg.get(code);
        if(object==null){
            return null;
        }
        return object.toString();
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }


}
