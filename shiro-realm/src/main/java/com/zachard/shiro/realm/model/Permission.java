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
 * 权限实体类
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
@SuppressWarnings("serial")
public class Permission implements Serializable {
	
	/**
	 * 权限id
	 */
	private Long id;
	
	/**
	 * 权限标识
	 * <p>例如: "user:create"</p>
	 */
    private String permission;
    
    /**
     * 权限描述
     */
    private String description;
    
    /**
     * 权限是否可用
     * <p>默认值为{@link Boolean.<code>false</code>}</p>
     */
    private Boolean available = Boolean.FALSE;

    /**
     * 默认构造器
     */
	public Permission() {
		super();
	}

	/**
	 * 带参数构造器
	 * 
	 * @param permission    权限标识
	 * @param description   权限描述
	 * @param available     权限是否可用
	 */
	public Permission(String permission, String description, Boolean available) {
		super();
		this.permission = permission;
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
	 * @return the permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param permission the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
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
	 * 判断对象是否相等
	 * 
	 * @param o  需要判断的对象
	 */
	@Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }

        Permission role = (Permission) o;

        if (id != null ? !id.equals(role.id) : role.id != null) {
        	return false;
        }

        return true;
    }

	/**
	 * 生成对象的哈希值
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
                ", permission='" + permission + '\'' +
                ", description='" + description + '\'' +
                ", available=" + available +
                '}';
    }

}
