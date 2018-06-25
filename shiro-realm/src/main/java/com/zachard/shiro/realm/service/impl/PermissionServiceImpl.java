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

package com.zachard.shiro.realm.service.impl;

import com.zachard.shiro.realm.dao.PermissionDao;
import com.zachard.shiro.realm.dao.impl.PermissionDaoImpl;
import com.zachard.shiro.realm.model.Permission;
import com.zachard.shiro.realm.service.PermissionService;

/**
 * 权限对象业务层实现类
 * <p>实现增加删除权限的方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class PermissionServiceImpl implements PermissionService {
	
	/**
	 * 权限DAO层对象
	 * <p>应该通过依赖注入方式注入</p>
	 */
	private PermissionDao permissionDao = new PermissionDaoImpl();

	/**
	 * 创建一个权限对象
	 * 
	 * @param permission   需要创建的权限对象
	 * @return             创建好的权限对象
	 */
	@Override
	public Permission createPermission(Permission permission) {
		return permissionDao.createPermission(permission);
	}

	/**
	 * 根据权限id删除权限对象
	 * 
	 * @param permissionId    需要删除的权限对象id
	 */
	@Override
	public void deletePermission(Long permissionId) {
		permissionDao.deletePermission(permissionId);
	}

}
