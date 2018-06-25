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

import com.zachard.shiro.realm.model.Permission;

/**
 * 权限操作业务类
 * <p>定义类权限的增删查改等方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public interface PermissionService {
	
	/**
	 * 创建一个权限对象
	 * 
	 * @param permission   需要创建的权限对象
	 * @return             创建好的权限对象
	 */
	Permission createPermission(Permission permission);
	
	/**
	 * 根据权限id删除权限对象
	 * 
	 * @param permissionId    需要删除的权限对象id
	 */
    void deletePermission(Long permissionId);

}
