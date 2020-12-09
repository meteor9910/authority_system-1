package com.hopu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopu.domain.User;
import com.hopu.mapper.UserMapper;
import com.hopu.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
