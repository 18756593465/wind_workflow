package com.bcx.wind.workflow.helper;

import com.bcx.wind.workflow.core.pojo.DefaultUser;
import com.bcx.wind.workflow.core.pojo.User;
import com.bcx.wind.workflow.core.pojo.Workflow;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JsonHelperTest {

    private String content = "{\"123\":\"abc\",\"234\":\"bcd\"}";


    private String json = "{\"userId\":\"10001\",\"userName\":\"张三\"}";

    @Test
    public void jsonToMap(){
        Map<String,Object> map  = JsonHelper.jsonToMap(content);
        System.out.println(map);
    }

    @Test
    public void jsonToObject(){
        DefaultUser user = JsonHelper.jsonToObject(json,DefaultUser.class);
        System.out.println(user);
    }


    @Test
    public void mapToObject(){
        Map<String,Object> args = new HashMap<>();
        args.put("userId","10001");
        args.put("userName","张三");

        DefaultUser user = JsonHelper.mapToObject(args,DefaultUser.class);
        System.out.println(user);
    }

    @Test
    public void objectToMap(){
        DefaultUser user = new DefaultUser();
        user.setUserId("10001");
        user.setUserName("张三");

        Map<String,Object> args = JsonHelper.objectToMap(user);
        System.out.println();
    }

    @Test
    public void coverObject(){
        Map<String,Object> args = new HashMap<>();
        args.put("system","dms");
        args.put("orderId","121324");
        args.put("businessId",Collections.singletonList("123"));
        DUser user = new DUser();
        user.setUserId("10001");
        user.setUserName("张三");
        user.setAge(12);
        user.setLocal("北京");
        args.put("user",user);

        Workflow workflow = JsonHelper.coverObject(args,Workflow.class);
        System.out.println();
    }

    @Test
    public void parseJson(){

        String content = "{\"processDefinition\":null,\"system\":\"dms\",\"orderId\":\"121324\",\"businessId\":[\"123\"],\"processConfig\":null,\"curTask\":[],\"user\":{\"userId\":\"10001\",\"userName\":\"张三\",\"nickName\":null,\"status\":null,\"group\":null,\"idCard\":null,\"mobilePhone\":null,\"officePhone\":null,\"email\":null,\"gender\":null,\"job\":null,\"hireDate\":null,\"age\":\"12\",\"height\":null,\"qq\":null,\"boss\":null,\"company\":null,\"school\":null,\"local\":\"北京\",\"homeTown\":null,\"birthday\":null,\"remark\":null,\"description\":null,\"weight\":null},\"approveUsers\":[],\"variable\":null,\"orderInstance\":null,\"childOrderInstance\":[],\"engine\":null}";
        Workflow workflow = JsonHelper.parseJson(content,Workflow.class);
        System.out.println();
    }


    @Test
    public void toJson(){
        Map<String,Object> args = new HashMap<>();
        args.put("system","dms");
        args.put("orderId","121324");
        args.put("businessId",Collections.singletonList("123"));
        DUser user = new DUser();
        user.setUserId("10001");
        user.setUserName("张三");
        user.setAge(12);
        user.setLocal("北京");
        args.put("user",user);
        String json = JsonHelper.toJson(args);
        System.out.println(json);
    }

    @Test
    public void toHump(){
        String hump = JsonHelper.toHump("user_name");
        System.out.println(hump);
    }

    @Test
    public void toUnderLineField(){
        String underLine = JsonHelper.toUnderLineField("userName");
        System.out.println(underLine);
    }


    private static class DUser implements User{
        private String userId;

        private String userName;

        private int age;

        private String local;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        @Override
        public String userId() {
            return this.userId;
        }

        @Override
        public String userName() {
            return this.userName;
        }
    }



}
