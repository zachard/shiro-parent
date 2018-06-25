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

import java.util.Arrays;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义指定路径的拦截器
 * <p>{@link PathMatchingFilter} 会对指定的url进行处理, 而忽略其他url请求</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class CustomPathMatchingFilter extends PathMatchingFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomPathMatchingFilter.class);
	
	/**
	 * 如果url模式与请求url匹配，则执行onPreHandle, 并把该拦截器配置的参数传入
	 * <p>用于进行一些验证(如角色授权)</p>
	 * <p>只需实现 {@link PathMatchingFilter} 的 {@link PathMatchingFilter#onPreHandle(ServletRequest, ServletResponse, Object)} 方法<br />
	 * 而无需实现 {@link #preHandle(ServletRequest, ServletResponse)} 方法</p>
	 * 
	 * @param   request       请求对象
	 * @param   response      响应对象
	 * @param   mappedValue   在规则映射中为拦截器指定的URL
	 * @return  {@code true} 当请求需要进行被拦截器处理时，{@code false} 当拦截器处理请求后直接返回响应时
	 */
	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, 
			Object mappedValue) throws Exception {
		logger.info("URL matches, config is: " + Arrays.asList((String[]) mappedValue));
		return true;
	}

}
