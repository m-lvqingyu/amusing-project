package com.amusing.start.admin.controller;

import com.amusing.start.admin.from.UserFrom;
import com.amusing.start.client.api.MapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Create By 2021/7/24
 *
 * @author lvqingyu
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private MapClient mapClient;

    @GetMapping("/{id}")
    public String get(@PathVariable("id") String id) {
        return id;
    }

    @PostMapping
    public String post(@RequestBody UserFrom userFrom) {
        mapClient.gaoDeGeoCode("北京朝阳门");
        return "success";
    }
}
