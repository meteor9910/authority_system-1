package com.hopu.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hopu.domain.Role;
import com.hopu.domain.User;
import com.hopu.service.IRoleService;
import com.hopu.utils.PageEntity;
import com.hopu.utils.ResponseEntity;
import com.hopu.service.IUserService;
import com.hopu.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


import static com.hopu.utils.ResponseEntity.error;
import static com.hopu.utils.ResponseEntity.success;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;

    @GetMapping("/toRolePage")
    public String toRoleListPage(){
        return "admin/role/role_list";
    }

    @RequestMapping("/toAddPage")
    public String toAddPage(){
        return "/admin/role/role_add";
    }

    @ResponseBody
    @RequestMapping("/add")
    public ResponseEntity addRole(Role role){
        // 可以先对用户名重名校验
        // 创建条件查询封装对象
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role",role.getRole());
        Role one = roleService.getOne(queryWrapper);
        if(one !=null){
            return error("对象已经存在了");
        }
        role.setId(UUIDUtils.getID());
        role.setCreateTime(new Date());
        roleService.save(role);
        return success();
    }

    // 向修改页面跳转
    @RequestMapping("/toUpdatePage")
    public String toUpdatePage(String id, HttpServletRequest request){
        Role role = roleService.getById(id);
        request.setAttribute("role",role);
        return "admin/role/role_update";
    }

    // 用户修改
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity updateUser(Role role){
        role.setUpdateTime(new Date());
        roleService.updateById(role);
        return success();
    }

    // 用户删除
    @RequestMapping("/delete")
    @ResponseBody
    public ResponseEntity deleteUser(@RequestBody List<Role> roles){
        try {
            // 如果是root用户，禁止删除
            for (Role role : roles) {
                if("root".equals(role.getRole())){
                    throw new Exception("不能删除超级管理员");
                }
//                if(user.getUserName().equals("root")){ // nullpointerException
//                    throw new Exception("不能删除超级管理员");
//                }
                roleService.removeById(role.getId());
            }
            return success();
        } catch (Exception e) {
            e.printStackTrace();
            return error();
        }
    }


    @ResponseBody
    @RequestMapping("/list")
    public PageEntity list(@RequestParam(value = "page" , defaultValue = "1")Integer pageNum ,
                            @RequestParam(value = "limit" , defaultValue = "5")Integer pageSize,
                            Role role){
        Page<Role> page2 = new Page<>(pageNum,pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (role != null){
            if (StringUtils.isNotEmpty(role.getRole())){
                queryWrapper.like("role",role.getRole());
            }
        }
        IPage<Role> iPage = roleService.page(page2,queryWrapper);
        return new PageEntity(iPage);
    }
}
