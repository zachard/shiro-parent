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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义OncePerRequestFilter
 * <p>自定义实现每次请求只会调用一次的过滤器</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class CustomOncePerRequestFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomOncePerRequestFilter.class);

	/**
	 * 每次请求调用的过滤器方法
	 * <p>每次请求都只会调用一次的方法</p>
	 * 
	 * @param    request    请求对象
	 * @param    response   响应对象
	 * @param    chain      过滤器链
	 */
	@Override
	protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		logger.info("调用了每次请求只调用一次的拦截器方法");
		chain.doFilter(request, response);
	}

}
