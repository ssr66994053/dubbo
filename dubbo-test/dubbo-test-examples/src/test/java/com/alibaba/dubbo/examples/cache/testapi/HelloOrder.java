/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-07-14 17:46 创建
 *
 */
package com.alibaba.dubbo.examples.cache.testapi;

import com.alibaba.dubbo.cache.keygenerator.selective.KeyComponent;
import com.google.common.base.Objects;
import com.yjf.common.service.OrderBase;

/**
 * @author daidai@yiji.com
 */
@KeyComponent(properties = {"userId"})
public class HelloOrder extends OrderBase {
	private String userId;
	private String message;
	
	public HelloOrder() {
		super();
	}
	
	public HelloOrder(String userId, String message) {
		super();
		this.userId = userId;
		this.message = message;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@KeyComponent
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public boolean isCheckGid() {
		return false;
	}

	@Override
	public String toString() {
		return "HelloOrder{" +
				"userId='" + userId + '\'' +
				", message='" + message + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		HelloOrder that = (HelloOrder) o;
		return Objects.equal(getUserId(), that.getUserId()) &&
				Objects.equal(getMessage(), that.getMessage());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getUserId(), getMessage());
	}
}
