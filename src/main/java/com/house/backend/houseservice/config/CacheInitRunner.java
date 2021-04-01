package com.house.backend.houseservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(value = 1)
@Slf4j
public class CacheInitRunner implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {

    }


}
