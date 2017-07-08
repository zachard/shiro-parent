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

package com.zachard.shiro.realm.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import com.zachard.shiro.realm.model.User;

/**
 * 用户对密码进行处理的工具类
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class PasswordHelper {
	
	/**
	 * 用于对密码进行加密的对象
	 */
	private static final PasswordHelper passwordHelper = new PasswordHelper();
	
	/**
	 * 用于生成随机数的对象
	 */
	private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	/**
	 * 算法名称
	 */
    private static final String algorithmName = "md5";
    
    /**
     * 哈希次数
     */
    private static final int hashIterations = 2;
    
    /**
     * 私有化构造器并通过饿汉方法保证单例
     */
    private PasswordHelper() {
    	
    }
    
    /**
     * 获取密码加密对象
     * <p>通过饿汉单例方式获得</p>
     * 
     * @return  密码加密对象
     */
    public static PasswordHelper getInstance() {
    	return passwordHelper;
    }

    /**
     * 对用户密码加密的方法
     * <p>用户的加密salt在方法中随机生成</p>
     * 
     * @param user   用户
     */
    public static void encryptPassword(User user) {

        user.setSalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }

}
