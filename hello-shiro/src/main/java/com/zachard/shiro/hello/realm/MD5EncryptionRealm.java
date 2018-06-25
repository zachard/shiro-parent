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
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 验证MD5加密后算法的Realm
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class MD5EncryptionRealm extends AuthorizingRealm {

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
		return null;
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
		String username = "zachard";
		String salt2 = "1942693c93f1f4a7d6405628ab2eb592";
		String password = "7d6ce9b0f271ce47f23ee1017ffa57b5";
		SimpleAuthenticationInfo authorizationInfo = new SimpleAuthenticationInfo(username, password, getName());
		authorizationInfo.setCredentialsSalt(ByteSource.Util.bytes(username + salt2));
		
		return authorizationInfo;
	}

}
