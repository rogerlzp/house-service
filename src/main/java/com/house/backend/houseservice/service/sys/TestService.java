package com.house.backend.houseservice.service.sys;

import com.house.backend.houseservice.dao.sys.SysUserMapper;
import com.house.backend.houseservice.dto.sys.SysUserDto;
import com.house.backend.houseservice.pojo.sys.SysUser;
import com.house.backend.houseservice.pojo.sys.SysUserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    SysUserMapper sysUserMapper;

    public SysUser queryUserInfoPage(SysUserDto sysUserDto) {
        SysUserExample sysUserExample = new SysUserExample();
        sysUserExample.or().andUidEqualTo("123456");
        SysUser sysUser = sysUserMapper.selectOneByExample(sysUserExample);
        return sysUser;
    }

}
