package com.config;

import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import javax.annotation.Resource;

/**
 * @author songbin
 * @date 2020/10/9
 */
public class UserRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //拿到当前用户
        Subject currentUser = SecurityUtils.getSubject();

        User user = (User)currentUser.getPrincipal();
        //获取用户权限
        info.addStringPermission("user:admin");
        return info;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了认证");
        UsernamePasswordToken userToken = (UsernamePasswordToken)token;
        User user = userService.findUserByUsername(((UsernamePasswordToken) token).getUsername());
        System.out.println(user);
        if (user==null){
            return null;  //抛出异常  UnknownAccountException
        }

        //密码认证   shiro做
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
