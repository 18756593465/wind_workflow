/**
 *    Copyright 2009-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.bcx.wind.workflow.io;

import java.io.InputStream;
import java.net.URL;


public class ClassLoaderWrapper {

  ClassLoader defaultClassLoader;
  ClassLoader systemClassLoader;

  ClassLoaderWrapper() {
    try {
      systemClassLoader = ClassLoader.getSystemClassLoader();
    } catch (SecurityException ignored) {

    }
  }
  

  public URL getResourceAsURL(String resource) {
    return getResourceAsURL(resource, getClassLoaders(null));
  }


  public URL getResourceAsURL(String resource, ClassLoader classLoader) {
    return getResourceAsURL(resource, getClassLoaders(classLoader));
  }


  public InputStream getResourceAsStream(String resource) {
    return getResourceAsStream(resource, getClassLoaders(null));
  }


  public InputStream getResourceAsStream(String resource, ClassLoader classLoader) {
    return getResourceAsStream(resource, getClassLoaders(classLoader));
  }


  public Class<?> classForName(String name) throws ClassNotFoundException {
    return classForName(name, getClassLoaders(null));
  }


  public Class<?> classForName(String name, ClassLoader classLoader) throws ClassNotFoundException {
    return classForName(name, getClassLoaders(classLoader));
  }

  InputStream getResourceAsStream(String resource, ClassLoader[] classLoader) {
    for (ClassLoader cl : classLoader) {
      if (null != cl) {


        InputStream returnValue = cl.getResourceAsStream(resource);


        if (null == returnValue) {
          returnValue = cl.getResourceAsStream("/" + resource);
        }

        if (null != returnValue) {
          return returnValue;
        }
      }
    }
    return null;
  }


  URL getResourceAsURL(String resource, ClassLoader[] classLoader) {

    URL url;

    for (ClassLoader cl : classLoader) {

      if (null != cl) {


        url = cl.getResource(resource);


        if (null == url) {
          url = cl.getResource("/" + resource);
        }


        if (null != url) {
          return url;
        }

      }

    }


    return null;

  }


  Class<?> classForName(String name, ClassLoader[] classLoader) throws ClassNotFoundException {

    for (ClassLoader cl : classLoader) {

      if (null != cl) {

        try {

          Class<?> c = Class.forName(name, true, cl);

          if (null != c) {
            return c;
          }

        } catch (ClassNotFoundException e) {

        }

      }

    }

    throw new ClassNotFoundException("Cannot find class: " + name);

  }

  ClassLoader[] getClassLoaders(ClassLoader classLoader) {
    return new ClassLoader[]{
        classLoader,
        defaultClassLoader,
        Thread.currentThread().getContextClassLoader(),
        getClass().getClassLoader(),
        systemClassLoader};
  }

}
