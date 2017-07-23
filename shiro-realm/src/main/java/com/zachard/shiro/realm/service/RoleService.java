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

package com.zachard.shiro.realm.service;

import com.zachard.shiro.realm.model.Role;

/**
 * 角色操作业务类
 * <p>定义了角色相关的增删查改方法及<br/>
 * 角色与权限关联和解绑的方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public interface RoleService {
	
	/**
	 * 创建角色对象
	 * 
	 * @param role   需要创建的角色对象
	 * @return       创建完成的角色对象
	 */
	Role createRole(Role role);
	
	/**
	 * 根据角色id删除角色
	 * 
	 * @param roleId   需要删除的角色id
	 */
    void deleteRole(Long roleId);

    /**
     * 将角色与多个权限进行关联
     * 
     * @param roleId        角色id
     * @param permissionIds  权限id列表
     */
    void correlationPermissions(Long roleId, Long... permissionIds);

    /**
     * 批量移除角色所拥有的权限
     * 
     * @param roleId        角色id
     * @param permissionIds 权限id列表
     */
    void uncorrelationPermissions(Long roleId, Long... permissionIds);

}
