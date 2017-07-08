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

package com.zachard.shiro.hello.permission;

import java.util.Arrays;
import java.util.Collection;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * 将角色转换为权限<br/>
 * 或者返回该角色拥有的权限
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class CustomRolePermissionResolver implements RolePermissionResolver {

	/**
	 * 将角色字符串转换为权限集合
	 * <p>应该通过角色字符串查询数据库，得到该角色拥有的权限</p>
	 * 
	 * @param  roleString  角色字符串
	 * @return    权限集合
	 */
	@Override
	public Collection<Permission> resolvePermissionsInRole(String roleString) {
		
		if ("admin".equals(roleString)) {
			return Arrays.asList((Permission) new WildcardPermission("menu:*"));
		}
		
		return null;
	}

}
