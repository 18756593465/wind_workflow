package com.bcx.wind.workflow.helper;

import com.bcx.wind.workflow.io.Resources;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;

public class XmlHelperTest {

    @Test
    public void buildXmlStr() throws IOException {
        InputStream stream = Resources.getResourceAsStream("process/Demo.xml");

        byte[] bytes = StreamHelper.getByte(stream);
        String content = StreamHelper.getStringByByte(bytes);

        Document document = XmlHelper.buildDoc(content);

        assert document!=null;
    }


    @Test
    public void buildXmlStream() throws IOException {
        InputStream stream = Resources.getResourceAsStream("process/Demo.xml");

        Document document = XmlHelper.buildDoc(stream);

        assert document!=null;
    }
}
