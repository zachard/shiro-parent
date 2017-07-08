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

package com.zachard.shiro.realm.dao;

import com.zachard.shiro.realm.model.Role;

/**
 * 角色相关的DAO层类
 * <p>定义了增加角色,删除角色<br/>
 * 为角色关联/删除权限的方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public interface RoleDao {
	
	/**
	 * 创建角色
	 * 
	 * @param role    需要创建的角色
	 * @return        创建完成的角色
	 */
	Role createRole(Role role);
	
	/**
	 * 根据角色id删除角色
	 * 
	 * @param roleId    角色id
	 */
    void deleteRole(Long roleId);

    /**
     * 批量为用户增加权限
     * 
     * @param roleId    角色id
     * @param permissionIds    权限id列表
     */
    void correlationPermissions(Long roleId, Long... permissionIds);
    
    /**
     * 批量删除角色拥有的权限
     * 
     * @param roleId    角色id
     * @param permissionIds    权限id列表
     */
    void uncorrelationPermissions(Long roleId, Long... permissionIds);

}
