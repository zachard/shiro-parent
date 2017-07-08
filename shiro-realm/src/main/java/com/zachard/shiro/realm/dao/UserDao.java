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

import java.util.Set;

import com.zachard.shiro.realm.model.User;

/**
 * 用户相关的DAO层类
 * <p>定义了创建用户,删除用户,更新用户<br/>
 * 为用户添加/删除角色,查找用户及其角色权限等方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public interface UserDao {
	
	/**
	 * 创建用户
	 * 
	 * @param user   需要创建的用户
	 * @return       创建后的用户
	 */
	User createUser(User user);
	
	/**
	 * 更新用户
	 * 
	 * @param user   需要更新的用户
	 */
    void updateUser(User user);
    
    /**
     * 根据用户id删除用户
     * 
     * @param userId  用户id
     */
    void deleteUser(Long userId);

    /**
     * 为用户批量添加角色
     * 
     * @param userId    用户id
     * @param roleIds   角色列表
     */
    void correlationRoles(Long userId, Long... roleIds);
    
    /**
     * 为用户批量删除角色
     * 
     * @param userId    用户id
     * @param roleIds   角色id列表
     */
    void uncorrelationRoles(Long userId, Long... roleIds);

    /**
     * 根据用户id查找用户
     * 
     * @param userId    用户id
     * @return          用户
     */
    User findOne(Long userId);

    /**
     * 根据用户名称查找用户
     * 
     * @param username    用户名称
     * @return            用户
     */
    User findByUsername(String username);

    /**
     * 根据用户名称查找用户拥有的角色
     * 
     * @param username    用户名称
     * @return            用户角色列表
     */
    Set<String> findRoles(String username);

    /**
     * 根据用户名称查找用户权限列表
     * 
     * @param username    用户名称
     * @return            用户权限列表
     */
    Set<String> findPermissions(String username);

}
