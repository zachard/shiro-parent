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

package com.zachard.shiro.hello.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.util.ThreadContext;
import org.junit.After;
import org.junit.BeforeClass;

/**
 * 测试用例的父类，封装测试用例需要的公共方法
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class AbstractTestCase {
	
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
	 * 将线程与Subject对象进行解绑
	 * <p>防止对下次测试产生影响</p>
	 */
	@After
	public void tearDown() {
		ThreadContext.unbindSubject();
	}
	
	/**
	 * 根据用户名及密码登录的函数
	 * 
	 * @param username    用户名
	 * @param password    密码
	 */
	protected void login(String username, String password) {
		/*
		 * 获取Subject对象，会自动绑定到线程
		 * <p>在web环境中，请求结束时，需要解除绑定</p>
		 * <p>测试过当subject为类实例变量时，用户同时访问，不会相互之间影响</p>
		 */
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		subject.login(token);
	}
	
	/**
	 * 通过指定配置文件来初始化Factory对象
	 * <p>将作用域设置为包范围，防止子类需要指定不同配置文件</p>
	 * 
	 * @param configFile    配置文件路径
	 */
	protected static void initFactoryByFile(String configFile) {
		facotry = new IniSecurityManagerFactory(configFile);
		SecurityManager securityManager = facotry.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
	}

}
