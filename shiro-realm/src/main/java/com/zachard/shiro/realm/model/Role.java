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
 * 角色实体类
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class Role implements Serializable {
	
	/**
	 * 角色id
	 */
	private Long id;
	
	/**
	 * 角色标识
	 */
	private String role;
	
	/**
	 * 角色描述
	 */
	private String description;
	
	/**
	 * 角色是否可用
	 * <p>默认值为: {@link Boolean.<code>false</code>}</p>
	 */
	private Boolean available = Boolean.FALSE;
	
	/**
	 * 默认构造器
	 */
	public Role() {
		
	}
	
	/**
	 * 带参数构造器
	 * 
	 * @param role         角色标识
	 * @param description  角色描述
	 * @param available    角色是否可用
	 */
	public Role(String role, String description, Boolean available) {
        this.role = role;
        this.description = description;
        this.available = available;
    }

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the available
	 */
	public Boolean getAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	
	/**
	 * 比较两个对象是否相等
	 */
	@Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }

        Role role = (Role) o;

        if (id != null ? !id.equals(role.id) : role.id != null) {
        	return false;
        }

        return true;
    }

	/**
	 * 计算对象的Hash值
	 */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /**
	 * 以字符串格式显示对象
	 */
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                '}';
    }

}
