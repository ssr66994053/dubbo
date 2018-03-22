/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-09-24 18:43 创建
 *
 */
package com.alibaba.dubbo.cache.keygenerator.allargs;

import com.alibaba.dubbo.cache.CacheKeyGenerator;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Invocation;

/**
 * @author daidai@yiji.com
 */
public class AllArgsCacheKeyGenerator implements CacheKeyGenerator {
	
	public Object generateKey(URL url, Invocation invocation) {
		return StringUtils.toArgumentString(invocation.getArguments());
	}
}
