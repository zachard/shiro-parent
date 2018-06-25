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

package com.zachard.shiro.hello.test.authorize;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zachard.shiro.hello.test.AbstractTestCase;

/**
 * 用户是否拥有角色测试类
 * <p>隐式角色测试类，ini文件中只定义用户与角色之间的关系
 * 不定义角色与权限之间的关系</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class RoleTest extends AbstractTestCase {
	
	private static final Logger logger = LoggerFactory.getLogger(RoleTest.class);

	/**
	 * 测试用户是否具有相应的角色
	 * <p>这个测试是基于ini配置文件进行的测试<br/>
	 * 在测试的时候注意保证[users]配置有效(不能为securityManager配置相应的realm)</p>
	 */
	@Test
	public void testHasRole() {
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		logger.info("用户的身份标识为: " + subject.getPrincipal());
		
		//判断是否拥有某个权限
		Assert.assertTrue(subject.hasRole("admin"));
		
		//判断是否拥有列表中全部权限
		Assert.assertTrue(subject.hasAllRoles(Arrays.asList("admin", "cto")));
		
		//判断是否拥有部分权限
		boolean[] results = subject.hasRoles(Arrays.asList("admin", "cto", "guest"));
		Assert.assertTrue(results[0]);
		Assert.assertTrue(results[1]);
		Assert.assertFalse(results[2]);
	}
	
	/**
	 * 检查用户是否具有某个/某些权限
	 * <p>这个测试是基于ini配置文件进行的测试<br/>
	 * 在测试的时候注意保证[users]配置有效(不能为securityManager配置相应的realm)</p>
	 */
	@Test(expected = UnauthorizedException.class)
	public void testCheckRole() {
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		logger.info("用户的身份标识为: " + subject.getPrincipal());
		
		//检查用户是否具有某个权限(如果有则静默执行完方法，如果没有则抛出异常)
		subject.checkRole("admin");
		
		//检查用户是否具有某些权限(如果都包含这些权限则静默执行完方法，否则抛出异常)
		subject.checkRoles("cto", "guest");
	}

}
