/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-10-15 10:23 创建
 *
 */
package com.alibaba.dubbo.examples.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import com.alibaba.dubbo.examples.cache.testapi.CacheService;
import com.alibaba.dubbo.examples.cache.testapi.HelloOrder;
import com.alibaba.dubbo.examples.cache.testapi.TimedHelloOrder;
import org.junit.BeforeClass;
import org.junit.Test;

import com.alibaba.dubbo.cache.keygenerator.selective.SelectiveCacheKeyGenerator;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;


/**
 * @author daidai@yiji.com
 */
public class SelectiveKeyGeneratorTest {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private static SelectiveCacheKeyGenerator keyGenerator = new SelectiveCacheKeyGenerator();
    private static URL url = URL.valueOf("consumer://192.168.46.4/" + CacheService.class.getCanonicalName() +
            "?application=piggy&category=consumers&check=false&default.check=false&default.loadbalance=roundrobin" +
            "&dubbo=yiji-2.5.6&interface=" + CacheService.class.getCanonicalName() + "&logger=slf4j&methods=sayHello&" +
            "organization=technology-center&owner=yaoyao&pid=12461&revision=3.0.1-20151012&" +
            "side=consumer×tamp=1444739664420&version=1.5");
    private static RpcInvocation invocation = new RpcInvocation();
    private static String clazz = CacheService.class.getCanonicalName();
    private static String method = "sayHello";

    @BeforeClass
    public static void init() {
        invocation.setInvoker(new Invoker<CacheService>() {
            public Class<CacheService> getInterface() {
                return CacheService.class;
            }

            public Result invoke(Invocation invocation) throws RpcException {
                return null;
            }

            public URL getUrl() {
                return null;
            }

            public boolean isAvailable() {
                return false;
            }

            public void destroy() {

            }
        });
        invocation.setMethodName("sayHello");

    }


    private static class A {
        private String zip;
        private String extract;
        private String compress;

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getExtract() {
            return extract;
        }

        public void setExtract(String extract) {
            this.extract = extract;
        }

        public String getCompress() {
            return compress;
        }

        public void setCompress(String compress) {
            this.compress = compress;
        }

        @Override
        public String toString() {
            return "A{" +
                    "zip='" + zip + '\'' +
                    ", extract='" + extract + '\'' +
                    ", compress='" + compress + '\'' +
                    '}';
        }
    }

    @Test
    public void testJSON() {
        A a = new A();
        a.setZip("1");
        a.setExtract("5");
        a.setCompress("3");
        assertThat(JSON.toJSONString(a, SerializerFeature.SortField)).isEqualTo("{\"compress\":\"3\",\"extract\":\"5\",\"zip\":\"1\"}");
    }

    @Test
    public void testGenKey1() {
        String expectedKey = CacheService.class.getCanonicalName() +
                "#sayHello({\"message\":\"How are you?\",\"userId\":\"daidai\"})";
        testKey(expectedKey, getHelloOrderClass(), getHelloOrder());
    }

    private void testKey(String expectedKey, Class[] paramTypes, Object ... args) {
        invocation.setArguments(args);
        invocation.setParameterTypes(paramTypes);
        String key = (String) keyGenerator.generateKey(url, invocation);
        assertThat(key).isEqualTo(expectedKey);
    }

    private void testKey(String expectedKey, String methodName, Class[] paramTypes, Object ... args) {
        invocation.setArguments(args);
        invocation.setParameterTypes(paramTypes);
        String key = (String) keyGenerator.generateKey(CacheService.class, methodName, paramTypes, args);
        assertThat(key).isEqualTo(expectedKey);
    }

    private Class[] getHelloOrderClass() {
        return new Class[] {HelloOrder.class};
    }

    private HelloOrder getHelloOrder() {
        return new HelloOrder("daidai", "How are you?");
    }

    @Test
    public void testGenKey2() {
        String expectedKey = CacheService.class.getCanonicalName() +
                "#sayHello({\"important\":false,\"message\":\"How are you?\",\"time\":1444879163512,\"userId\":\"daidai\"})";
        testKey(expectedKey, getHelloOrderClass(), getTimedHelloOrder());
    }

    private TimedHelloOrder getTimedHelloOrder() {
        TimedHelloOrder helloOrder = new TimedHelloOrder("daidai", "How are you?");
        long time = 1444879163512L;
        helloOrder.setOrderNo("1");
        helloOrder.setTime(new Date(time));
        return helloOrder;
    }

    @Test
    public void testGenKey3() {
        String expectedKey = CacheService.class.getCanonicalName() +
                "#sayHello({\"message\":\"How are you?\",\"userId\":\"daidai\"},{\"important\":false," +
                "\"message\":\"How are you?\",\"time\":1444879163512,\"userId\":\"daidai\"})";
        testKey(expectedKey, new Class[]{HelloOrder.class, TimedHelloOrder.class}, getHelloOrder(), getTimedHelloOrder());
    }

    private long timeRunnable(Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        return System.currentTimeMillis() - start;
    }

    @Test
    public void testGenKey4() {
        keyGenerator = new SelectiveCacheKeyGenerator();
        long cost1 = timeRunnable(new Runnable() {
            public void run() {
                testGenKey1();
            }
        });
        long cost2 = timeRunnable(new Runnable() {
            public void run() {
                testGenKey1();
            }
        });
        assertThat(cost1).isGreaterThanOrEqualTo(cost2);
        logger.info("\ncost without class cache " + cost1 + ",\ncost with class cache " + cost2);
    }

    @Test
    public void testGenKey5() {
        final int total = 10000;
        long cost = timeRunnable(new Runnable() {
            public void run() {
                int i = 0;
                while (i++ < total) {
                    testGenKey3();
                }
            }
        });
        double actual = cost * 1.0 / total;
        double other = 0.5;
        System.err.println("average genkey cost: " + actual + " ms");
        assertThat(actual).isLessThan(other)
                .withFailMessage("average cost of generating one key is too high: %f, expected limit: %f", actual, other);
    }

    @Test
    public void testGenKey6() {
        String expectedKey = CacheService.class.getCanonicalName() +
                "#sayHello(1,{\"message\":\"How are you?\",\"userId\":\"daidai\"})";
        testKey(expectedKey, new Class[]{int.class, HelloOrder.class}, 1, getHelloOrder());
    }

    @Test
    public void testGenKey7() {
        String expectedKey = CacheService.class.getCanonicalName() +
                "#sayHello(1,{\"message\":\"How are you?\",\"userId\":\"daidai\"})";
        testKey(expectedKey, "sayHello", new Class[]{int.class, HelloOrder.class}, 1, getHelloOrder());
    }
}
