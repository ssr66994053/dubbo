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
package com.alibaba.dubbo.cache.support.jcache;

import javax.cache.*;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;

import java.util.concurrent.TimeUnit;

/**
 * JCache
 * 
 * @author william.liangf
 */
public class JCache extends com.alibaba.dubbo.cache.support.AbstractCache {

    private final Cache<Object, Object> store;

    public JCache(URL url) {
        String type = url.getParameter("jcache");
        String expiration = url.getParameter(Constants.EXPIRATION_KEY, "60");
        CacheManager cacheManager = type == null || type.length() == 0 ? Caching.getCacheManager() : Caching.getCacheManager(type);
        CacheBuilder<Object, Object> cacheBuilder = cacheManager.createCacheBuilder(url.getServiceKey());
        cacheBuilder.setExpiry(CacheConfiguration.ExpiryType.ACCESSED, new CacheConfiguration.Duration(TimeUnit.SECONDS, Integer.valueOf(expiration)));
        this.store = cacheBuilder.build();
    }

    @Override
    public void put(Object key, Object value) {
        store.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return store.get(key);
    }

}
