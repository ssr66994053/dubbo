/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-09-24 14:36 创建
 *
 */
package com.alibaba.dubbo.cache;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;
import com.alibaba.dubbo.rpc.Invocation;

/**
 * @author daidai@yiji.com
 */
@SPI("allargs")
public interface CacheKeyGenerator {

    @Adaptive("keygen")
    Object generateKey(URL url, Invocation invocation);
}
