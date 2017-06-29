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

package com.zachard.shiro.hello.test.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * shiro权限框架登入登出测试
 * 
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class LoginLogoutTest {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginLogoutTest.class);

	/**
	 * 测试shiro登入登出
	 */
	@Test
	public void testLoginLogout() {
		
		/*
		 * 使用shiro预先经过的三个步骤
		 * <p>设置SecurityManager的操作是一个全局操作，只需设置一次</p>
		 */
		Factory<SecurityManager> facotry = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = facotry.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		
		/*
		 * 获取Subject对象，会自动绑定到线程
		 * <p>在web环境中，请求结束时，需要解除绑定</p>
		 */
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
		
		try {
			//这里采用了IniRealm数据源进行比较用户信息
			subject.login(token);
		} catch (AuthenticationException ae) {
			logger.error("用户登录失败", ae.getMessage());
		}
		
		//判断是否经过认证
		Assert.assertEquals(true, subject.isAuthenticated());
		logger.info("用户的身份标识为: " + subject.getPrincipal());
		subject.logout();
	}

}
