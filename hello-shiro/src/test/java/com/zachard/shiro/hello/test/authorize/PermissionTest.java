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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zachard.shiro.hello.test.AbstractTestCase;

/**
 * 用户是否拥有权限测试类
 * <p>显示角色测试类，ini配置文件同时维护用户与角色.
 * 角色与权限之间的关系</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class PermissionTest extends AbstractTestCase {
	
	private static final Logger logger = LoggerFactory.getLogger(PermissionTest.class);

	/**
	 * 测试用户是否具有某个权限
	 * <p>这个测试是基于ini配置文件进行的测试<br/>
	 * 在测试的时候注意保证[users]及[roles]配置有效(不能为securityManager配置相应的realm)</p>
	 */
	@Test
	public void testIsPermission() {
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		logger.info("用户身份标识为: " + subject.getPrincipal());
		
		//判断用户是否具有某个权限
		Assert.assertTrue(subject.isPermitted("delete:view"));
		
		//判断用户是否具有某些权限
		Assert.assertTrue(subject.isPermittedAll("user:create", "user:update"));
	}
	
	/**
	 * 检查用户是否具有某个/某些权限
	 * <p>这个测试是基于ini配置文件进行的测试<br/>
	 * 在测试的时候注意保证[users]及[roles]配置有效(不能为securityManager配置相应的realm)</p>
	 */
	@Test(expected = UnauthorizedException.class)
	public void testCheckPermission() {
		login("wang", "wang");
		Subject subject = SecurityUtils.getSubject();
		logger.info("用户身份标识为: " + subject.getPrincipal());
		
		//检查用户是否具有某个权限
		subject.checkPermission("developer");
		
		//检查用户是否具有某些权限
		subject.checkPermissions("user:update", "developer");
		
		//测试不具有权限时，抛出异常
		subject.checkPermission("user:create");
	}

}
