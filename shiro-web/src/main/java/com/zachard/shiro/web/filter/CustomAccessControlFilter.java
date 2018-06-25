/*
 *  Copyright 2015-2017 zachard, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zachard.shiro.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义访问控制拦截器
 * <p>{@link AccessControlFilter}类定义了对资源进行访问控制的拦截器<br />
 * 当未通过认证时，可能会重定向到登录页面, 继承了{@link PathMatchingFilter}类</p>
 * 
 * <p>通过覆写{@link AccessControlFilter#saveRequestAndRedirectToLogin(javax.servlet.ServletRequest, javax.servlet.ServletResponse)}<br />
 * 方法实现未认证时的自定义操作</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class CustomAccessControlFilter extends AccessControlFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomAccessControlFilter.class);

	/**
	 * 判断请求是否允许访问资源
	 *
	 * @param    request   请求对象
	 * @param    response  响应对象
	 * @param    mappedValue   与拦截器配置对应的拦截器URLs对应值
	 * @return   当请求允许访问资源时,返回{@code true}并交由后续拦截器处理<br>
	 *           当需要交由{@link #onAccessDenied(ServletRequest, ServletResponse)}方法处理时，返回{@code false}
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		logger.info("允许访问资源");
		
		return false;
	}

	/**
	 * 当{@link #isAccessAllowed(ServletRequest, ServletResponse, Object)}方法时,对请求进行处理
	 * 
	 * @param    request   请求对象
	 * @param    response  响应对象
	 * @return   当请求还需要继续被处理时,返回{@code true}; 当请求被子类处理完成时,返回{@code false}
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		logger.info("访问拒绝，并且不进行处理，转而交给其他拦截器处理");
		
		return true;
	}

}
