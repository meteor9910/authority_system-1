package com.hopu.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hopu.domain.Menu;
import com.hopu.domain.RoleMenu;
import com.hopu.service.IMenuService;
import com.hopu.service.IRoleMenuService;
import com.hopu.utils.IconFontUtils;
import com.hopu.utils.PageEntity;
import com.hopu.utils.ResponseEntity;
import com.hopu.utils.UUIDUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hopu.utils.ResponseEntity.success;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @GetMapping("/toMenuPage")
    @RequiresPermissions("menu:list")
    public String toListPage(){
        return "admin/menu/menu_list";
    }

    @RequestMapping("MenuList")
    @ResponseBody
    public PageEntity menuList(String roleId){
        List<RoleMenu> roleMenuList = roleMenuService.list(new QueryWrapper<RoleMenu>().eq("role_id",roleId));
        List<Menu> list = menuService.list();
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        list.forEach(menu -> {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(menu));
            List<String> menuId = roleMenuList.stream().map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
            if (menuId.contains(menu.getId())){
                jsonObject.put("LAY_CHECKED",true);
            }
            jsonObjects.add(jsonObject);
        });
        return new PageEntity(jsonObjects.size(),jsonObjects);
    }

    @RequestMapping("/list")
    @ResponseBody
    public PageEntity list(){
        // 先查询父菜单
        List<Menu> pList = menuService.list(new QueryWrapper<Menu>().eq("pid", "0"));
        // 接着，根据父菜单id查询对应的所有子菜单，把子菜单封装到父菜单对象的属性nodes中

        // 需求：最终返回的是各个菜单集合
        ArrayList<Menu> menus = new ArrayList<>();

        findChildMenu(pList, menus);


//        return new PageEntity(menuList.size(),menuList);
        return new PageEntity(menus.size(),menus);

//        // 如果不涉及到子菜单关联
//        List<Menu> list = menuService.list();
//        return new PageEntity(list.size(),list);

    }

    //私有方法，循环查询子菜单列表
    private List<Menu> findChildMenu(List<Menu> pList, List<Menu> menus){
//        pList.forEach(menu -> {
//////            menus.add(menu); // 向返回集合中，添加父菜单
////
////            String pId = menu.getId();
////            List<Menu> childList = menuService.list(new QueryWrapper<Menu>().eq("pid", pId));
////            menu.setNodes(childList);
////
////            menus.addAll(childList); // 向返回集合中，添加子菜单
////
////            if(childList.size()>0){
////                // 递归调用
////                menus= findChildMenu(childList,menus);
////            }
////        });

        for (Menu menu : pList) {
            if(!menus.contains(menu)){
                menus.add(menu);
            }

            String pId = menu.getId();
            List<Menu> childList = menuService.list(new QueryWrapper<Menu>().eq("pid", pId));
            menu.setNodes(childList);

            if(childList.size()>0){
                // 递归调用
                menus= findChildMenu(childList,menus);
            }
        }
        return menus;
    }

    // 向菜单添加页面跳转
    @RequestMapping("/addPage")
    @RequiresPermissions("menu:add")
    public String toAddPage(HttpServletRequest request){
        // 1、查询父级目录（不需要查询第三级菜单）
        // 1.1、先查询顶级父目录
        List<Menu> pMenus = menuService.list(new QueryWrapper<Menu>().eq("pid", "0"));
        // 1.2、查询并封装对应的子菜单
        pMenus.forEach(menu -> {
            List<Menu> childMenus = menuService.list(new QueryWrapper<Menu>().eq("pid", menu.getId()));
            menu.setNodes(childMenus);
        });

        // 2、查询所有字体图标(查询所有字体图片class类)
        List<String> iconFont = IconFontUtils.getIconFont();

        request.setAttribute("list",pMenus);
        request.setAttribute("iconFont",iconFont);

        return "admin/menu/menu_add";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequestMapping("/save")
    public ResponseEntity addMenu(Menu menu){
        menu.setId(UUIDUtils.getID());
        menu.setCreateTime(new Date());
        menuService.save(menu);
        return success();
    }

    /**
     * 向菜单修改页面跳转
     */
    @GetMapping("/toUpdatePage")
    public String toUpdatePage(@RequestParam(value = "id") String menuId, HttpServletRequest request){
        // 先查询要修改的菜单信息
        Menu menu = menuService.getById(menuId);

        // 查询图标信息
        List<String> iconFont = IconFontUtils.getIconFont();

        // 1、查询父级目录（不需要查询第三级菜单）
        // 1.1、先查询顶级父目录
        List<Menu> pMenus = menuService.list(new QueryWrapper<Menu>().eq("pid", "0"));
        // 1.2、查询并封装对应的子菜单
        pMenus.forEach(menu2 -> {
            List<Menu> childMenus = menuService.list(new QueryWrapper<Menu>().eq("pid", menu2.getId()));
            menu2.setNodes(childMenus);
        });

        // 数据放在request域对象中
        // pageScope < requestScope < sessionScope < applicationScope
        request.setAttribute("menu",menu);
        request.setAttribute("iconFont",iconFont);
        request.setAttribute("list",pMenus);

        return "admin/menu/menu_update";
    }
    /**
     * 修改
     */
    @ResponseBody
    @RequestMapping("/update")
    public ResponseEntity update(Menu menu){
        menu.setUpdateTime(new Date());
        menuService.updateById(menu);
        return success();
    }

    /**
     * 删除（支持批量删除）
     */
    @ResponseBody
    @RequestMapping("/delete")
    public ResponseEntity delete(@RequestBody ArrayList<Menu> menus){
        List<String> list = new ArrayList<String>();
        for (Menu menu : menus) {
            list.add(menu.getId());
        }
        menuService.removeByIds(list);
        return success();
    }
}
