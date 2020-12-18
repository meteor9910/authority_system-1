package com.hopu.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hopu.domain.Role;
import com.hopu.domain.User;
import com.hopu.utils.ALiYunOOSUtils;
import com.hopu.utils.ResponseEntity;
import com.hopu.service.IUserService;
import com.hopu.utils.ShiroUtils;
import com.hopu.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import static com.hopu.utils.ResponseEntity.error;
import static com.hopu.utils.ResponseEntity.success;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/toSetRole")
    public String toSetRole(String id , Model model){
        model.addAttribute("user_id",id);
        return "admin/user/user_setRole";
    }

    @ResponseBody
    @RequestMapping("setRole")
    public ResponseEntity setRole(String id, @RequestBody ArrayList<Role> roles){
        userService.setRole(id,roles);
        return success();
    }

    @RequiresPermissions("user:list")
    @GetMapping("/toListPage")
    public String toUserListPage(){
        return "admin/user/user_list";
    }

    @RequestMapping("/toAddPage")
    @RequiresPermissions("user:add")
    public String toAddPage(){
        return "/admin/user/user_add";
    }

    @ResponseBody
    @RequestMapping("/add")
    public ResponseEntity addUser(User user, @RequestParam("user-img")MultipartFile multipartFile){
        // 可以先对用户名重名校验
        // 创建条件查询封装对象
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name",user.getUserName());
        User one = userService.getOne(queryWrapper);

        if(one !=null){
            return error("用户名已经存在了");
            // 示例 http://as-img.oss-cn-beijing.aliyuncs.com/20201218183032.jpg
        }

        String oldName = multipartFile.getOriginalFilename();
        System.out.println(oldName);
        String[] fileType = oldName.split("\\.");
        try {
            String newFileName = ALiYunOOSUtils.createAnOrderId();

            String newName = newFileName + "." + fileType[1];

            ALiYunOOSUtils.uploadFile(multipartFile,newName);

            user.setUser_img(newName);
            user.setId(UUIDUtils.getID());
            user.setSalt(UUIDUtils.getID());
            user.setCreateTime(new Date());
            ShiroUtils.encPass(user);
            userService.save(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success();
    }

    // 向修改页面跳转
    @RequestMapping("/toUpdatePage")
    public String toUpdatePage(String id, HttpServletRequest request){
        User user = userService.getById(id);
        request.setAttribute("user",user);
        return "admin/user/user_update";
    }

    // 用户修改
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity updateUser(User user){
        ShiroUtils.encPass(user);
        user.setUpdateTime(new Date());
        userService.updateById(user);
        return success();
    }

    // 用户删除
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity deleteUser(@RequestBody List<User> users){
        try {
            // 如果是root用户，禁止删除
            for (User user : users) {
                if("root".equals(user.getUserName())){
                    throw new Exception("不能删除超级管理员");
                }
//                if(user.getUserName().equals("root")){ // nullpointerException
//                    throw new Exception("不能删除超级管理员");
//                }
                userService.removeById(user.getId());
            }
            return success();
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }


    @ResponseBody
    @RequestMapping("/list")
    public IPage<User> list(@RequestParam(value = "page" , defaultValue = "1")Integer pageNum ,
                       @RequestParam(value = "limit" , defaultValue = "5")Integer pageSize,
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
