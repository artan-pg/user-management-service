package com.github.artanpg.user.launcher;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.blackbird.BlackbirdModule;
import jakarta.validation.Validator;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.boot.validation.beanvalidation.FilteredMethodValidationPostProcessor;
import org.springframework.boot.validation.beanvalidation.MethodValidationExcludeFilter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.CollectionFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * In this class, all the general beans required by the project are defined.
 *
 * @author Mohammad Yazdian
 */
@Configuration(proxyBeanMethods = false)
class GeneralConfigurationBeans {

    private GeneralConfigurationBeans() {
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
    @EnableConfigurationProperties(MessageSourceProperties.class)
    @ImportRuntimeHints(MessageSourceConfiguration.MessageSourceRuntimeHints.class)
    static class MessageSourceConfiguration {

        @Bean
        MessageSource messageSource(MessageSourceProperties properties) {
            var messageSource = new ReloadableResourceBundleMessageSource();

            if (!CollectionUtils.isEmpty(properties.getBasename())) {
                messageSource.setBasenames(properties.getBasename().toArray(new String[0]));
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
            messageSource.setCommonMessages(loadCommonMessages(properties.getCommonMessages()));
            return messageSource;
        }

        Properties loadCommonMessages(List<Resource> resources) {
            if (CollectionUtils.isEmpty(resources)) {
                return null;
            }
            var properties = CollectionFactory.createSortedProperties(false);
            for (Resource resource : resources) {
                try {
                    PropertiesLoaderUtils.fillProperties(properties, resource);
                } catch (IOException ex) {
                    throw new UncheckedIOException("Failed to load common messages from '%s'".formatted(resource), ex);
                }
            }
            return properties;
        }

        @Bean
        MessageSourceAccessor messageSourceAccessor(MessageSource messageSource, WebProperties webProperties) {
            return new MessageSourceAccessor(messageSource, webProperties.getLocale());
        }

        static class MessageSourceRuntimeHints implements RuntimeHintsRegistrar {

            @Override
            public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
                hints
                    .resources()
                    .registerPattern("messages.properties")
                    .registerPattern("messages_*.properties");
            }

        }
    }

    @Configuration(proxyBeanMethods = false)
    static class ValidationConfiguration {

        @Bean
        LocalValidatorFactoryBean validator(MessageSource messageSource,
                                            ObjectProvider<ValidationConfigurationCustomizer> customizers) {

            var localValidatorFactoryBean = new LocalValidatorFactoryBean();
            var interpolatorFactory = new MessageInterpolatorFactory(messageSource);

            localValidatorFactoryBean.setMessageInterpolator(interpolatorFactory.getObject());
            localValidatorFactoryBean.setValidationMessageSource(messageSource);
            localValidatorFactoryBean.setConfigurationInitializer(
                configuration -> customizers.orderedStream().forEach(customizer -> customizer.customize(configuration)));

            return localValidatorFactoryBean;
        }

        @Bean
        MethodValidationPostProcessor methodValidationPostProcessor(
            ObjectProvider<Validator> validator, ObjectProvider<MethodValidationExcludeFilter> excludeFilters) {

            var processor = new FilteredMethodValidationPostProcessor(excludeFilters.orderedStream());

            processor.setProxyTargetClass(true);
            processor.setAdaptConstraintViolations(true);
            processor.setValidatorProvider(validator);

            return processor;
        }
    }
}
