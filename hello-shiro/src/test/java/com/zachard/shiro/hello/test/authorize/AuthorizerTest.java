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
import org.apache.shiro.subject.Subject;
import org.junit.Assert;
import org.junit.Test;

import com.zachard.shiro.hello.test.AbstractTestCase;

/**
 * 测试通过JDBC Realm获取用户的相关身份及权限信息
 * <p>请确保ini配置文件中采用了jdbcRealm的相关配置</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class AuthorizerTest extends AbstractTestCase {

	/**
	 * 测试通过相关配置获取得到的权限
	 * <p>请注意ini文件的配置以及数据库中是否存在对应数据</p>
	 */
	@Test
	public void testIsPermission() {
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		
		//测试默认方式的权限
		Assert.assertTrue(subject.isPermitted("user:update"));
		Assert.assertTrue(subject.isPermitted("website:update"));
		
		//测试自定义方式定义的权限
		Assert.assertTrue(subject.isPermitted("^user^2"));
		Assert.assertTrue(subject.isPermitted("^user^8"));
		Assert.assertTrue(subject.isPermitted("^website^10"));
		
		//测试不存在的权限
		Assert.assertFalse(subject.isPermitted("^user^4"));
		
		//测试通过RolePermissionResolver获得权限
		Assert.assertTrue(subject.isPermitted("menu:view"));
	}

}
