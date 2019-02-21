package com.bcx.wind.workflow.helper;

import com.bcx.wind.workflow.core.flow.TaskModel;
import com.bcx.wind.workflow.core.flow.TaskNode;
import com.bcx.wind.workflow.core.pojo.Configuration;
import com.bcx.wind.workflow.core.pojo.config.Condition;
import com.bcx.wind.workflow.exception.WorkflowException;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.bcx.wind.workflow.core.constant.Constant.AND;
import static com.bcx.wind.workflow.core.constant.Constant.OR;

/**
 * object helper class
 */
public class ObjectHelper {


    private static final Pattern NUMBER = Pattern.compile("^(-)?\\d+(\\.\\d+)?$");


    private ObjectHelper(){}

    public static String getValue(String key,Map args){
        return isEmpty(args.get(key)) ? "" : args.get(key).toString();
    }


    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj instanceof Optional) {
            return !((Optional)obj).isPresent();
        } else if (obj instanceof CharSequence) {
            return ((CharSequence)obj).length() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            return ((Collection)obj).isEmpty();
        } else {
            return obj instanceof Map ? ((Map)obj).isEmpty() : false;
        }
    }

    public static boolean hasEmpty(Object...objects){
        if(isEmpty(objects)){
            return true;
        }
        for(Object obj : objects){
            if(isEmpty(obj)){
                return true;
            }
        }
        return false;
    }


    public static boolean allEmpty(Object...objects){
        if(isEmpty(objects)){
            return true;
        }

        for(Object obj : objects){
            if(!isEmpty(obj)){
                return false;
            }
        }

        return true;
    }


    public static String primaryKey(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    public static Object getValue(Map<String,Object> args,String key){
        if(isEmpty(args)){
            return "";
        }

        return args.get(key);
    }


    /**
     * 判断是否是数字，支持负数，小数点等,所以不能完全判断是否纯数字
     *
     * @param str 判断字符串
     * @return boolean
     */
    private static boolean isNumeric(String str) {
        return NUMBER.matcher(str).matches();
    }


    /**
     * 通过条件校验 和  业务数据进行公式比较  返回boolean
     *
     * @param con           配置的匹配规则
     * @param dataMap       业务数据
     * @return               布尔  公式成立true 不成立false
     */
    public static boolean checkValue(Condition con, Map<String,Object> dataMap){
        //条件逻辑运算符  并且或者
        String  logic  = con.getLogic();
        logic = ObjectHelper.isEmpty(logic) ? OR : logic;
        //条件校验中的键，对应业务数据中的属性
        String key = con.getKey();
        //比较运算符
        String condition = con.getCondition().value();
        //条件校验值
        Object value = con.getValue();
        //业务数据值
        String dataValue = getDataValue(dataMap,key);
        //如果配置校验数据为数组
        if(value instanceof List){
            List<String> compareValues = (List)value;
            return checkListValues(compareValues,dataValue,condition,dataMap,key,logic);
        }else {
            String compareStrValue = value.toString();
            String[] compareStrValues = compareStrValue.split(",");
            if(compareStrValues.length==1) {
                return  checkValue(dataValue, condition, compareStrValue);
            }
            return checkListValues(Arrays.asList(compareStrValues),dataValue,condition,dataMap,key,OR);
        }
    }


    private static  boolean  checkListValues(List<String> compareValues,String dataValue,String condition,Map<String,Object> dataMap,String key,String logic){
        return  checkValue(compareValues,dataValue,condition,logic);
    }


    /**
     * 校验数组元素包含
     * @param compareValues 配置数据数组
     * @param dataValue 业务数据
     * @param condition  比较符号
     * @param logic       并且  或者
     * @return boolean
     */
    private static boolean checkValue(List<String> compareValues,String dataValue,String condition,String logic){
        boolean result = false;
        for(String value : compareValues){
            result = checkValue(dataValue,condition,value);
            //或者运算符使用
            if(result && OR.equals(logic)) {
                return true;
            }else if(!result && AND.equals(logic)){
                return false;
            }
        }
        return result;
    }



    private static String getDataValue(Map<String,Object> data,String key){
        String dataValue = getValue(key,data);
        if(ObjectHelper.isEmpty(dataValue)){
            dataValue =  ObjectHelper.getValue(JsonHelper.toHump(key),data);
            if(ObjectHelper.isEmpty(dataValue)){
                dataValue = ObjectHelper.getValue(JsonHelper.toUnderLineField(key),data);
                if(ObjectHelper.isEmpty(dataValue)){
                    dataValue = "";
                }
            }
        }
        return dataValue;
    }



    /**
     * 比较
     *
     * @param conditionValue   比较值
     * @param condition         自定义比较运算符
     * @param dataValue         被比较值
     * @return                   boolean
     */
    private static boolean checkValue(String conditionValue,String condition,String dataValue){
        if(ObjectHelper.hasEmpty(condition,conditionValue,dataValue)){
            return false;
        }
        BigDecimal v1 = new BigDecimal(0);
        BigDecimal v2 = new BigDecimal(0);
        Pattern pat;
        Matcher mat;
        if(isNumeric(conditionValue) || isNumeric(dataValue)) {
            v1 = new BigDecimal(conditionValue);
            v2 = new BigDecimal(dataValue);
        }

        switch (condition){
            //字符串等于
            case "eq":
                return conditionValue.equals(dataValue);
            //字符串 !=
            case "ueq":
                return !conditionValue.equals(dataValue);
            //like
            case "lk":
                return conditionValue.contains(dataValue);
            //not like
            case "ulk":
                return !conditionValue.contains(dataValue);
            //被包含
            case "in":
                return dataValue.contains(conditionValue);
            //不被包含
            case "uin":
                return !dataValue.contains(conditionValue);
            //conditionValue start with val3
            case "st":
                return conditionValue.startsWith(dataValue);
            //conditionValue end with val3
            case "ed":
                return conditionValue.endsWith(dataValue);
            //val3 start with conditionValue
            case "if":
                return dataValue.startsWith(conditionValue);
            //val3 end with conditionValue
            case "ie":
                return dataValue.endsWith(conditionValue);
            //正则表达式 match
            case "rg":
                pat = Pattern.compile(dataValue);
                mat = pat.matcher(conditionValue);
                return mat.matches();
            //dataValue 是主题match
            case "irg":
                pat = Pattern.compile(conditionValue);
                mat = pat.matcher(dataValue);
                return mat.matches();
            //数字
            //=
            case "neq":
                return v1.compareTo(v2) == 0;
            //!=
            case "nueq":
                return v1.compareTo(v2) != 0;
            // <
            case "nlt":
                return v1.compareTo(v2) < 0;
            // <=
            case "nelt":
                return v1.compareTo(v2) < 1;
            // >
            case "ngt":
                return v1.compareTo(v2) > 0;
            // >=
            case "negt":
                return v1.compareTo(v2) > -1;
            default:
                return false;
        }

    }


    /**
     * 获取节点配置数据
     * @param configuration   模型所有配置
     * @param taskNode        指定任务节点
     * @return                任务节点配置数据
     */
    public static Configuration getTaskConfig(Configuration configuration, TaskModel taskNode, Map<String,Object> dataMap){
        if(!ObjectHelper.isEmpty(configuration)){

            List<Configuration> configList = configuration.getConfigurationList();
            List<Configuration> configs = configList.stream().filter(config-> taskNode.name().equals(config.getNodeId()))
                    .collect(Collectors.toList());

            if(!ObjectHelper.isEmpty(configs)){
                configs = configs.stream().filter(config->{
                    String logic = ObjectHelper.isEmpty(config.getLogic()) ? AND : config.getLogic();
                    List<Condition> conditions = config.getConditionConfig();
                    boolean result = true;
                    for(Condition condition : conditions){
                        result  =  ObjectHelper.checkValue(condition,dataMap);
                        if(!result && logic.equals(AND)){
                            return false;
                        }else if(result && logic.equals(OR)){
                            return true;
                        }
                    }
                    return result;
                }).collect(Collectors.toList());

                if(!ObjectHelper.isEmpty(configs)){
                    return  configs.stream().max(Comparator.comparingInt(Configuration::getSort))
                            .orElseThrow(()->new WorkflowException("processConfig property sort is null!"));
                }
            }
        }
        return null;
    }

}
