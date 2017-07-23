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

package com.zachard.shiro.realm.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zachard.shiro.common.test.AbstractTestCase;
import com.zachard.shiro.realm.model.Permission;
import com.zachard.shiro.realm.model.Role;
import com.zachard.shiro.realm.model.User;
import com.zachard.shiro.realm.service.PermissionService;
import com.zachard.shiro.realm.service.RoleService;
import com.zachard.shiro.realm.service.UserService;
import com.zachard.shiro.realm.service.impl.PermissionServiceImpl;
import com.zachard.shiro.realm.service.impl.RoleServiceImpl;
import com.zachard.shiro.realm.service.impl.UserServiceImpl;
import com.zachard.shiro.realm.util.JdbcTemplateFactory;

/**
 * {@link UserRealm} 测试类
 * <p>
 * </p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class UserRealmTest extends AbstractTestCase {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRealmTest.class);

	protected PermissionService permissionService = new PermissionServiceImpl();
	protected RoleService roleService = new RoleServiceImpl();
	protected UserService userService = new UserServiceImpl();

	protected String password = "123";

	protected Permission p1;
	protected Permission p2;
	protected Permission p3;
	protected Role r1;
	protected Role r2;
	protected User u1;
	protected User u2;
	protected User u3;
	protected User u4;

	/**
	 * 准备测试数据
	 * <p>
	 * 先将数据库中数据清除，然后再新建相关数据
	 * </p>
	 */
	@Before
	public void setUp() {
		// 测试前清空数据
		JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_users");
		JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_roles");
		JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_permissions");
		JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_users_roles");
		JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_roles_permissions");

		// 创建三个权限，并添加到数据库
		p1 = new Permission("user:create", "用户模块新增", Boolean.TRUE);
		p2 = new Permission("user:update", "用户模块修改", Boolean.TRUE);
		p3 = new Permission("menu:create", "菜单模块新增", Boolean.TRUE);
		permissionService.createPermission(p1);
		permissionService.createPermission(p2);
		permissionService.createPermission(p3);

		// 创建两个用户，并添加到数据库
		r1 = new Role("admin", "管理员", Boolean.TRUE);
		r2 = new Role("user", "用户管理员", Boolean.TRUE);
		roleService.createRole(r1);
		roleService.createRole(r2);

		// 将角色与对应的角色相关联
		roleService.correlationPermissions(r1.getId(), p1.getId());
		roleService.correlationPermissions(r1.getId(), p2.getId());
		roleService.correlationPermissions(r1.getId(), p3.getId());
		roleService.correlationPermissions(r2.getId(), p1.getId());
		roleService.correlationPermissions(r2.getId(), p2.getId());

		// 新增用户，并添加到数据库
		u1 = new User("zhang", password);
		u2 = new User("li", password);
		u3 = new User("wu", password);
		u4 = new User("wang", password);
		u4.setLocked(Boolean.TRUE);
		userService.createUser(u1);
		userService.createUser(u2);
		userService.createUser(u3);
		userService.createUser(u4);

		// 将用户与角色关联
		userService.correlationRoles(u1.getId(), r1.getId());
	}

	/**
	 * 测试用户成功登录
	 */
	@Test
	public void testLoginSuccess() {
		login(u1.getUsername(), password);
		Assert.assertTrue(SecurityUtils.getSubject().isAuthenticated());
	}

	/**
	 * 测试不存在的用户登录失败
	 */
	@Test(expected = UnknownAccountException.class)
	public void testLoginFailWithUnknownUsername() {
		login(u1.getUsername() + "1", password);
	}

	/**
	 * 测试用户密码错误登录失败
	 */
	@Test(expected = IncorrectCredentialsException.class)
	public void testLoginFailWithErrorPassowrd() {
		login(u1.getUsername(), password + "1");
	}

	/**
	 * 测试用户账户锁定登录失败
	 */
	@Test(expected = LockedAccountException.class)
	public void testLoginFailWithLocked() {
		login(u4.getUsername(), password + "1");
	}

	/**
	 * 测试5次密码输入错误
	 */
	@Test(expected = ExcessiveAttemptsException.class)
	public void testLoginFailWithLimitRetryCount() {
		
		//将这里的5改小进行测试，会出现 IncorrectCredentialsException(测试不通过)
		for (int i = 1; i <= 5; i++) {
			try {
				login(u3.getUsername(), password + "1");
			} catch (Exception e) {
				logger.error("密码错误", e.getMessage());
			}
		}
		
		login(u3.getUsername(), password + "1");

		// 需要清空缓存，否则后续的执行就会遇到问题(或者使用一个全新账户测试)
	}

	/**
	 * 测试用户是否拥有角色
	 */
	@Test
	public void testHasRole() {
		login(u1.getUsername(), password);
		Assert.assertTrue(SecurityUtils.getSubject().hasRole("admin"));
	}

	/**
	 * 测试用户不具有某权限
	 */
	@Test
	public void testNoRole() {
		login(u2.getUsername(), password);
		Assert.assertFalse(SecurityUtils.getSubject().hasRole("admin"));
	}

	/**
	 * 测试用户拥有某权限
	 */
	@Test
	public void testHasPermission() {
		login(u1.getUsername(), password);
		Assert.assertTrue(SecurityUtils.getSubject().isPermittedAll("user:create", "menu:create"));
	}

	/**
	 * 测试用户不具备某权限
	 */
	@Test
	public void testNoPermission() {
		login(u2.getUsername(), password);
		Assert.assertFalse(SecurityUtils.getSubject().isPermitted("user:create"));
	}

}
