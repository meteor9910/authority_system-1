package com.hopu.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hopu.domain.User;
import com.hopu.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping("/toListPage")
    public String toUserListPage(){
        return "admin/user/user_list";
    }

//    @ResponseBody
//    @RequestMapping("/list")
//    public IPage<User> list(@RequestParam(value = "pageNum" , defaultValue = "1")Integer pageNum ,
//                            @RequestParam(value = "pageSize" , defaultValue = "5")Integer pageSize,
//                            Model model){
//        Page<User> page2 = new Page<>(pageNum, pageSize);
//        IPage<User> userIPage = userService.page(page2);
//        model.addAttribute("ipage",userIPage);
//        return userIPage;
//    }

    @RequestMapping("/toAddPage")
    public String toAddPage(){
        return "/admin/user/user_add";
    }

    @ResponseBody
    @RequestMapping("")
    public


    @ResponseBody
    @RequestMapping("/list")
    public IPage<User> list(@RequestParam(value = "pageNum" , defaultValue = "1")Integer pageNum ,
                       @RequestParam(value = "pageSize" , defaultValue = "5")Integer pageSize,
                       User user){
        Page<User> page2 = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (user != null){
            if (StringUtils.isNotEmpty(user.getUserName())){
                queryWrapper.like("user_name",user.getUserName());
            }if (StringUtils.isNotEmpty(user.getTel())){
                queryWrapper.like("tel",user.getTel());
            }if (StringUtils.isNotEmpty(user.getEmail())){
                queryWrapper.like("email",user.getEmail());
            }
        }
        IPage<User> iPage = userService.page(page2,queryWrapper);
        return iPage;
    }
}
