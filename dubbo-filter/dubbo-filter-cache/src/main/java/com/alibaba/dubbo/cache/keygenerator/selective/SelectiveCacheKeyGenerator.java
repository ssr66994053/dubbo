/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-09-24 18:45 创建
 *
 */
package com.alibaba.dubbo.cache.keygenerator.selective;

import com.alibaba.dubbo.cache.CacheKeyGenerator;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.sf.cglib.beans.BeanCopier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daidai@yiji.com
 */
public class SelectiveCacheKeyGenerator implements CacheKeyGenerator {
	private KeyComponentClassGenerator componentClassGenerator = new DefaultKeyComponentClassGenerator();
	private ConcurrentHashMap<String, Boolean[]> argAnnotationMap = new ConcurrentHashMap<String, Boolean[]>();

	public void setComponentClassGenerator(KeyComponentClassGenerator componentClassGenerator) {
		this.componentClassGenerator = componentClassGenerator;
	}
	
	public Object generateKey(URL url, Invocation invocation) {
		String claz = url.getServiceInterface();
		String methodName = invocation.getMethodName();
		Class<?>[] parameterTypes = invocation.getParameterTypes();
		Object[] args = invocation.getArguments();
		String methodSignature = getMethodSignature(claz, methodName, parameterTypes);
		Boolean[] hasAnnotation = argAnnotationMap.get(methodSignature);
		if (hasAnnotation == null) {
			hasAnnotation = getAnnotations(invocation, methodName, parameterTypes);
			argAnnotationMap.putIfAbsent(methodSignature, hasAnnotation);
			hasAnnotation = argAnnotationMap.get(methodSignature);
		}
		return genKeyInternal(claz, methodName, args, hasAnnotation);
	}

	public Object generateKey(Class<?> interfaceClaz, String methodName, Class[] parameterTypes, Object[] args) {
		String claz = interfaceClaz.getCanonicalName();
		String methodSignature = getMethodSignature(claz, methodName, parameterTypes);
		Boolean[] hasAnnotation = argAnnotationMap.get(methodSignature);
		if (hasAnnotation == null) {
			hasAnnotation = getAnnotations(interfaceClaz, methodName, parameterTypes);
			argAnnotationMap.putIfAbsent(methodSignature, hasAnnotation);
			hasAnnotation = argAnnotationMap.get(methodSignature);
		}
		return genKeyInternal(claz, methodName, args, hasAnnotation);
	}

	private String genKeyInternal(String clazz, String methodName, Object[] args, Boolean[] hasAnnotation) {
		StringBuilder builder = new StringBuilder(512);
		builder.append(clazz).append("#").append(methodName).append("(");
		Object arg;
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; ++i) {
				arg = args[i];
				if (arg != null) {
					if (hasAnnotation[i]) {
						builder.append(JSON.toJSONString(arg, SerializerFeature.SortField)).append(",");
					} else {
						Class generateClazz = componentClassGenerator.generate(arg.getClass());
						try {
							Object bean = copyBean(arg, generateClazz.newInstance());
							builder.append(JSON.toJSONString(bean, SerializerFeature.SortField)).append(",");
						} catch (InstantiationException e) {
							throw new IllegalStateException(e);
						} catch (IllegalAccessException e) {
							throw new IllegalStateException(e);
						}
					}
				} else {
					builder.append("{},");
				}
			}
		}
		if (builder.charAt(builder.length() - 1) == ',') {
			builder.setCharAt(builder.length() - 1, ')');
		} else {
			builder.append(")");
		}
		return builder.toString();
	}

	private Object copyBean(Object from, Object to) {
		BeanCopier beanCopier = BeanCopier.create(from.getClass(), to.getClass(), false);
		beanCopier.copy(from, to, null);
		return to;
	}

	private String getMethodSignature(String clazz, String method, Class[] parameterTypes) {
		StringBuilder builder = new StringBuilder(1024);
		builder.append(clazz).append("#").append(method).append("(");
		if (parameterTypes.length > 0) {
			builder.append(parameterTypes[0].getCanonicalName());
		}
		for (int i = 1; i < parameterTypes.length; i++) {
			builder.append(",").append(parameterTypes[i].getCanonicalName());
		}
		builder.append(")");
		return builder.toString();
	}

	private Boolean[] getAnnotations(Invocation invocation, String methodName, Class[] parameterTypes) {
		return getAnnotations(invocation.getInvoker().getInterface(), methodName, parameterTypes);
	}

	private Boolean[] getAnnotations(Class<?> interfaceClaz, String methodName, Class[] parameterTypes) {
		try {
			Method method = interfaceClaz.getMethod(methodName, parameterTypes);
			return getAnnotations(method);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private Boolean[] getAnnotations(Method method) {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Boolean[] ret = new Boolean[parameterAnnotations.length];
		for (int i = 0; i < parameterAnnotations.length; ++i) {
            ret[i] = containAnnotation(parameterAnnotations[i]);
        }
		return ret;
	}

	private boolean containAnnotation(Annotation[] target) {
		for (Annotation a : target) {
			if (a.annotationType().equals(KeyComponent.class)) {
				return true;
			}
		}
		return false;
	}
}
