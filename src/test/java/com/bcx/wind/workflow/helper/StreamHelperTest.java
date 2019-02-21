package com.bcx.wind.workflow.helper;

import com.bcx.wind.workflow.io.Resources;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class StreamHelperTest {

    @Test
    public void getByte() throws IOException {
        InputStream stream = Resources.getResourceAsStream("process/Demo.xml");

        byte[] bytes = StreamHelper.getByte(stream);
        System.out.println();
    }

    @Test
    public void getStringByByte() throws IOException {
        InputStream stream = Resources.getResourceAsStream("process/Demo.xml");

        byte[] bytes = StreamHelper.getByte(stream);
        String content = StreamHelper.getStringByByte(bytes);
        System.out.println(content);

    }
}
