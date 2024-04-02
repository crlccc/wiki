package com.example.wiki.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan("com.example")
@SpringBootApplication
@MapperScan("com.example.wiki.mapper")
@EnableScheduling
public class WikiApplication {


    private static final Logger log = LoggerFactory.getLogger(WikiApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WikiApplication.class);
        Environment env = app.run(args).getEnvironment();

        log.info("启动成功！");
        log.info("Local: \t\thttp://localhost:127.0.0.1:{}", env.getProperty("server.port"));

    }

}
