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
 * 对/role请求进行处理的servlet
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
@SuppressWarnings("serial")
@WebServlet(name = "roleServlet", urlPatterns = "/role")
public class RoleServlet extends HttpServlet {
	
	/**
	 * 对/role的GET请求进行处理的方法
	 * 
	 * @param    req    请求对象
	 * @param    resp   响应对象
	 */
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Subject subject = SecurityUtils.getSubject();
        
        //也在在ini配置文件的[urls]中配置/role需要的角色(当不具备角色时，服务端出现500异常)
        subject.checkRole("admin");
        req.getRequestDispatcher("/WEB-INF/jsp/hasRole.jsp").forward(req, resp);
    }

}
