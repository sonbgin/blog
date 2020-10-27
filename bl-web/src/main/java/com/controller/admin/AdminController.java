package com.controller.admin;

import entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author songbin
 * @date 2020/10/9
 */
@Controller
@RequestMapping({"/admin","/"})
public class AdminController {

    @GetMapping
    public String toLogin(){
        return "block/admin/login";
    }

    @RequestMapping
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session){
        System.out.println("我来了");
        //获取当前用户
        Subject currentUser = SecurityUtils.getSubject();
        System.out.println("用户已认证:"+currentUser.isAuthenticated());

        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username,password);
        try {
            currentUser.login(token);
            session.setAttribute("user", (User)currentUser.getPrincipal());
            System.out.println(currentUser.getPrincipal());
            return "redirect:/admin/toBlogs";
        } catch (UnknownAccountException e) {  //用户名不存在
            session.setAttribute("adminMsg","当前用户不存在");
            return "redirect:/admin";
        }catch (IncorrectCredentialsException e){  //密码不存在
            session.setAttribute("adminMsg","密码错误");
            return "redirect:/admin";
        }
    }

    @RequestMapping("/noauth")
    @ResponseBody
    public String unauthorized(){
        return "未经授权无法访问此页面";
    }

    @RequestMapping("/logout")
    public String logout(){
        Subject currrentUser = SecurityUtils.getSubject();
        currrentUser.logout();
        return "redirect:/ordinary/toIndex";
    }
}
