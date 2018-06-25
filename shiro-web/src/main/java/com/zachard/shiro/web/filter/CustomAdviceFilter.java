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

import org.apache.shiro.web.servlet.AdviceFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义实现通知拦截器
 * <p>通知拦截器包含前置处理，后置处理及后置最终处理三个步骤</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class CustomAdviceFilter extends AdviceFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomAdviceFilter.class);
	
	/**
	 * 进行请求的预处理，根据返回值决定是否继续处理
	 * <p>可以通过此方法进行权限控制</p>
	 * 
	 * @param   request    请求对象
	 * @param   response   响应对象
	 * @return  后续拦截器是否执行  {@code true} 表示继续执行  {@code false} 表示中断执行
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		logger.info("自定义通知拦截器前置处理");
		return true;
	}
	
	/**
	 * 对post通知进行处理
	 * <p>只有在拦截器链未发生异常的情况下才会执行,<br />
	 * 即如果有异常发生，这个方法不会被调用</p>
	 * 
	 * @param  request   请求对象
	 * @param  response  响应对象
	 */
	@Override
	protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
		logger.info("自定义通知拦截器后置处理");
	}
	
	/**
	 * 在拦截器完成处理请求完成之后调用
	 * <p>不管是返回 {@code false} 或者抛出异常, 或是正常处理，此方法一定会被执行<br />
	 * 常用于完成资源的清理</p>
	 * 
	 * @param    request   请求对象
	 * @param    response  响应对象
	 * @param    exception 异常对象
	 */
	@Override
	public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
		logger.info("自定义通知拦截器后置最终处理");
	}

}
