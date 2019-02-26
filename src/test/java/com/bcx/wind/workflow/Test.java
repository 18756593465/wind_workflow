package com.bcx.wind.workflow;

public class Test {

    private void add(){

        try{
            System.out.println(1/0);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("anc");
        }

    }


    private void update(){
        try{
            add();
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("方法：update");
            throw new RuntimeException("abc",e);
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        try {
            test.update();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
