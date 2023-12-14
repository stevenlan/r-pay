package com.rpay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
public class IndexController {

    /*
     * 主页

    @GetMapping("")
    public String index(Model model) {
        if (getLoginUser() == null) {
            return "redirect:login";
        }
        return "redirect:index";
    }*/

    /*
    @GetMapping("/fileChoose")
    public String fileChoose() {

        return "fileChoose";
    }*/

    /*
     * 错误页
    @RequestMapping("/error/{code}")
    public String error(@PathVariable("code") String code) {
        if ("403".equals(code)) {
            return "error/403";
        } else if ("500".equals(code)) {
            return "error/500";
        }
        return "error/404";
    }*/
}
