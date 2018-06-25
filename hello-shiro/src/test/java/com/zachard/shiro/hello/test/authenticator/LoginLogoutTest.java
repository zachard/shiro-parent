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

package com.zachard.shiro.hello.test.authenticator;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
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
	private static Factory<SecurityManager> facotry;
	
	/**
	 * 使用shiro预先经过的三个步骤
	 * <p>设置SecurityManager的操作是一个全局操作，只需设置一次
	 * 所以应该在类开始之前(相当于项目启动)</p>
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		facotry = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = facotry.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}
	
	/**
	 * 测试shiro登入登出
	 */
	@Test
	public void testLoginLogout() {
		
		/*
		 * 获取Subject对象，会自动绑定到线程
		 * <p>在web环境中，请求结束时，需要解除绑定</p>
		 * <p>测试过当subject为类实例变量时，用户同时访问，不会相互之间影响</p>
		 */
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("zachard", "admin");
		
		try {
			subject.login(token);
		} catch (AuthenticationException ae) {
			logger.error("用户登录失败" + ae.getMessage());
		}
		
		//判断是否经过认证
		Assert.assertEquals(true, subject.isAuthenticated());
		logger.info("用户的身份标识为: " + subject.getPrincipal());
		subject.logout();
	}
	
	/**
	 * 测试SecutiryManager配置一个Realm的登录验证情况
	 * <p>当为SecurityManager配置Realm时，登录过程中会通过 <br/>
	 * {@link org.apache.shiro.realm.Realm#getAuthenticationInfo}来验证用户名及密码是否正确 <br/>
	 * 当配置Realm之后, ini配置文件中的[users]配置自动失效</p>
	 */
	@Test
	public void testLoginBySingleRealm() {
		
		/*
		 * 获取Subject对象，会自动绑定到线程
		 * <p>在web环境中，请求结束时，需要解除绑定</p>
		 * <p>测试过当subject为类实例变量时，用户同时访问，不会相互之间影响</p>
		 */
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("zachard", "admin");
		
		try {
			subject.login(token);
		} catch (AuthenticationException ae) {
			logger.error("用户登录失败" + ae.getMessage());
		}
		
		//判断是否经过认证
		Assert.assertEquals(true, subject.isAuthenticated());
		logger.info("用户的身份标识为: " + subject.getPrincipal());
		subject.logout();
	}
	
	/**
	 * 测试SecurityManager配置多个Realm的登录验证
	 * <p>当配置多个Realm时，会按照配置的顺序验证</p>
	 */
	@Test
	public void testLoginByMultiRealm() {
		/*
		 * 获取Subject对象，会自动绑定到线程
		 * <p>在web环境中，请求结束时，需要解除绑定</p>
		 * <p>测试过当subject为类实例变量时，用户同时访问，不会相互之间影响</p>
		 */
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("wang", "wang123");
		
		try {
			subject.login(token);
		} catch (AuthenticationException ae) {
			logger.error("用户登录失败" + ae.getMessage());
		}
		
		//判断是否经过认证
		Assert.assertEquals(false, subject.isAuthenticated());
		logger.info("用户的身份标识为: " + subject.getPrincipal());
		subject.logout();
	}
	
	/**
	 * 测试通过JDBCRealm连接数据库验证登录信息，这里采用shiro默认的JdbcRealm实现
	 * <p>JdbcRealm默认会查数据源中的users，user_roles及roles_permissions表<br/>
	 * 获取相关认证及权限信息</p>
	 */
	@Test
	public void testLoginByJDBCRealm() {
		/*
		 * 获取Subject对象，会自动绑定到线程
		 * <p>在web环境中，请求结束时，需要解除绑定</p>
		 * <p>测试过当subject为类实例变量时，用户同时访问，不会相互之间影响</p>
		 */
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken("zachard", "admin");
		
		try {
			subject.login(token);
		} catch (AuthenticationException ae) {
			logger.error("用户登录失败" + ae.getMessage());
		}
		
		//判断是否经过认证
		Assert.assertEquals(true, subject.isAuthenticated());
		logger.info("用户的身份标识为: " + subject.getPrincipal());
		subject.logout();
	}
	
	/**
	 * 将线程与Subject对象进行解绑
	 * <p>防止对下次测试产生影响</p>
	 */
	@After
	public void tearDown() {
		ThreadContext.unbindSubject();
	}

}
