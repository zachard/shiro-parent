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

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * 用于将字符串转换为权限Permission对象
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class BitAndWildPermissionResolver implements PermissionResolver {

	/**
	 * 将字符串转换为Permission对象
	 * 
	 * @param     permissionString  字符串对象
	 * @return    权限对象
	 */
	@Override
	public Permission resolvePermission(String permissionString) {
		
		if (permissionString.startsWith("^")) {
			return new BitPermission(permissionString);
		}
		
		return new WildcardPermission(permissionString);
	}

}
