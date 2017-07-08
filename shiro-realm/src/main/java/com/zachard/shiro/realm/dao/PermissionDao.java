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

import com.zachard.shiro.realm.model.Permission;

/**
 * 权限对应DAO层类
 * <p>定义了增加及删除权限的方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public interface PermissionDao {
	
	/**
	 * 创建权限
	 * 
	 * @param permission   需要创建的权限
	 * @return             创建完成的权限
	 */
	Permission createPermission(Permission permission);

	/**
	 * 根据权限id删除权限
	 * 
	 * @param permissionId    权限id
	 */
    void deletePermission(Long permissionId);

}
