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
import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.support.hessian.Hessian2SerializerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * Hessian2 Object input.
 * 
 * @author qian.lei
 */

public class Hessian3ObjectInput implements ObjectInput {
	private Hessian2Input mH2i;
	
	public Hessian3ObjectInput(InputStream is) {
		Deflation envelope = new Deflation();
		mH2i = new Hessian2Input(is);
		mH2i.setSerializerFactory(Hessian2SerializerFactory.SERIALIZER_FACTORY);
		try {
			mH2i = envelope.unwrap(mH2i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean readBool() throws IOException {
		return mH2i.readBoolean();
	}
	
	public byte readByte() throws IOException {
		return (byte) mH2i.readInt();
	}
	
	public short readShort() throws IOException {
		return (short) mH2i.readInt();
	}
	
	public int readInt() throws IOException {
		return mH2i.readInt();
	}
	
	public long readLong() throws IOException {
		return mH2i.readLong();
	}
	
	public float readFloat() throws IOException {
		return (float) mH2i.readDouble();
	}
	
	public double readDouble() throws IOException {
		return mH2i.readDouble();
	}
	
	public byte[] readBytes() throws IOException {
		return mH2i.readBytes();
	}
	
	public String readUTF() throws IOException {
		return mH2i.readString();
	}
	
	public Object readObject() throws IOException {
		return mH2i.readObject();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
		return (T) mH2i.readObject(cls);
	}
	
	public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
		return readObject(cls);
	}
	
}