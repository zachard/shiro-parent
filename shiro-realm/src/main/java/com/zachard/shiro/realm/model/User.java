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
 * 用户对象实体类
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
@SuppressWarnings({ "serial" })
public class User implements Serializable {
	
	/**
	 * 用户id
	 */
	private Long id;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 用户密码
	 */
	private String password;
	
	/**
	 * 用户加密的盐
	 */
	private String salt;
	
	/**
	 * 用户是否被锁定
	 * <p>默认值为{@link Boolean.<code>false</code>}</p>
	 */
	private Boolean locked = Boolean.FALSE;
	
	/**
	 * 用户对象默认构造器
	 */
	public User() {
		
	}
	
	/**
	 * 用户对象含参数构造器
	 * 
	 * @param username    用户名
	 * @param password    密码
	 */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Boolean getLocked() {
		return locked;
	}
	
	public String getCredentialsSalt() {
		return username + salt;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	
	/**
	 * 比较两个对象是否相等的方法
	 * 
	 * @param  需要比较的对象
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		User user = (User) o;
		
		if (id != null ? !id.equals(user.id) : user.id != null) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 计算对象的哈希值
	 */
	@Override
	public int hashCode() {
		
		//相对而言，采用非Java8方式更简洁
//		if (Optional.ofNullable(id).isPresent()) {
//			return id.hashCode();
//		}
//		
//		return 0;
		
		return id != null ? id.hashCode() : 0;
	}
	
	/**
	 * 以字符串格式显示对象
	 */
	@Override
	public String toString() {
		return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", locked=" + locked +
                '}';
	}

}
