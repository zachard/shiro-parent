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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * 自定义表单登录拦截器
 * <p>也可以继承{@link AuthenticatingFilter}类, 或是参考{@link FormAuthenticationFilter}<br>
 * 内部实现</p>
 * 
 *
 * @author zachard
 * @version 1.0.0
 */
public class CustomFormLoginFilter extends PathMatchingFilter {
	
	/**
	 * 登录页面URL
	 */
	private String loginUrl = "/login.jsp";
	
	/**
	 * 登录成功之后重定向的路径
	 */
    private String successUrl = "/";

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
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        //若用户已经登录, 则请求继续由过滤器链处理
    	if(SecurityUtils.getSubject().isAuthenticated()) {
            return true;
        }
    	
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        //是否为登录请求
        if(isLoginRequest(req)) {
        	
        	//是否为POST请求
            if("post".equalsIgnoreCase(req.getMethod())) {
                boolean loginSuccess = login(req);
                
                //登录成功重定向到之前页面
                if(loginSuccess) {
                    return redirectToSuccessUrl(req, resp);
                }
                
            }
            
            //请求继续由过滤器链处理
            return true;
        } else {
        	//非登录请求, 则保存登录前页面并重定向到登录页面
            saveRequestAndRedirectToLogin(req, resp);
            return false;
        }
    }

    /**
     * 重定向到登录成功页面
     * 
     * @param req    请求对象
     * @param resp   响应对象
     * @return       {@code false}
     * @throws IOException   IO异常对象
     */
    private boolean redirectToSuccessUrl(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebUtils.redirectToSavedRequest(req, resp, successUrl);
        return false;
    }

    /**
     * 保存登录之前的URL并重定向到登录页面
     * 
     * @param req    请求对象
     * @param resp   响应对象
     * @throws IOException    IO异常
     */
    private void saveRequestAndRedirectToLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WebUtils.saveRequest(req);
        WebUtils.issueRedirect(req, resp, loginUrl);
    }

    /**
     * 根据请求对象中的信息进行登录操作
     * 
     * @param req    请求对象
     * @return       {@code true}登录成功, {@code false}登录失败
     */
    private boolean login(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
        } catch (Exception e) {
            req.setAttribute("shiroLoginFailure", e.getClass());
            return false;
        }
        
        return true;
    }

    /**
     * 判断请求是否为登录请求
     * 
     * @param req    请求对象
     * @return   {@code true}是登录请求, {@code false}不是登录请求
     */
    private boolean isLoginRequest(HttpServletRequest req) {
        return pathsMatch(loginUrl, WebUtils.getPathWithinApplication(req));
    }

}
