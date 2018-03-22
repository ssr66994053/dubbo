/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-09-29 10:37 创建
 *
 */
package com.alibaba.dubbo.cache.validator;

import com.alibaba.dubbo.cache.CacheValueValidator;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;

/**
 * @author daidai@yiji.com
 */
public class NotNullValueValidator implements CacheValueValidator {
	
	public boolean isValid(URL url, Invocation invocation, Object value) {
		if (value != null) {
			return true;
		} else {
			return false;
		}
	}
}
