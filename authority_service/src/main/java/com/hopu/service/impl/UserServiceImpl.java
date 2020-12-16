package com.hopu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopu.domain.Role;
import com.hopu.domain.User;
import com.hopu.domain.UserRole;
import com.hopu.mapper.UserMapper;
import com.hopu.service.IUserRoleService;
import com.hopu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public void setRole(String id, List<Role> roles) {
        userRoleService.remove(new QueryWrapper<>(new UserRole()).eq("user_id",id));
        for (Role role : roles){
            UserRole userRole = new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(role.getId());
            userRoleService.save(userRole);
        }
    }
}
