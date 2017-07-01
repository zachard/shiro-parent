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
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
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
 * 登录认证相关测试类
 * <p>Authenticator认证的实现类为ModularRealmAuthenticator<br/>
 * 并且验证采用不同的认证策略产生的结果</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class AuthenticatorTest {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticatorTest.class);
	private static Factory<SecurityManager> facotry;
	
	/**
	 * 使用shiro预先经过的三个步骤
	 * <p>设置SecurityManager的操作是一个全局操作，只需设置一次
	 * 所以应该在类开始之前(相当于项目启动)</p>
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		initFactoryByFile("classpath:shiro.ini");
	}
	
	/**
	 * 测试配置所有Realm验证通过时才能登录成功
	 * <p>请确保配置文件中securityManager.authenticator.authenticationStrategy<br/>
	 * 属性配置为AllSuccessfulStrategy对象及Realm配置能否让登录通过</p>
	 */
	@Test
	public void testAllSuccessfulStrategy() {
		
		/*
		 * 这里曾经尝试在login方法中返回Subject对象，但是却出现异常(没有配置Realms)
		 * <p>Subject对象只能由SecurityUtils获取，不能将一个Subject对象赋值给另外一个</p>
		 */
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		
		//这里直接写出配置Realm的个数，按照ini配置的文件的方法，应该可以通过SecurityManager方法得到
		int realmNum = 2;
		
		// 得到身份集合，验证是否与配置身份集合个数相等
		PrincipalCollection principalCollection = subject.getPrincipals();
		Assert.assertEquals(realmNum, principalCollection.asList().size());
		logger.info("用户身份标识为: " + subject.getPrincipal());
		subject.logout();
	}
	
	/**
	 * 测试配置所有Realm验证通过时才能登录成功(验证失败的情况)
	 * <p>请确保配置文件中securityManager.authenticator.authenticationStrategy<br/>
	 * 属性配置为AllSuccessfulStrategy对象及Realm配置不会让验证全部通过</p>
	 */
	@Test(expected = UnknownAccountException.class)
	public void testAllSuccessStrategyWithFail() {
		/*
		 * 这里曾经尝试在login方法中返回Subject对象，但是却出现异常(没有配置Realms)
		 * <p>Subject对象只能由SecurityUtils获取，不能将一个Subject对象赋值给另外一个</p>
		 * 
		 */
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		
		//这里直接写出配置Realm的个数，按照ini配置的文件的方法，应该可以通过SecurityManager方法得到
		int realmNum = 2;
		
		// 得到身份集合，验证是否与配置身份集合个数相等
		PrincipalCollection principalCollection = subject.getPrincipals();
		Assert.assertEquals(realmNum, principalCollection.asList().size());
		logger.info("用户身份标识为: " + subject.getPrincipal());
		subject.logout();
	}
	
	/**
	 * 测试配置当第一个Realm验证成功即成功的情况
	 * <p>请确保配置文件中securityManager.authenticator.authenticationStrategy<br/>
	 * 属性配置为FirstSuccessfulStrategy对象及Realm至少有一个能通过<br/>
	 * 由于只有一个匹配的Realm，所以realmNum的数量只能为1</p>
	 */
	@Test
	public void testFirstSuccessfulStrategy() {
		/*
		 * 这里曾经尝试在login方法中返回Subject对象，但是却出现异常(没有配置Realms)
		 * <p>Subject对象只能由SecurityUtils获取，不能将一个Subject对象赋值给另外一个</p>
		 */
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		
		//这里直接写出配置Realm的个数，按照ini配置的文件的方法，应该可以通过SecurityManager方法得到
		int realmNum = 1;
		
		// 得到身份集合，验证是否与配置身份集合个数相等
		PrincipalCollection principalCollection = subject.getPrincipals();
		Assert.assertEquals(realmNum, principalCollection.asList().size());
		logger.info("用户身份标识为: " + subject.getPrincipal());
		subject.logout();
	}
	
	/**
	 * 测试配置当至少一个Realm验证成功即成功的情况
	 * <p>请确保配置文件中securityManager.authenticator.authenticationStrategy<br/>
	 * 属性配置为AtLeastOneSuccessfulStrategy对象及Realm至少有一个能通过<br/>
	 * 由于可能匹配一个或多个Realm,所以需要判断返回的身份信息个数是否大于0</p>
	 */
	@Test
	public void testAtLeastOneSuccessfulStrategy() {
		/*
		 * 这里曾经尝试在login方法中返回Subject对象，但是却出现异常(没有配置Realms)
		 * <p>Subject对象只能由SecurityUtils获取，不能将一个Subject对象赋值给另外一个</p>
		 */
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		
		// 得到身份集合，验证是否与配置身份集合个数相等
		PrincipalCollection principalCollection = subject.getPrincipals();
		Assert.assertTrue(principalCollection.asList().size() > 0);
		logger.info("Realm匹配的个数为: " + principalCollection.asList().size());
		logger.info("用户身份标识为: " + subject.getPrincipal());
		subject.logout();
	}
	
	/**
	 * 测试配置只有一个Realm验证成功即成功的情况
	 * <p>请确保配置文件中securityManager.authenticator.authenticationStrategy<br/>
	 * 属性配置为自定义的OnlyOneSuccessfulStrategy对象及Realm只有一个能通过</p>
	 */
	@Test
	public void testCustomOnlyOneSuccessfulStrategy() {
		/*
		 * 这里曾经尝试在login方法中返回Subject对象，但是却出现异常(没有配置Realms)
		 * <p>Subject对象只能由SecurityUtils获取，不能将一个Subject对象赋值给另外一个</p>
		 */
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		
		// 得到身份集合，验证是否与配置身份集合个数相等
		PrincipalCollection principalCollection = subject.getPrincipals();
		Assert.assertEquals(1, principalCollection.asList().size());
		logger.info("Realm匹配的个数为: " + principalCollection.asList().size());
		logger.info("用户身份标识为: " + subject.getPrincipal());
		subject.logout();
	}
	
	/**
	 * 测试配置当至少两个Realm验证成功才成功的情况
	 * <p>请确保配置文件中securityManager.authenticator.authenticationStrategy<br/>
	 * 属性配置为自定义的AtLeastTwoSuccessfulStrategy对象及Realm至少有两个能通过<br/>
	 * 由于可能匹配两个或多个Realm,所以需要判断返回的身份信息个数是否大于1</p>
	 */
	@Test
	public void testCustomAtLeastTwoSuccessfulStrategy() {
		/*
		 * 这里曾经尝试在login方法中返回Subject对象，但是却出现异常(没有配置Realms)
		 * <p>Subject对象只能由SecurityUtils获取，不能将一个Subject对象赋值给另外一个</p>
		 */
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		
		// 得到身份集合，验证是否与配置身份集合个数相等
		PrincipalCollection principalCollection = subject.getPrincipals();
		Assert.assertTrue(principalCollection.asList().size() > 1);
		logger.info("Realm匹配的个数为: " + principalCollection.asList().size());
		logger.info("用户身份标识为: " + subject.getPrincipal());
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
	
	/**
	 * 通过指定配置文件来初始化Factory对象
	 * 
	 * @param configFile    配置文件路径
	 */
	private static void initFactoryByFile(String configFile) {
		facotry = new IniSecurityManagerFactory(configFile);
		SecurityManager securityManager = facotry.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}
	
	/**
	 * 根据用户名及密码登录的函数
	 * 
	 * @param username    用户名
	 * @param password    密码
	 */
	private void login(String username, String password) {
		/*
		 * 获取Subject对象，会自动绑定到线程
		 * <p>在web环境中，请求结束时，需要解除绑定</p>
		 * <p>测试过当subject为类实例变量时，用户同时访问，不会相互之间影响</p>
		 */
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		subject.login(token);
	}

}
