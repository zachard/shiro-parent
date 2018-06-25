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

package com.zachard.shiro.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制登录的servlet类
 * <p>
 * </p>
 *
 * @author zachard
 * @version 1.0.0
 */
@SuppressWarnings("serial")
@WebServlet(name = "loginServlet", urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);

	/**
	 * 处理/login的GET请求
	 * <p>将请求转发到login.jsp页面</p>
	 * 
	 * @param  req   请求对象
	 * @param  resp  响应对象
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
	}

	/**
	 * 处理用户登录的POST请求
	 * <p>验证用户名与密码的正确性</p>
	 * 
	 * @param    req    请求对象
	 * @param    resp   响应对象
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String error = null;
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		token.setRememberMe(true);
		
		//进行登录操作
		try {
			subject.login(token);
		} catch (UnknownAccountException e) {
			logger.error("用户: " + username + "尝试使用密码: " + password + "登录失败", e);
			error = "用户名/密码错误";
		} catch (IncorrectCredentialsException e) {
			logger.error("用户: " + username + "尝试使用密码: " + password + "登录失败", e);
			error = "用户名/密码错误";
		} catch (AuthenticationException e) {
			// 其他错误，比如锁定，如果想单独处理请单独catch处理
			logger.error("用户: " + username + "尝试使用密码: " + password + "登录失败", e);
			error = "其他错误：" + e.getMessage();
		}

		if (error != null) {
			// 出错了，返回登录页面
			req.setAttribute("error", error);
			req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
		} else {
			// 直接跳转到成功页面(例如: 主页)
			//req.getRequestDispatcher("/WEB-INF/jsp/loginSuccess.jsp").forward(req, resp);
			
			//实现重定向到登录之前的url
			WebUtils.redirectToSavedRequest(req, resp, "/");
		}
	}

}
