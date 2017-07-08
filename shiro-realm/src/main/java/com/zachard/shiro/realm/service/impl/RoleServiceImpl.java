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

import com.zachard.shiro.realm.dao.RoleDao;
import com.zachard.shiro.realm.dao.impl.RoleDaoImpl;
import com.zachard.shiro.realm.model.Role;
import com.zachard.shiro.realm.service.RoleService;

/**
 * 角色对象业务层实现类
 * <p>实现了角色对象增加/删除/操作权限的方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class RoleServiceImpl implements RoleService {
	
	/**
	 * 角色DAO层操纵对象
	 * <p>应该通过依赖注入方式注入</p>
	 */
	private RoleDao roleDao = new RoleDaoImpl();

	/**
	 * 创建角色对象
	 * 
	 * @param role   需要创建的角色对象
	 * @return       创建完成的角色对象
	 */
	@Override
	public Role createRole(Role role) {
		return roleDao.createRole(role);
	}

	/**
	 * 根据角色id删除角色
	 * 
	 * @param roleId   需要删除的角色id
	 */
	@Override
	public void deleteRole(Long roleId) {
		roleDao.deleteRole(roleId);
	}

	/**
     * 将角色与多个权限进行关联
     * 
     * @param roleId        角色id
     * @param permissionIds  权限id列表
     */
	@Override
	public void correlationPermissions(Long roleId, Long... permissionIds) {
		roleDao.correlationPermissions(roleId, permissionIds);
	}

	/**
     * 批量移除角色所拥有的权限
     * 
     * @param roleId        角色id
     * @param permissionIds 权限id列表
     */
	@Override
	public void uncorrelationPermissions(Long roleId, Long... permissionIds) {
		roleDao.uncorrelationPermissions(roleId, permissionIds);
	}

}
