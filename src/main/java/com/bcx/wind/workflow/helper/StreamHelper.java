package com.bcx.wind.workflow.helper;

import com.bcx.wind.workflow.exception.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * @author zhanglei
 */
public class StreamHelper {

    private static final Logger logger = LoggerFactory.getLogger(StreamHelper.class);


    public static byte[]  getByte(InputStream inputStream){
        try {
            ByteArrayOutputStream write = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int num = inputStream.read(buffer);
            while (num != -1) {
                write.write(buffer, 0, num);
                num = inputStream.read(buffer);
            }
            write.flush();
            return write.toByteArray();
        }catch (Exception e){
            if(logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
            throw new WorkflowException(e.getMessage());
        }  finally{
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.debug(e.getMessage());
                }
            }
        }

    }


    public static String  getStringByByte(byte[] bytes){
        try {
            return new String(bytes,"utf-8");
        } catch (UnsupportedEncodingException e) {
            if(logger.isDebugEnabled()){
                logger.debug(e.getMessage(),e);
            }
            throw new WorkflowException("byte to String error!");
        }
    }

}
