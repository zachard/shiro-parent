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

package com.zachard.shiro.realm.model;

import java.io.Serializable;

/**
 * 角色与权限关系实体类
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class RolePermssion implements Serializable {
	
	/**
	 * 角色id
	 */
	private Long roleId;
	
	/**
	 * 权限id
	 */
	private Long permissionId;

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the permissionId
	 */
	public Long getPermissionId() {
		return permissionId;
	}

	/**
	 * @param permissionId the permissionId to set
	 */
	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}
	
	/**
	 * 判断对象是否相等
	 * 
	 * @param o 需要比较的对象
	 */
	@Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }

        RolePermssion that = (RolePermssion) o;

        if (permissionId != null ? !permissionId.equals(that.permissionId) : that.permissionId != null) {
        	return false;
        }
        
        if (roleId != null ? !roleId.equals(that.roleId) : that.roleId != null) {
        	return false;
        }

        return true;
    }

	/**
	 * 计算对象的哈希值
	 */
    @Override
    public int hashCode() {
        int result = roleId != null ? roleId.hashCode() : 0;
        result = 31 * result + (permissionId != null ? permissionId.hashCode() : 0);
        return result;
    }

    /**
     * 以字符串打印对象
     */
    @Override
    public String toString() {
        return "RolePermssion{" +
                "roleId=" + roleId +
                ", permissionId=" + permissionId +
                '}';
    }

}
