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

package com.zachard.shiro.hello.permission;

import org.apache.shiro.authz.Permission;

import com.alibaba.druid.util.StringUtils;

/**
 * 用于实现位移方式的权限
 * <p>权限字符串格式: ^资源^权限^实例ID, 以^进行分割<br/>
 * 权限: 0: 所有权限<br/>
 *       1: 新增(二进制: 0001)<br/>
 *       2: 修改(二进制: 0010)<br/>
 *       4: 删除(二进制: 0100)<br/>
 *       8: 查看(二进制: 1000)<br/>
 * 示例: ^user^10: 表示对资源user具有查看及修改权限</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class BitPermission implements Permission {
	
	/**
	 * 资源字符串
	 */
	private String resource;
	
	/**
	 * 权限
	 */
	private int permission;
	
	/**
	 * 资源实例ID
	 */
	private String instance;
	
	/**
	 * 权限带字符串的构造函数
	 * @param permissionStr   权限字符串
	 */
	public BitPermission(String permissionStr) {
		String[] permissionArr = permissionStr.split("\\^");
		
		//因为权限的定义以^开头了，所以这里应该取下标为1
		if (permissionArr.length > 1) {
			resource = permissionArr[1];
		}
		
		//缺省时表示全部
		if (StringUtils.isEmpty(resource)) {
			resource = "*";
		}
		
		//注意下标的取值,此外，int类型值默认即为0，表示所有权限(无需*处理)
		if (permissionArr.length > 2) {
			permission = Integer.valueOf(permissionArr[2]);
		}
		
		if (permissionArr.length > 3) {
			instance = permissionArr[3];
		}
		
		if (StringUtils.isEmpty(instance)) {
			instance = "*";
		}
	}

	/**
	 * 判断当前实例是否包含权限参数指定的权限或是能够访问相应的资源
	 * 
	 * @param p    权限或是资源控制
	 * @return     当实例具有权限参数描述的功能或资源访问时，则为true，否则返回false
	 */
	@Override
	public boolean implies(Permission p) {
		//不属于权限类型，则直接返回false
		if (!(p instanceof BitPermission)) {
			return false;
		}
		
		BitPermission other = (BitPermission) p;
		
		/*
		 * resource不是private，怎么可以直接获取
		 */
		if (!("*".equals(this.resource) || this.resource.equals(other.resource))) {
			return false;
		}
		
		if (!(this.permission == 0 || (this.permission & other.permission) != 0)) {
			return false;
		}
		
		if (!("*".equals(this.instance) || this.instance.equals(other.instance))) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return "BitPermission{" 
				+ "resource='" + resource + "'" 
				+ ", permission='" + permission 
				+ ", instance='" + instance + "'" 
				+ "}";
	}

}
