package com.house.backend.houseservice.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置参数表
 */
@Configuration
@Slf4j
@Data
public class HouseConfig implements InitializingBean {
    private static HouseConfig simConfig = null;

    public static HouseConfig getInstance() {
        return simConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        simConfig = this;
    }
}
