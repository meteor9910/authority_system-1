package com.hopu.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hopu.domain.User;
import com.hopu.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private IUserService userService;

	@RequestMapping("/login")
	public String login(User user, Model model , HttpServletRequest request) {

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(),user.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            HttpSession session = WebUtils.toHttp(request).getSession();
            User user1 = (User) subject.getPrincipal();
            User user2 = userService.getOne(new QueryWrapper<User>().eq("user_name", user1.getUserName()));
            System.out.println(user1);
            session.setAttribute("user",user1);

            return "admin/index";
        }catch (Exception e){
            String msg = "账户["+ token.getPrincipal() + "]的用户名或密码错 误！";
            model.addAttribute("msg", msg);
            return "forward:/login.jsp";
        }
	}

    //退出
    @RequestMapping(value = "/logout",name="用户登出")
    public String logout(){
        return "forward:/login";
    }
}
