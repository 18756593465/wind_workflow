package com.bcx.wind.workflow.io;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

public class ResourcesTest {


    @Test
    public void getResourceURL() throws IOException {

        URL url = Resources.getResourceURL("process/Demo.xml");
        System.out.println(url);
    }

    @Test
    public void getResourceURLForClassLoader() throws IOException {
        URL url = Resources.getResourceURL(ResourcesTest.class.getClassLoader(),"process/Demo.xml");
        System.out.println(url);
    }


    @Test
    public void getResourceAsStream() throws IOException {
        InputStream stream = Resources.getResourceAsStream("process/Demo.xml");
        System.out.println(stream);
    }

    @Test
    public void getResourceAsStreamForLoader() throws IOException {
        InputStream stream = Resources.getResourceAsStream(ResourcesTest.class.getClassLoader(),"process/Demo.xml");
        System.out.println(stream);
    }

    @Test
    public void getResourceAsProperties() throws IOException {
        Properties properties = Resources.getResourceAsProperties("wind/jdbc.properties");
        System.out.println(properties.size());
    }

    @Test
    public void getResourceAsPropertiesForLoaderthrows()throws IOException {
        Properties properties = Resources.getResourceAsProperties(ResourcesTest.class.getClassLoader(),"wind/jdbc.properties");
        System.out.println(properties.size());
    }

    @Test
    public void getResourceAsReader() throws IOException {
        Reader reader = Resources.getResourceAsReader("process/Demo.xml");
        System.out.println(reader);
    }

    @Test
    public void getResourceAsReaderForLoader() throws IOException {
        Reader reader = Resources.getResourceAsReader(ResourcesTest.class.getClassLoader(),"process/Demo.xml");
        System.out.println(reader);
    }

    @Test
    public void getResourceAsFile() throws IOException {
        File file = Resources.getResourceAsFile("process/Demo.xml");
        System.out.println(file);
    }


    @Test
    public void getResourceAsFileForLoader() throws IOException {
        File file = Resources.getResourceAsFile(ResourcesTest.class.getClassLoader(),"process/Demo.xml");
        System.out.println(file);
    }


}
