package com.hopu.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hopu.domain.*;
import com.hopu.service.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class MyRealm extends AuthorizingRealm {

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuService menuService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        User user = (User) principals.getPrimaryPrincipal();

        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        List<UserRole> userRoleList = userRoleService.list(new QueryWrapper<UserRole>(userRole));

        List<String> roleIds = new ArrayList<String>();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (UserRole userRole1 : userRoleList){
            roleIds.add(userRole1.getRoleId());
            Role role = roleService.getById(userRole1.getRoleId());
            if (role!=null) {
                simpleAuthorizationInfo.addRole(role.getRole());
            }
        }
        if (!roleIds.isEmpty()) {
            RoleMenu roleMenu2 = new RoleMenu();
            QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<RoleMenu>(roleMenu2);
            queryWrapper.in("role_id", roleIds);
            List<RoleMenu> list = roleMenuService.list(queryWrapper);
            for (RoleMenu Rolemenu : list) { Menu menu = menuService.getById(Rolemenu.getMenuId());
                if (menu!=null) { simpleAuthorizationInfo.addStringPermission(menu.getPermiss());
                }
            }
        }

        return simpleAuthorizationInfo;
    }

    //验证

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        String username = token.getUsername();

        User user = userService.getOne(new QueryWrapper<User>().eq("user_name", username));

        if (user==null){
            throw new UnknownAccountException("用户名或密码错误！");
        }

        ByteSource credentialsSalt = ByteSource.Util.bytes(username + user.getSalt());

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),credentialsSalt,getName());

        return info;
    }
}
