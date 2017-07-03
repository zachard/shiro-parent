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
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.zachard.shiro.hello.permission.BitPermission;

/**
 * 自定义访问规则控制
 * <p>推荐使用{@link org.apache.shiro.realm.AuthorizingRealm}<br/>
 * 而非{@link org.apache.shiro.realm.Realm}, 因为前者可以有选择的获取更多的信息</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class RoleRealm extends AuthorizingRealm {

	/**
	 * 根据身份标识信息在潜在的数据源中检索访问控制信息信息
	 * <p>使用此方法时，考虑返回一个{@link SimpleAuthorizationInfo}对象<br/>
	 * 因为它比较适合大多数情况</p>
	 * 
	 * @param  principals  身份标识信息
	 * @return   访问控制信息
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//此处因为模拟数据库操作，权限相关数据应该存储在数据库之中
		SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
		authenticationInfo.addRole("admin");
		authenticationInfo.addRole("developer");
		authenticationInfo.addObjectPermission(new BitPermission("^user^10"));
		authenticationInfo.addObjectPermission(new WildcardPermission("user:*"));
		authenticationInfo.addStringPermission("^website^10");
		authenticationInfo.addStringPermission("website:*");
		
		return authenticationInfo;
	}

	/**
	 * 根据相关的token在数据源中(RDBMS, LDAP等)获取用户的身份信息
	 * 
	 * @param  token   登录token信息
	 * @return 用户身份信息
	 * @see {@link org.apache.shiro.realm.Realm#getAuthenticationInfo(AuthenticationToken)}
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
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
