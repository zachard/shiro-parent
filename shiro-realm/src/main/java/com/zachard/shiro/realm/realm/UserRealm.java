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

package com.zachard.shiro.realm.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.zachard.shiro.realm.model.User;
import com.zachard.shiro.realm.service.UserService;
import com.zachard.shiro.realm.service.impl.UserServiceImpl;

/**
 * 用户Realm对应类
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class UserRealm extends AuthorizingRealm {
	
	/**
	 * 用户业务层对象
	 * <p>应该通过依赖注入</p>
	 */
	private UserService userService = new UserServiceImpl();

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
		String username = (String) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(userService.findRoles(username));
        authorizationInfo.setStringPermissions(userService.findPermissions(username));

        return authorizationInfo;
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
		//根据用户名获取用户对象
		String username = (String) token.getPrincipal();
        User user = userService.findByUsername(username);

        //用户不存在, 注意：采用向上层调用抛异常的方式返回信息
        if(user == null) {
            throw new UnknownAccountException();
        }

        //判断账号是否被锁定
        if(Boolean.TRUE.equals(user.getLocked())) {
            throw new LockedAccountException();
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                getName()
        );
        
        return authenticationInfo;
	}

}
