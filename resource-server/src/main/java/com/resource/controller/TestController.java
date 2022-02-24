package com.resource.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试控制层，测试授权
 *
 * @author by lucongwen
 * @Date 2022/2/24 16:31
 */
@RestController
@RequestMapping("/resource")
public class TestController {

    @RequestMapping("/get")
    public String testMethod() {
        return "success get resource";
    }
}
