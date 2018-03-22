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
package com.alibaba.dubbo.cache.filter;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.cache.CacheKeyGenerator;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.rpc.*;

/**
 * CacheFilter
 * 
 * @author william.liangf
 */
@Activate(group = {Constants.CONSUMER}, value = Constants.CACHE_KEY)
public class CacheFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private CacheFactory cacheFactory;

    private CacheKeyGenerator keyGenerator;

    public void setCacheFactory(CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    public void setKeyGenerator(CacheKeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (cacheFactory != null && ConfigUtils.isNotEmpty(invoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.CACHE_KEY))) {
            Cache cache = cacheFactory.getCache(invoker.getUrl().addParameter(Constants.METHOD_KEY, invocation.getMethodName()), invocation);
            if (cache != null) {
                Object key = keyGenerator.generateKey(invoker.getUrl(), invocation);
                if (cache.isKeySupported(key)) {
                    Object value = null;
                    try {
                        value = cache.get(key);
                    } catch (Exception e) {
                        logger.error("Fail to get value from cache for key " + key, e);
                    }
                    if (value != null) {
                        return new RpcResult(value);
                    }
                    Result result = invoker.invoke(invocation);
                    if (! result.hasException()) {
                        if (cache.getValidator().isValid(invoker.getUrl(), invocation, result.getValue())) {
                            cache.put(key, result.getValue());
                        }
                    }
                    return result;
                } else {
                    logger.error("key type " + key.getClass().getName() + " is not supported by " + cache.getClass().getName());
                }
            }
        }
        return invoker.invoke(invocation);
    }

}
