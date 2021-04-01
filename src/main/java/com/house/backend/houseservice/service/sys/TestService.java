package com.house.backend.houseservice.service.sys;

import com.house.backend.houseservice.dao.sys.SysUserMapper;
import com.house.backend.houseservice.dto.sys.SysUserDto;
import com.house.backend.houseservice.pojo.sys.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    SysUserMapper sysUserMapper;

    public SysUser queryUserInfoPage(SysUserDto sysUserDto) {
        SysUser sysUserCondition = new SysUser();
        sysUserCondition.setUid("123456");
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(sysUserCondition.getUid());
        return sysUser;
    }

}
