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
 * 用户与权限关系实体类
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class UserRole implements Serializable {
	
	/**
	 * 用户id
	 */
	private Long userId;
	
	/**
	 * 角色id
	 */
	private Long roleId;

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

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
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        
        //判断对象是否相等，总是要先判断是否为同一类型对象
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }

        UserRole userRole = (UserRole) o;

        if (roleId != null ? !roleId.equals(userRole.roleId) : userRole.roleId != null) {
        	return false;
        }
        
        if (userId != null ? !userId.equals(userRole.userId) : userRole.userId != null) {
        	return false;
        }

        return true;
    }

	/**
	 * 计算对象的哈希值
	 */
    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (roleId != null ? roleId.hashCode() : 0);
        return result;
    }

    /**
     * 以字符串格式打印对象
     */
    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }

}
