package com.github.artanpg.user.main;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Objects;

/**
 * In this class, all the general beans required by the project are
 * defined.
 *
 * @author Mohammad Yazdian
 */
@Configuration(proxyBeanMethods = false)
class GeneralConfigurationBeans {

    private GeneralConfigurationBeans() {
    }

    @Bean
    LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Configuration(proxyBeanMethods = false)
    static class JacksonModulesConfiguration {

        @Bean
        JavaTimeModule javaTimeModule() {
            return new JavaTimeModule();
        }

        @Bean
        Jdk8Module jdk8Module() {
            return new Jdk8Module();
        }

        @Bean
        BlackbirdModule blackbirdModule() {
            return new BlackbirdModule();
        }

    }

    @Configuration(proxyBeanMethods = false)
    @EnableConfigurationProperties
    static class MessageSourceConfiguration {

        @Bean
        @ConfigurationProperties(prefix = "spring.messages")
        MessageSourceProperties messageSourceProperties() {
            return new MessageSourceProperties();
        }

        @Bean
        MessageSource messageSource(MessageSourceProperties properties) {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

            if (StringUtils.hasText(properties.getBasename())) {
                messageSource.setBasenames(
                    StringUtils.commaDelimitedListToStringArray(
                        StringUtils.trimAllWhitespace(properties.getBasename())));
            }
            if (Objects.nonNull(properties.getEncoding())) {
                messageSource.setDefaultEncoding(properties.getEncoding().name());
            }
            messageSource.setFallbackToSystemLocale(properties.isFallbackToSystemLocale());
            if (Objects.nonNull(properties.getCacheDuration())) {
                messageSource.setCacheMillis(properties.getCacheDuration().toMillis());
            }
            messageSource.setAlwaysUseMessageFormat(properties.isAlwaysUseMessageFormat());
            messageSource.setUseCodeAsDefaultMessage(properties.isUseCodeAsDefaultMessage());
            return messageSource;
        }

        @Bean
        MessageSourceAccessor messageSourceAccessor(MessageSource messageSource, WebProperties webProperties) {
            return new MessageSourceAccessor(messageSource, webProperties.getLocale());
        }
    }
}
