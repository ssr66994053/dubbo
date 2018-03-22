/*
 * Copyright 1999-2011 Alibaba Group.
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
package com.alibaba.dubbo.common.serialize.support.hessian3;

import com.alibaba.com.caucho.hessian.io.Deflation;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.support.hessian.Hessian2SerializerFactory;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Hessian2 Object output.
 * 
 * @author qian.lei
 */

public class Hessian3ObjectOutput implements ObjectOutput {
	private Hessian2Output mH2o;
	
	public Hessian3ObjectOutput(OutputStream os) {
		Hessian2Output tmp = new Hessian2Output(os);
		Deflation envelope = new Deflation();
		tmp.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
		try {
			mH2o = envelope.wrap(tmp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeBool(boolean v) throws IOException {
		mH2o.writeBoolean(v);
	}
	
	public void writeByte(byte v) throws IOException {
		mH2o.writeInt(v);
	}
	
	public void writeShort(short v) throws IOException {
		mH2o.writeInt(v);
	}
	
	public void writeInt(int v) throws IOException {
		mH2o.writeInt(v);
	}
	
	public void writeLong(long v) throws IOException {
		mH2o.writeLong(v);
	}
	
	public void writeFloat(float v) throws IOException {
		mH2o.writeDouble(v);
	}
	
	public void writeDouble(double v) throws IOException {
		mH2o.writeDouble(v);
	}
	
	public void writeBytes(byte[] b) throws IOException {
		mH2o.writeBytes(b);
	}
	
	public void writeBytes(byte[] b, int off, int len) throws IOException {
		mH2o.writeBytes(b, off, len);
	}
	
	public void writeUTF(String v) throws IOException {
		mH2o.writeString(v);
	}
	
	public void writeObject(Object obj) throws IOException {
		mH2o.writeObject(obj);
	}
	
	public void flushBuffer() throws IOException {
		mH2o.completeMessage();
		mH2o.close();
	}
}