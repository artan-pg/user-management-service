package com.github.artanpg.user.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(
    proxyBeanMethods = false,
    scanBasePackages = {
        "com.github.artanpg.user.controller",
        "com.github.artanpg.user.service",
        "com.github.artanpg.user.persistence",
        "com.github.artanpg.user.main"
    },
    exclude = {
        MessageSourceAutoConfiguration.class,
    }
)
@ConfigurationPropertiesScan(basePackages = {"com.github.artanpg.user.service.properties"})
public class LauncherApplication {

    public static void main(String[] args) {
        SpringApplication.run(LauncherApplication.class, args);
    }
}
