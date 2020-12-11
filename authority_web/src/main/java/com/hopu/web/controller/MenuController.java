package com.hopu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {
    @GetMapping("/toMenuPage")
    public String toListPage(){
        return "admin/menu/menu_list";
    }

    public 
}
