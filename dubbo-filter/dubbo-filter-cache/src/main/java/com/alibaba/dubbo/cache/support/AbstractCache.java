/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-09-25 10:05 创建
 *
 */
package com.alibaba.dubbo.cache.support;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.CacheValueValidator;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

/**
 * @author qiubo@yiji.com
 */
public class AbstractCache implements Cache {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private CacheValueValidator validator;
	
	public void put(Object key, Object value) {
		
	}
	
	public Object get(Object key) {
		return null;
	}
	
	public boolean isKeySupported(Object key) {
		if (key != null && key instanceof String) {
			return true;
		}
		return false;
	}
	
	public CacheValueValidator getValidator() {
		return validator;
	}
	
	public void setValidator(CacheValueValidator validator) {
		this.validator = validator;
	}
}
