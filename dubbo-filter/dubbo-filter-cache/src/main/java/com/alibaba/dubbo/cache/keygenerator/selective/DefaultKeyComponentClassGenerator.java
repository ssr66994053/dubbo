/*
 * www.yiji.com Inc.
 * Copyright (c) 2014 All Rights Reserved
 */

/*
 * 修订记录:
 * daidai@yiji.com 2015-09-23 17:50 创建
 *
 */
package com.alibaba.dubbo.cache.keygenerator.selective;

import com.alibaba.dubbo.common.utils.StringUtils;
import net.sf.cglib.beans.BeanGenerator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daidai@yiji.com
 */
public class DefaultKeyComponentClassGenerator implements KeyComponentClassGenerator {
	private ConcurrentHashMap<Class, Class> arg2classMap = new ConcurrentHashMap<Class, Class>();

	public Class generate(Class clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("clazz can't be null!");
		}
		Class aClass = arg2classMap.get(clazz);
		if (aClass != null) {
			return aClass;
		}
		String[] properties = null;
		if (clazz.isAnnotationPresent(KeyComponent.class)) {
			KeyComponent keyComponent = (KeyComponent) clazz.getAnnotation(KeyComponent.class);
			properties = keyComponent.properties();
			if (properties == null || properties.length == 0) {
				arg2classMap.putIfAbsent(clazz, clazz);
				return arg2classMap.get(clazz);
			}
		}

		Map<String, Boolean> hasProperty = new HashMap<String, Boolean>();
		if (properties != null && properties.length > 0) {
			for (String property : properties) {
				hasProperty.put(property, false);
			}
		}

		BeanGenerator generator = new BeanGenerator();
		for (Method method : clazz.getMethods()) {
			if (isGetter(method) || isBooleanGetter(method)) {
				String name = propertyName(method.getName());
				if (isAnnotated(method)) {
					hasProperty.put(name, true);
					generator.addProperty(name, method.getReturnType());
				} else {
					if (hasProperty.containsKey(name)) {
						hasProperty.put(name, true);
						generator.addProperty(name, method.getReturnType());
					}
				}
			}
		}

		Set<String> invalidProperties = new HashSet<String>();
		for (Map.Entry<String, Boolean> entry : hasProperty.entrySet()) {
			if (!entry.getValue()) {
				invalidProperties.add(entry.getKey());
			}
		}

		if (!invalidProperties.isEmpty()) {
			throw new IllegalArgumentException(String.format("No getters are found for the following properties: %s",
					StringUtils.join(invalidProperties, ",")));
		}

		aClass = (Class) generator.createClass();
		arg2classMap.putIfAbsent(clazz, aClass);
		return arg2classMap.get(clazz);
	}
	
	private boolean isAnnotated(Method method) {
		return (method.getAnnotation(KeyComponent.class) != null);
	}
	
	private boolean isBooleanGetter(Method method) {
		String methodName = method.getName();
		return methodName.startsWith("is") && methodName.length() > 2
				&& method.getParameterTypes().length == 0
				&& !method.getReturnType().equals(Void.class);
	}
	
	private boolean isGetter(Method method) {
		String methodName = method.getName();
		return methodName.startsWith("get") && methodName.length() > 3
				&& method.getParameterTypes().length == 0
				&& !method.getReturnType().equals(Void.class);
	}
	
	private String propertyName(String getter) {
		StringBuilder builder = new StringBuilder();
		if (getter.startsWith("get")) {
			builder.append(getter.substring(3, 4).toLowerCase());
			if (getter.length() > 4) {
				builder.append(getter.substring(4));
			}
			return builder.toString();
		} else {
			builder.append(getter.substring(2, 3).toLowerCase());
			if (getter.length() > 3) {
				builder.append(getter.substring(3));
			}
			return builder.toString();
		}
	}
}
