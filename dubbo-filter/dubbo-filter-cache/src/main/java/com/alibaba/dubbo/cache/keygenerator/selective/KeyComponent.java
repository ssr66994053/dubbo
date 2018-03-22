/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-09-23 17:39 创建
 *
 */
package com.alibaba.dubbo.cache.keygenerator.selective;

import java.lang.annotation.*;

/**
 * @author daidai@yiji.com
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KeyComponent {
	/**
	 * 返回被标记类型作为key组成部分的property名字。仅当被标记的对象是ElementType.TYPE类型时生效。
	 * @return 作为key组成部分的property名字
	 */
	String[] properties() default {""};
}
