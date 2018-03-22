/*
 * Copyright 1999-2012 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.examples.cache;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.cache.keygenerator.selective.SelectiveCacheKeyGenerator;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.examples.cache.api.CacheService;
import com.alibaba.dubbo.examples.cache.api.HelloOrder;
import com.alibaba.dubbo.examples.cache.api.TimedHelloOrder;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;

/**
 * CacheConsumer
 * 
 * @author william.liangf
 */
public class CacheConsumer {
    
    public static void main(String[] args) throws Exception {
        System.setProperty("hera.zk.connectionStr", "snet.yiji:2181") ;
        String config = CacheConsumer.class.getPackage().getName().replace('.', '/') + "/cache-consumer.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
        context.start();
        
        CacheService cacheService = (CacheService)context.getBean("cacheService");
        RedisService<String, String, Object> redisService = (RedisService<String, String, Object>) context.getBean("redisService");
        testLruCache(cacheService);
        testRedisCache(cacheService);
        System.err.println("key length:" + getKey().length());
        testObjectReadWrite(redisService);
    }

    private static void testLruCache(CacheService cacheService) throws InterruptedException {
        // 测试缓存生效，多次调用返回同样的结果。(服务器端自增长返回值)
        String fix = null;
        for (int i = 0; i < 5; i ++) {
            String result = cacheService.findCache("0");
            if (fix == null || fix.equals(result)) {
                System.out.println("OK: " + result);
            } else {
                System.err.println("ERROR: " + result);
            }
            fix = result;
            Thread.sleep(500);
        }
        System.err.println("part 1 finished");
        Thread.sleep(500);
        // LRU的缺省cache.size为1000，执行1001次，应有溢出
        for (int n = 0; n < 1001; n ++) {
            String pre = null;
            for (int i = 0; i < 10; i ++) {
                String result = cacheService.findCache(String.valueOf(n));
                if (pre != null && ! pre.equals(result)) {
                    System.err.println("ERROR: " + result);
                }
                pre = result;
            }
        }
        System.err.println("part 2 finished");
        Thread.sleep(500);
        // 测试LRU有移除最开始的一个缓存项
        String result = cacheService.findCache("0");
        if (fix != null && ! fix.equals(result)) {
            System.out.println("OK: " + result);
        } else {
            System.err.println("ERROR: " + result);
        }
    }

    private static void testRedisCache(CacheService cacheService) {
        System.err.println("first round!");
        ConcurrentHashMap<Object, Object> map = new ConcurrentHashMap<Object, Object>();
        test(map, cacheService);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.err.println("second round!");
        test(map, cacheService);
    }

    private static void test(ConcurrentHashMap<Object, Object> map, CacheService cacheService) {
        int i = 0;
        int total = 10000;
        long start = System.currentTimeMillis();
        String userId = "ajigenghie243546578";
        for (; i < 10000; ++i) {
            HelloOrder helloOrder = new TimedHelloOrder(true, userId, "message-" + i, "1234", new Date());
            Object objects = cacheService.sayHello(helloOrder);
        }
        System.err.println("average cost: " + (System.currentTimeMillis() - start) * 1.0 / total);
    }

    private static void testObjectReadWrite(RedisService redisService) {
        long x = CacheConsumer.<String, String, Object>testObjectRead(redisService, getKey(), getValue());
        System.err.println("read cost for object value: " + x);
        x = CacheConsumer.<String, String, Object>testObjectRead(redisService, getKey(), JSON.toJSONString(getValue()));
        System.err.println("read cost for string value: " + x);

        x = CacheConsumer.<String, String, Object>testObjectWrite(redisService, getKey(), getValue());
        System.err.println("write cost for object value: " + x);
        x = CacheConsumer.<String, String, Object>testObjectWrite(redisService, getKey(), JSON.toJSONString(getValue()));
        System.err.println("write cost for string value: " + x);
    }

    private static <K, F, V> long testObjectRead(RedisService<K, F, V> redisService, K key, V value) {
        redisService.put(key, value);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            if (redisService.get(key) == null) {
                throw new IllegalStateException("null value");
            }
        }
        return System.currentTimeMillis() - start;
    }

    private static <K, F, V> long testObjectWrite(RedisService<K, F, V> redisService, K key, V value) {
        int n = 10000;
        String[] keys = genKeys(key, n);
        long start = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            redisService.put((K)keys[i], value);
        }
        return System.currentTimeMillis() - start;
    }

    private static String[] genKeys(Object key, int num) {
        String[] strings = new String[num];
        for (int i = 0; i < num; i++) {
            strings[i] = String.format("%s_%d", key, num);
        }
        return strings;
    }

    private static String getKey() {
        HelloOrder helloOrder = new HelloOrder("ajigenghie243546578", "message 9999");
        helloOrder.setGid("H0000000043722015101514505421500000");
        SelectiveCacheKeyGenerator keyGenerator = new SelectiveCacheKeyGenerator();
        URL url = URL.valueOf("consumer://192.168.46.4/com.alibaba.dubbo.examples.cache.api.CacheService?application=piggy&" +
                "category=consumers&check=false&default.check=false&default.loadbalance=roundrobin&dubbo=yiji-2.5.6&" +
                "interface=com.alibaba.dubbo.examples.cache.api.CacheService&logger=slf4j&methods=sayHello&" +
                "organization=technology-center&owner=yaoyao&pid=12461&revision=3.0.1-20151012&" +
                "side=consumer×tamp=1444739664420&version=1.5");
        RpcInvocation invocation = new RpcInvocation();
        invocation.setMethodName("sayHello");
        invocation.setArguments(new Object[]{helloOrder});
        invocation.setParameterTypes(new Class[]{HelloOrder.class});
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
        return ((String) keyGenerator.generateKey(url, invocation));
    }

    private static Object getValue() {
        HelloOrder helloOrder = new HelloOrder("ajigenghie243546578", "message 9999");
        helloOrder.setGid("H0000000043722015101514505421500000");
        return helloOrder;
    }
}
