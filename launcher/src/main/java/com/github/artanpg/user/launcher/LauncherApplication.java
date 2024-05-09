package com.github.artanpg.user.launcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(
    proxyBeanMethods = false,
    scanBasePackages = {
        "com.github.artanpg.user.controller",
        "com.github.artanpg.user.service",
        "com.github.artanpg.user.persistence",
        "com.github.artanpg.user.launcher"
    },
    exclude = {
        MessageSourceAutoConfiguration.class,
        ValidationAutoConfiguration.class,
    }
)
@ConfigurationPropertiesScan(basePackages = {"com.github.artanpg.user.service.properties"})
public class LauncherApplication {

    public static void main(String[] args) {
        SpringApplication.run(LauncherApplication.class, args);
    }
}
