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

import java.util.Set;

import com.zachard.shiro.realm.dao.UserDao;
import com.zachard.shiro.realm.dao.impl.UserDaoImpl;
import com.zachard.shiro.realm.model.User;
import com.zachard.shiro.realm.service.UserService;
import com.zachard.shiro.realm.util.PasswordHelper;

/**
 * 用户对象业务层实现类
 * <p>实现了创建对象及修改密码的方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class UserServiceImpl implements UserService {
	
	/**
	 * 用户对象DAO层对象
	 * <p>应该通过依赖注入注入</p>
	 */
	private UserDao userDao = new UserDaoImpl();
	
	/**
	 * 创建用户
	 * 
	 * @param user   需要创建的用户
	 * @return       创建完成的用户
	 */
	@Override
	public User createUser(User user) {
		PasswordHelper.encryptPassword(user);
		
        return userDao.createUser(user);
	}

	/**
     * 修改用户密码
     * 
     * @param userId       用户id
     * @param newPassword  新密码
     */
	@Override
	public void changePassword(Long userId, String newPassword) {
		User user =userDao.findOne(userId);
        user.setPassword(newPassword);
        PasswordHelper.encryptPassword(user);
        
        userDao.updateUser(user);
	}

	/**
     * 为用户添加多个角色
     * 
     * @param userId    用户id
     * @param roleIds   角色id列表
     */
	@Override
	public void correlationRoles(Long userId, Long... roleIds) {
		userDao.correlationRoles(userId, roleIds);
	}

	/**
     * 批量删除用户拥有的角色
     * 
     * @param userId    用户id
     * @param roleIds   角色id列表
     */
	@Override
	public void uncorrelationRoles(Long userId, Long... roleIds) {
		userDao.uncorrelationRoles(userId, roleIds);
	}

	/**
     * 根据用户名查找用户
     * 
     * @param username   用户名
     * @return           用户
     */
	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	/**
     * 根据用户名查找所拥有的角色
     * 
     * @param username   用户名
     * @return           用户角色列表
     */
	@Override
	public Set<String> findRoles(String username) {
		return userDao.findRoles(username);
	}

	/**
     * 根据用户名查找用户权限
     * 
     * @param username    用户名
     * @return            用户权限列表
     */
	@Override
	public Set<String> findPermissions(String username) {
		return userDao.findPermissions(username);
	}

}
