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
import org.apache.shiro.subject.Subject;

/**
 * 对 /permission 请求进行处理的servlet
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
@SuppressWarnings("serial")
@WebServlet(name = "permissionServlet", urlPatterns = "/permission")
public class PermissionServlet extends HttpServlet {
	
	/**
	 * 对 /permission 的GET请求进行处理的方法
	 * <p>先判断是否有 user:create 权限，有则跳转至是否有权限的界面</p>
	 * 
	 * @param  req    请求对象
	 * @param  resp   响应对象
	 */
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("user:create");
        req.getRequestDispatcher("/WEB-INF/jsp/hasPermission.jsp").forward(req, resp);
    }

}
