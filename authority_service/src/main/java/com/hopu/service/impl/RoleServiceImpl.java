package com.hopu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hopu.domain.Menu;
import com.hopu.domain.Role;
import com.hopu.domain.RoleMenu;
import com.hopu.mapper.RoleMapper;
import com.hopu.service.IRoleMenuService;
import com.hopu.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IRoleMenuService roleMenuService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setMenu(String id, List<Menu> menus) {
        roleMenuService.remove(new QueryWrapper<RoleMenu>(new RoleMenu()).eq("role_id",id));
        for (Menu menu : menus){
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(id);
            roleMenu.setMenuId(menu.getId());
            roleMenuService.save(roleMenu);
        }
    }
}
