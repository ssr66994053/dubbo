/*
 * www.yiji.com Inc.
 * Copyright (c) 2016 All Rights Reserved
 */

/*
 * 修订记录:
 * qiubo@yiji.com 2016-06-07 19:27 创建
 */
package com.alibaba.dubbo.common.serialize.support.hessian3;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 支持数据压缩
 * @author qiubo@yiji.com
 */
public class Hessian3Serialization implements Serialization {
	
	public static final byte ID = 8;
	
	public byte getContentTypeId() {
		return ID;
	}
	
	public String getContentType() {
		return "x-application/hessian3";
	}
	
	public ObjectOutput serialize(URL url, OutputStream out) throws IOException {
		return new Hessian3ObjectOutput(out);
	}
	
	public ObjectInput deserialize(URL url, InputStream is) throws IOException {
		return new Hessian3ObjectInput(is);
	}
	
}