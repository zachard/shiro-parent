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

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.zachard.shiro.realm.dao.RoleDao;
import com.zachard.shiro.realm.model.Role;
import com.zachard.shiro.realm.util.JdbcTemplateFactory;

/**
 * 角色对象DAO层操作实现类
 * <p>角色对象增删查改等方法的实现</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class RoleDaoImpl implements RoleDao {
	
	/**
	 * JdbcTemplate对象,用于数据库操作
	 */
	private JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplateInstance();
	
	/**
	 * 创建角色的SQL语句
	 */
	protected static final String CREATE_ROLE = "insert into sys_roles(role, description, available) values(?, ?, ?) ";
	
	/**
	 * 根据角色id删除用户与角色相关联的关系SQL语句
	 */
	protected static final String DELETE_USER_ROLE_BY_ROLE_ID = "delete from sys_users_roles where role_id = ? ";
	
	/**
	 * 根据角色id删除角色与权限的关联关系SQL语句
	 */
	protected static final String DELETE_ROLE_PERMISSION_BY_ROLE_ID = "delete from sys_roles_permissions where role_id = ? ";
	
	/**
	 * 根据id删除角色的SQL语句
	 */
	protected static final String DELETE_ROLE_BY_ID = "delete from sys_roles where id = ? ";
	
	/**
	 * 将角色与权限相关联的SQL语句
	 */
	protected static final String CORRELE_ROLE_PERMISSION = "insert into sys_roles_permissions(role_id, permission_id) values(?, ?) ";

	/**
	 * 删除角色与权限关联关系的SQL语句
	 */
	protected static final String UNCORRELE_ROLE_PERMISSION = "delete from sys_roles_permissions where role_id = ? and permission_id = ? ";
	
	/**
	 * 根据角色id及权限id查询权限是否存在的SQL语句
	 */
	protected static final String IF_EXIST_ROLE_PERMISSION = "select count(1) from sys_roles_permissions where role_id = ? and permission_id = ? ";
	
	/**
	 * 创建角色
	 * 
	 * @param role    需要创建的角色
	 * @return        创建完成的角色
	 */
	@Override
	public Role createRole(Role role) {
		/*
		 * 用于接收JDBC执行时自动生成的id
		 * 在添加自动生成的id之前，会将上一次添加的清空
		 * 如果为实体类域，则可能导致并发问题
		 */
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement preparedStatement = con.prepareStatement(CREATE_ROLE, new String[] { "id" });
                preparedStatement.setString(1, role.getRole());
                preparedStatement.setString(2, role.getDescription());
                preparedStatement.setBoolean(3, role.getAvailable());
                
                return preparedStatement;
			}
		}, keyHolder);
		
		role.setId(keyHolder.getKey().longValue());
		
		return role;
	}

	/**
	 * 根据角色id删除角色
	 * 
	 * @param roleId    角色id
	 */
	@Override
	public void deleteRole(Long roleId) {
		//先删除与角色相关联的用户及权限信息
		jdbcTemplate.update(DELETE_USER_ROLE_BY_ROLE_ID, roleId);
		jdbcTemplate.update(DELETE_ROLE_PERMISSION_BY_ROLE_ID, roleId);
		
		//删除角色信息
		jdbcTemplate.update(DELETE_ROLE_BY_ID, roleId);
	}

	/**
     * 批量为用户增加权限
     * 
     * @param roleId    角色id
     * @param permissionIds    权限id列表
     */
	@Override
	public void correlationPermissions(Long roleId, Long... permissionIds) {
		
		if (ArrayUtils.isEmpty(permissionIds)) {
			return;
		}
		
		for (Long permissionId : permissionIds) {
			
			if (!existRolePermission(roleId, permissionId)) {
				jdbcTemplate.update(CORRELE_ROLE_PERMISSION, roleId, permissionId);
			}
			
		}
		
	}

	/**
     * 批量删除角色拥有的权限
     * 
     * @param roleId    角色id
     * @param permissionIds    权限id列表
     */
	@Override
	public void uncorrelationPermissions(Long roleId, Long... permissionIds) {
		
		if (ArrayUtils.isEmpty(permissionIds)) {
			return;
		}
		
		//不建议进行判断是否存在，会增加数据库操作
		for (Long permissionId : permissionIds) {
			jdbcTemplate.update(UNCORRELE_ROLE_PERMISSION, roleId, permissionId);
		}
		
	}
	
	/**
	 * 根据角色id及权限id查询角色是否具有该权限
	 * 
	 * @param roleId         角色id
	 * @param permissionId   权限id
	 * @return               角色是否具有该权限
	 */
	private boolean existRolePermission(Long roleId, Long permissionId) {
		return jdbcTemplate.queryForObject(IF_EXIST_ROLE_PERMISSION, Integer.class, roleId, permissionId) != 0;
	}

}
