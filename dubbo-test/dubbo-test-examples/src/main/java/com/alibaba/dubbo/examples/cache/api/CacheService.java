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
package com.alibaba.dubbo.examples.cache.api;

import com.alibaba.dubbo.cache.keygenerator.selective.KeyComponent;

/**
 * ValidationService
 * 
 * @author william.liangf
 */
public interface CacheService {

    String findCache(@KeyComponent String id);

    Object sayHello(HelloOrder orders);

    Object sayHello(HelloOrder orders, TimedHelloOrder timedHelloOrder);
}
