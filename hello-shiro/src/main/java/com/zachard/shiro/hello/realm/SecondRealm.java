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

package com.zachard.shiro.hello.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;

/**
 * 第二个Realm安全数据源实现
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class SecondRealm implements Realm {

	/**
	 * 获取Realm的名称
	 * 
	 * @return  Realm的名称
	 */
	@Override
	public String getName() {
		return "secondRealm";
	}

	/**
	 * 判断是否支持指定类型的token
	 * <p>这里仅支持用户名密码token类型</p>
	 * 
	 * @param toekn  指定的token类型
	 * @return  是否支持该token类型
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof UsernamePasswordToken;
	}

	/**
	 * 获取token的认证信息
	 * <p>这里只返回简单的用户认证信息</p>
	 * 
	 * @param token  认证token
	 * @return  用户的认证信息
	 */
	@Override
	public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		
		/*
		 * 这里应该调用DAO方法根据用户名查询用户对象
		 */
		String password = new String((char[]) token.getCredentials());
		
		/*
		 * 这里应该判断用户对象是否为null
		 */
		if (!"wang".equals(username)) {
			throw new UnknownAccountException("用户不存在");
		}
		
		/*
		 * 这里应该判断用户对象密码与输入是否匹配
		 */
		if (!"wang".equals(password)) {
			throw new IncorrectCredentialsException("密码输入错误");
		}
		
		return new SimpleAuthenticationInfo(username, password, getName());
	}

}
