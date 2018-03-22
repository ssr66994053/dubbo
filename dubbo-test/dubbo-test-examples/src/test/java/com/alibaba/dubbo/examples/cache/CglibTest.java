/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-10-15 15:20 创建
 *
 */
package com.alibaba.dubbo.examples.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.examples.cache.api.HelloOrder;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.core.KeyFactory;

import org.junit.Test;

import com.alibaba.dubbo.examples.cache.api.CacheService;
import com.alibaba.dubbo.examples.cache.api.TimedHelloOrder;
import com.alibaba.dubbo.examples.cache.impl.CacheServiceImpl;
import com.alibaba.fastjson.JSON;

/**
 * @author daidai@yiji.com
 */
public class CglibTest {
    public interface SampleKeyFactory {
        Object newInstance(String first, int second);
    }

    @Test
    public void testKeyFactory() throws Exception {
        SampleKeyFactory keyFactory = (SampleKeyFactory) KeyFactory.create(SampleKeyFactory.class);
        Object key = keyFactory.newInstance("foo", 42);
        System.out.println(JSON.toJSONString(BeanMap.create(key)));
        System.out.println(JSON.toJSONString(key));
        Map<Object, String> map = new HashMap<Object, String>();
        map.put(key, "Hello cglib!");
        assertThat("Hello cglib!").isEqualTo(map.get(keyFactory.newInstance("foo", 42)));
    }

    @Test
    public void testAnnotation() {
        Class<CacheService> serviceClass = CacheService.class;
        printAnnotations(serviceClass.getDeclaredAnnotations());
        printAnnotations(serviceClass.getAnnotations());
        try {
            Method method = serviceClass.getMethod("sayHello", HelloOrder.class);
            printAnnotations(method.getParameterAnnotations());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            Method method = CacheServiceImpl.class.getMethod("sayHello", HelloOrder.class);
            printAnnotations(method.getParameterAnnotations());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    private void printAnnotations(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            System.out.println(annotation.annotationType().getCanonicalName());
        }
    }

    private void printAnnotations(Annotation[][] annotations) {
        for (Annotation[] pa : annotations) {
            printAnnotations(pa);
        }
    }

    @Test
    public void testGetMethod() {
        try {
            for (Method method : CacheService.class.getMethods()) {
                Class[] parameterTypes = method.getParameterTypes();
                for (Class parameterType : parameterTypes) {
                    System.out.println(parameterType.getName());
                }
            }
            Method sayHello = CacheService.class.getMethod("sayHello", HelloOrder.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
