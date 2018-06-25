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

import java.util.Set;

import com.zachard.shiro.realm.model.User;

/**
 * 用户业务操作类
 * <p>定义了创建用户,修改密码,为用户关联/解绑角色<br/>
 * 及查找用户及用户权限的方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public interface UserService {
	
	/**
	 * 创建用户
	 * 
	 * @param user   需要创建的用户
	 * @return       创建完成的用户
	 */
    User createUser(User user);

    /**
     * 修改用户密码
     * 
     * @param userId       用户id
     * @param newPassword  新密码
     */
    void changePassword(Long userId, String newPassword);

    /**
     * 为用户添加多个角色
     * 
     * @param userId    用户id
     * @param roleIds   角色id列表
     */
    void correlationRoles(Long userId, Long... roleIds);
    
    /**
     * 批量删除用户拥有的角色
     * 
     * @param userId    用户id
     * @param roleIds   角色id列表
     */
    void uncorrelationRoles(Long userId, Long... roleIds);

    /**
     * 根据用户名查找用户
     * 
     * @param username   用户名
     * @return           用户
     */
    User findByUsername(String username);

    /**
     * 根据用户名查找所拥有的角色
     * 
     * @param username   用户名
     * @return           用户角色列表
     */
    public Set<String> findRoles(String username);

    /**
     * 根据用户名查找用户权限
     * 
     * @param username    用户名
     * @return            用户权限列表
     */
    Set<String> findPermissions(String username);

}
