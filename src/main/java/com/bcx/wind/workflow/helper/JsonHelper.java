package com.bcx.wind.workflow.helper;

import com.bcx.wind.workflow.exception.WorkflowException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * json数据解析工具类
 *
 * @author zhanglei
 */
public class JsonHelper {

    private static Logger logger = LoggerFactory.getLogger(JsonHelper.class);


    /**
     * jackson 解析对象
     */
    private static class ObjectMapperBuilder{
        private ObjectMapperBuilder(){}
        private static ObjectMapper objectMapper = new ObjectMapper();
    }

    private static ObjectMapper objectMapper(){
        return ObjectMapperBuilder.objectMapper;
    }


    /**
     * analysis json to map
     *
     *
     * @param json   json
     * @return       map
     */
    @SuppressWarnings("unchecked")
    public  static Map<String,Object>  jsonToMap(String json){
        return parseJson(json,Map.class,String.class,Object.class);
    }


    /**
     * analysis json to object
     *
     * @param json    json字符串
     * @param clazz   转型类
     * @param <T>     泛型
     * @return        T
     */
    public static <T>T  jsonToObject(String json,Class<T> clazz){
        return parseJson(json,clazz);
    }


    /**
     * 将map转成对象
     *
     * @param args   map
     * @param clazz  类型
     * @return       T
     */
    public static <T>T  mapToObject(Map<String,Object> args,Class<T> clazz){
        return coverObject(args,clazz);
    }


    /**
     * 将对象转成map
     *
     * @param object  对象
     * @return        map
     */
    @SuppressWarnings("unchecked")
    public static Map<String,Object>  objectToMap(Object object){
        return coverObject(object,Map.class,String.class,Object.class);
    }


    /**
     * analysis object to object
     *
     * @param object   需要解析的对象
     * @param clazz    解析成的类型
     * @param classes  泛型
     * @param <T>      返回类型
     * @return         T
     */
    public static <T>T  coverObject(Object object,Class<T> clazz,Class... classes){
        JavaType javaType = objectMapper().getTypeFactory().constructParametricType(clazz,classes);
        return objectMapper().convertValue(object,javaType);
    }


    /**
     * analysis  json string to class
     *
     * @param json      json String
     * @param clazz     类
     * @param classes   泛型
     * @param <T>       返回值类型
     * @return          解析后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T>T  parseJson(String json,Class<T> clazz,Class...classes){
        try {
            JavaType javaType = objectMapper().getTypeFactory().constructParametricType(clazz, classes);
            return objectMapper().readValue(json,javaType);
        } catch (IOException e) {
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e.getMessage());
        }
    }



    /**
     * 将对象转成json字符串
     *
     * @param object  对象
     * @return        json字符串
     */
    public static String  toJson(Object object){
        try {
            return objectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage(),e);
            throw new WorkflowException(e.getMessage());
        }
    }



    /**
     * 下划线转成驼峰
     */
    public static String toHump(String field){
        Pattern linePattern = Pattern.compile("_(\\w)");
        String str = field.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer builder = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(builder,matcher.group(1).toUpperCase());
        }
        matcher.appendTail(builder);
        return builder.toString();
    }



    /**
     * 驼峰转成下划线
     */
    public static String toUnderLineField(String field){
        StringBuilder result = new StringBuilder();
        char [] str = field.toCharArray();
        for(char c : str){
            if(Character.isUpperCase(c)){
                result.append("_"+String.valueOf(c).toLowerCase());
                continue;
            }
            result.append(String.valueOf(c));
        }
        return result.toString();
    }




}
