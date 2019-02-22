package com.bcx.wind.workflow.parser;


import com.bcx.wind.workflow.core.flow.ProcessModel;
import com.bcx.wind.workflow.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;


public class DemoParserTest {


    @Test
    public void test(){

        try {
            InputStream stream = Resources.getResourceAsStream("process/Demo.xml");

            ProcessModel model = ProcessBuilder.getInstance().setInputStream(stream).build("12345");

            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    @Test
    public void contextTest(){
        try {
            InputStream stream = Resources.getResourceAsStream("process/Context.xml");

            ProcessModel model = ProcessBuilder.getInstance().setInputStream(stream).build("12345");
            model.getAllTaskNodes();

            model.getLastTasks("boss");

            boolean isNext = model.isNextTask("edit","monitor");
            model.isNextTask("edit","approveB");

            System.out.println();
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

}
