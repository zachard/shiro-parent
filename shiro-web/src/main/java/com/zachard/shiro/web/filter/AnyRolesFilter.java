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
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * 任意角色拦截器,基于访问控制拦截器实现
 * <p>只要包含任一角色则交由拦截器链下一拦截器进行处理</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class AnyRolesFilter extends AccessControlFilter {
	
	/**
	 * 未认证页面
	 */
	private String unauthorizedUrl = "/unauthorized.jsp";
	
	/**
	 * 登录URL
	 */
    private String loginUrl = "/login.jsp";

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
		//获取到拦截器参数(配置的roles)
		String[] roles = (String[]) mappedValue;
		
		//如果没有设置角色参数，默认成功
        if(roles == null) {
            return true;
        }
        
        //检查是否拥有任一一个角色(有则将请求交由拦截器链下一拦截器处理)
        for(String role : roles) {
        	
            if(getSubject(request, response).hasRole(role)) {
                return true;
            }
            
        }
        
        //跳到onAccessDenied处理
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
		Subject subject = getSubject(request, response);
		
		//如果没有登录，重定向到登录页面
        if (subject.getPrincipal() == null) {
            saveRequest(request);
            WebUtils.issueRedirect(request, response, loginUrl);
        } else {
        	
        	//未授权页面不为空
            if (StringUtils.hasText(unauthorizedUrl)) {
                WebUtils.issueRedirect(request, response, unauthorizedUrl);
            } else {
            	//否则返回401未授权状态码
                WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        
        return false;
	}

}
