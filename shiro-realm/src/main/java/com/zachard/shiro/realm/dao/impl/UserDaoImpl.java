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

package com.zachard.shiro.realm.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.zachard.shiro.realm.dao.UserDao;
import com.zachard.shiro.realm.model.User;
import com.zachard.shiro.realm.util.JdbcTemplateFactory;

/**
 * 用户对象DAO层操作实现类
 * <p>用户对象增删查改方法实现</p>
 *
 * @author zachard
 * @version 1.0.0
 */
@SuppressWarnings("all")
public class UserDaoImpl implements UserDao {
	
	/**
	 * 日记记录器
	 */
	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
	
	/**
	 * JdbcTemplate对象,用于数据库操作
	 */
	private JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplateInstance();
	
	/**
	 * 创建用户的SQL语句
	 */
	protected static final String CREATE_USER = "insert into sys_users(username, password, salt, locked) values(?, ?, ?, ?) ";
	
	/**
	 * 更新用户的SQL语句
	 */
	protected static final String UPDATE_USER = "update sys_users set username = ?, password = ?, salt = ?, locked = ? where id = ? ";
	
	/**
	 * 根据用户id删除用户与角色关联的SQL语句
	 */
	protected static final String DELETE_USER_ROLE_BY_USER_ID = "delete from sys_users_roles where user_id = ? ";
	
	/**
	 * 根据用户id删除用户的SQL语句
	 */
	protected static final String DELETE_USER_BY_ID = "delete from sys_users where id = ? ";
	
	/**
	 * 为用户新增角色的SQL语句
	 */
	protected static final String CORRELE_USER_ROLE = "insert into sys_users_roles(user_id, role_id) values(?, ?) ";
	
	/**
	 * 为用户删除角色的SQL语句
	 */
	protected static final String UNCORRELE_USER_ROLE = "delete from sys_users_roles where user_id = ? and role_id = ? ";
	
	/**
	 * 根据用户id查询用户的SQL语句
	 */
	protected static final String FIND_USER_BY_ID = "select id, username, password, salt, locked from sys_users where id = ? ";
	
	/**
	 * 根据用户名查询用户的SQL语句
	 */
	protected static final String FIND_USER_BY_NAME = "select id, username, password, salt, locked from sys_users where username = ? ";
	
	/**
	 * 根据用户名查询用户拥有角色的SQL语句
	 */
	protected static final String FIND_ROLES_BY_USERNAME = "select role from sys_users u, sys_roles r,sys_users_roles ur where u.username = ? and u.id = ur.user_id and r.id = ur.role_id";
	
	/**
	 * 根据用户名查询用户拥有权限的SQL语句
	 */
	protected static final String FIND_PERMISSION_BY_USERNAME = "select permission from sys_users u, sys_roles r, sys_permissions p, sys_users_roles ur, "
			+ " sys_roles_permissions rp where u.username = ? and u.id = ur.user_id and r.id = ur.role_id and r.id = rp.role_id and p.id = rp.permission_id";
	
	/**
	 * 根据用户id及角色id判断是否用户是否关联角色的SQL语句
	 */
	protected static final String IF_EXIST_USER_ROLE = "select count(1) from sys_users_roles where user_id = ? and role_id = ? ";

	/**
	 * 创建用户
	 * 
	 * @param user   需要创建的用户
	 * @return       创建后的用户
	 */
	@Override
	public User createUser(User user) {
		/*
		 * 用于接收JDBC执行时自动生成的id
		 * 在添加自动生成的id之前，会将上一次添加的清空
		 * 如果为实体类域，则可能导致并发问题
		 */
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement preparedStatement = con.prepareStatement(CREATE_USER, new String[] {"id"});
				preparedStatement.setString(1, user.getUsername());
				preparedStatement.setString(2, user.getPassword());
				preparedStatement.setString(3, user.getSalt());
				preparedStatement.setBoolean(4, user.getLocked());
				
				return preparedStatement;
			}
		}, keyHolder);
		
		user.setId(keyHolder.getKey().longValue());
        return user;
	}

	/**
	 * 更新用户
	 * 
	 * @param user   需要更新的用户
	 */
	@Override
	public void updateUser(User user) {
		jdbcTemplate.update(UPDATE_USER, user.getUsername(), user.getPassword(), user.getSalt(), user.getLocked(), user.getId());
	}

	/**
     * 根据用户id删除用户
     * 
     * @param userId  用户id
     */
	@Override
	public void deleteUser(Long userId) {
		//先删除用户相关联的角色信息
		jdbcTemplate.update(DELETE_USER_ROLE_BY_USER_ID, userId);
		
		jdbcTemplate.update(DELETE_USER_BY_ID, userId);
	}

	/**
     * 为用户批量添加角色
     * 
     * @param userId    用户id
     * @param roleIds   角色列表
     */
	@Override
	public void correlationRoles(Long userId, Long... roleIds) {
		
		if (ArrayUtils.isEmpty(roleIds)) {
			return;
		}
		
		for (Long roleId : roleIds) {
			
			if (!existUserRole(userId, roleId)) {
				jdbcTemplate.update(CORRELE_USER_ROLE, userId, roleId);
			}
			
		}
		
	}

	/**
     * 为用户批量删除角色
     * 
     * @param userId    用户id
     * @param roleIds   角色id列表
     */
	@Override
	public void uncorrelationRoles(Long userId, Long... roleIds) {
		
		if (ArrayUtils.isEmpty(roleIds)) {
			return;
		}
		
		//不建议进行判断是否存在操作，因为会增加数据库操作，即使不存在进行删除也不会出问题
		for (Long roleId : roleIds) {
			jdbcTemplate.update(UNCORRELE_USER_ROLE, userId, roleId);
		}
		
	}

	/**
     * 根据用户id查找用户
     * 
     * @param userId    用户id
     * @return          用户
     */
	@Override
	public User findOne(Long userId) {
		List<User> users = jdbcTemplate.query(FIND_USER_BY_ID, new BeanPropertyRowMapper(User.class), userId);
		
		if (CollectionUtils.isEmpty(users)) {
			return null;
		}
		
		return users.get(0);
	}

	/**
     * 根据用户名称查找用户
     * 
     * @param username    用户名称
     * @return            用户
     */
	@Override
	public User findByUsername(String username) {
		List<User> users = jdbcTemplate.query(FIND_USER_BY_NAME, new BeanPropertyRowMapper(User.class), username);
		
		if (CollectionUtils.isEmpty(users)) {
			return null;
		}
		
		return users.get(0);
	}

	/**
     * 根据用户名称查找用户拥有的角色
     * 
     * @param username    用户名称
     * @return            用户角色列表
     */
	@Override
	public Set<String> findRoles(String username) {
		return new HashSet<>(jdbcTemplate.queryForList(FIND_ROLES_BY_USERNAME, String.class, username));
	}

	/**
     * 根据用户名称查找用户权限列表
     * 
     * @param username    用户名称
     * @return            用户权限列表
     */
	@Override
	public Set<String> findPermissions(String username) {
		return new HashSet<>(jdbcTemplate.queryForList(FIND_PERMISSION_BY_USERNAME, String.class, username));
	}
	
	/**
	 * 根据用户id及权限id判断用户是否存在指定权限
	 * 
	 * @param userId    用户id
	 * @param roleId    权限id
	 * @return          用户是否存在权限
	 */
	private boolean existUserRole(Long userId, Long roleId) {
		return jdbcTemplate.queryForObject(IF_EXIST_USER_ROLE, Integer.class, userId, roleId) != 0;
	}

}
