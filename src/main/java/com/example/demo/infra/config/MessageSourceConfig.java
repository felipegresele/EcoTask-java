package com.example.demo.infra.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

@Configuration
public class MessageSourceConfig {

    /**
     * Configura o MessageSource para carregar mensagens dos arquivos properties
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600); // Cache por 1 hora
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    /**
     * Configura o LocaleResolver para detectar o idioma do header Accept-Language
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.forLanguageTag("pt-BR")); // Português como padrão
        localeResolver.setSupportedLocales(Arrays.asList(
                Locale.forLanguageTag("pt-BR"),  // Português (Brasil)
                Locale.forLanguageTag("en-US")   // Inglês (Estados Unidos)
        ));
        return localeResolver;
    }
}

