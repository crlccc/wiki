package com.example.wiki.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@ComponentScan(basePackages = {"com.example.wiki"})
@SpringBootApplication
public class WikiApplication {


    private static final Logger log = LoggerFactory.getLogger(WikiApplication.class);
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(WikiApplication.class);
        Environment env = app.run(args).getEnvironment();

        log.info("启动成功！");
        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
                "Local: \t\t\thttp://localhost:127.0.0.1:{}\n\t" +
                "External: \t\thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("server.port"),
                env.getProperty("server.address"),
                env.getProperty("server.port"));;
    }

}
