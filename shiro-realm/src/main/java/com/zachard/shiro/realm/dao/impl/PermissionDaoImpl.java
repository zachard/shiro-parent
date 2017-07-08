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

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.zachard.shiro.realm.dao.PermissionDao;
import com.zachard.shiro.realm.model.Permission;
import com.zachard.shiro.realm.util.JdbcTemplateFactory;

/**
 * 权限对象DAO实现类
 * <p>实现了权限相关的增删查改方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class PermissionDaoImpl implements PermissionDao {
	
	/**
	 * JdbcTemplate对象,用于数据库操作
	 */
	private JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplateInstance();
	
	/**
	 * 创建权限的SQL语句
	 */
	protected static final String CREATE_PERMISSION = "insert into sys_permissions(permission, description, available) values(?, ?, ?) ";

	/**
	 * 根据权限id删除角色与权限关联关系的SQL语句
	 */
	protected static final String DELETE_ROLE_PERMISSION_BY_PERMISSION_ID = "delete from sys_roles_permissions where permission_id = ? ";
	
	/**
	 * 根据权限id删除权限的SQL语句
	 */
	protected static final String DELETE_PERMISSION_BY_ID = "delete from sys_permissions where id = ? ";
	
	/**
	 * 创建权限
	 * 
	 * @param permission   需要创建的权限
	 * @return             创建完成的权限
	 */
	@Override
	public Permission createPermission(Permission permission) {
		/*
		 * 用于接收JDBC执行时自动生成的id
		 * 在添加自动生成的id之前，会将上一次添加的清空
		 * 如果为实体类域，则可能导致并发问题
		 */
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement preparedStatement = con.prepareStatement(CREATE_PERMISSION,  new String[] { "id" });
                preparedStatement.setString(1, permission.getPermission());
                preparedStatement.setString(2, permission.getDescription());
                preparedStatement.setBoolean(3, permission.getAvailable());
                
                return preparedStatement;
			}
		}, keyHolder);
		
		permission.setId(keyHolder.getKey().longValue());
		
		return permission;
	}

	/**
	 * 根据权限id删除权限
	 * 
	 * @param permissionId    权限id
	 */
	@Override
	public void deletePermission(Long permissionId) {
		//先删除角色与权限关联关系
		jdbcTemplate.update(DELETE_ROLE_PERMISSION_BY_PERMISSION_ID, permissionId);
		
		jdbcTemplate.update(DELETE_PERMISSION_BY_ID, permissionId);
	}

}
