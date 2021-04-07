package com.house.backend.houseservice.controller.test;

import com.house.backend.houseservice.common.AjaxResult;
import com.house.backend.houseservice.dto.sys.SysUserDto;
import com.house.backend.houseservice.pojo.sys.SysUser;
import com.house.backend.houseservice.service.sys.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/helloController")
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public AjaxResult<SysUser> sayHello() {
        return AjaxResult.ok();
    }
}
