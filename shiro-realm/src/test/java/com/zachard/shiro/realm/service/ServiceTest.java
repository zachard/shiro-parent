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

package com.zachard.shiro.realm.service;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.zachard.shiro.realm.AbstractTestCase;
import com.zachard.shiro.realm.model.Permission;
import com.zachard.shiro.realm.model.Role;
import com.zachard.shiro.realm.model.User;
import com.zachard.shiro.realm.service.impl.PermissionServiceImpl;
import com.zachard.shiro.realm.service.impl.RoleServiceImpl;
import com.zachard.shiro.realm.service.impl.UserServiceImpl;
import com.zachard.shiro.realm.util.JdbcTemplateFactory;

/**
 * The description...
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class ServiceTest extends AbstractTestCase {

	protected PermissionService permissionService = new PermissionServiceImpl();
    protected RoleService roleService = new RoleServiceImpl();
    protected UserService userService = new UserServiceImpl();

    protected String password = "123";

    protected Permission p1;
    protected Permission p2;
    protected Permission p3;
    protected Role r1;
    protected Role r2;
    protected User u1;
    protected User u2;
    protected User u3;
    protected User u4;
    
    /**
     * 准备测试数据
     * <p>先将数据库中数据清除，然后再新建相关数据</p>
     */
    @Before
    public void setUp() {
    	//测试前清空数据
    	JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_users");
    	JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_roles");
    	JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_permissions");
    	JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_users_roles");
    	JdbcTemplateFactory.getJdbcTemplateInstance().update("delete from sys_roles_permissions");
    	
    	//创建三个权限，并添加到数据库
    	p1 = new Permission("user:create", "用户模块新增", Boolean.TRUE);
        p2 = new Permission("user:update", "用户模块修改", Boolean.TRUE);
        p3 = new Permission("menu:create", "菜单模块新增", Boolean.TRUE);
        permissionService.createPermission(p1);
        permissionService.createPermission(p2);
        permissionService.createPermission(p3);
        
        //创建两个用户，并添加到数据库
        r1 = new Role("admin", "管理员", Boolean.TRUE);
        r2 = new Role("user", "用户管理员", Boolean.TRUE);
        roleService.createRole(r1);
        roleService.createRole(r2);
        
        //将角色与对应的角色相关联
        roleService.correlationPermissions(r1.getId(), p1.getId());
        roleService.correlationPermissions(r1.getId(), p2.getId());
        roleService.correlationPermissions(r1.getId(), p3.getId());
        roleService.correlationPermissions(r2.getId(), p1.getId());
        roleService.correlationPermissions(r2.getId(), p2.getId());

        //新增用户，并添加到数据库
        u1 = new User("zhang", password);
        u2 = new User("li", password);
        u3 = new User("wu", password);
        u4 = new User("wang", password);
        u4.setLocked(Boolean.TRUE);
        userService.createUser(u1);
        userService.createUser(u2);
        userService.createUser(u3);
        userService.createUser(u4);
        
        //将用户与角色关联
        userService.correlationRoles(u1.getId(), r1.getId());
    }
    
    /**
     * 测试用户，角色，权限之间的关系
     */
    @Test
    public void testUserRolePermissionRelation() {
    	//判断用户是否拥有
        Set<String> roles = userService.findRoles(u1.getUsername());
        Assert.assertEquals(1, roles.size());
        Assert.assertTrue(roles.contains(r1.getRole()));

        //判断用户是否拥有权限
        Set<String> permissions = userService.findPermissions(u1.getUsername());
        Assert.assertEquals(3, permissions.size());
        Assert.assertTrue(permissions.contains(p3.getPermission()));

        //判断用户是否拥有角色及权限
        roles = userService.findRoles(u2.getUsername());
        Assert.assertEquals(0, roles.size());
        permissions = userService.findPermissions(u2.getUsername());
        Assert.assertEquals(0, permissions.size());
        
        //解除角色与权限之间的关联关系
        roleService.uncorrelationPermissions(r1.getId(), p3.getId());
        permissions = userService.findPermissions(u1.getUsername());
        Assert.assertEquals(2, permissions.size());
        Assert.assertFalse(permissions.contains(p3.getPermission()));

        //将权限从角色中删除
        permissionService.deletePermission(p2.getId());
        permissions = userService.findPermissions(u1.getUsername());
        Assert.assertEquals(1, permissions.size());

        //解除用户与角色之间的关联关系
        userService.uncorrelationRoles(u1.getId(), r1.getId());
        roles = userService.findRoles(u1.getUsername());
        Assert.assertEquals(0, roles.size());
    }

}
