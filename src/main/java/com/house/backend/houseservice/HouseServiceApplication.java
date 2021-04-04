package com.house.backend.houseservice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
@SpringBootApplication(scanBasePackages = {"com.house.backend"})
@MapperScan(basePackages = {"com.house.backend.houseservice.dao","com.house.backend.houseservice.generalization"}, sqlSessionTemplateRef = "sqlSessionTemplate")

@Slf4j
public class HouseServiceApplication {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World";
    }

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext application = SpringApplication.run(HouseServiceApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("\n--------------------------------------------------------------------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}/house/backend/swagger-ui.html\n\t" +
                        "External: \thttp://{}:{}/datasim/backend/\n--------------------------------------------------------------------------------------------------------------------",
                new String[]{"BackEndApplication Server Application v1.0",
                        env.getProperty("server.port"),
                        InetAddress.getLocalHost().getHostAddress(),
                        env.getProperty("server.port")});
    }

}
