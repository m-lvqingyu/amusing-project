package com.amusing.start.admin.controller;

import com.amusing.start.admin.from.UserFrom;
import org.springframework.web.bind.annotation.*;

/**
 * Create By 2021/7/24
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String get() {
        return "success";
    }

    @PostMapping
    public String post(@RequestBody UserFrom userFrom) {
        System.out.println("1111111111111");
        return userFrom + "success";
    }
}
