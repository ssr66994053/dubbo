/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-09-23 17:47 创建
 *
 */
package com.alibaba.dubbo.cache.keygenerator.selective;

/**
 * @author daidai@yiji.com
 */
public interface KeyComponentClassGenerator {
	Class generate(Class clazz);
}
