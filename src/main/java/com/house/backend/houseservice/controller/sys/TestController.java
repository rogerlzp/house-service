package com.house.backend.houseservice.controller.sys;

import com.house.backend.houseservice.common.AjaxResult;
import com.house.backend.houseservice.dto.sys.SysUserDto;
import com.house.backend.houseservice.pojo.sys.SysUser;
import com.house.backend.houseservice.service.sys.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/userInfoController")
@Slf4j
public class TestController {

    @Autowired
    TestService testService;

    @PostMapping("/queryUserInfoPage")
    public AjaxResult<SysUser> queryUserInfoPage(@RequestBody SysUserDto request) {
        return AjaxResult.ok(testService.queryUserInfoPage(request));
    }
}
