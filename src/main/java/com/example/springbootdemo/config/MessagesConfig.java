package com.example.springbootdemo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;

@Configuration
public class MessagesConfig implements WebMvcConfigurer {

    @Bean
    public MessageSource messageSource() {
//        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
//        source.setBasenames("classpath:i18n/messages-common");

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages-common");

        source.setDefaultEncoding("utf-8");
        source.setFallbackToSystemLocale(true);
        source.setDefaultLocale(Locale.ENGLISH);
        source.setUseCodeAsDefaultMessage(true);
        source.setCacheSeconds(60);
        return source;
    }

    @Bean
    public LocaleResolver localeResolver(HttpServletRequest request, HttpServletResponse response) {

//        SessionLocaleResolver localeResolver = new SessionLocaleResolver();

//        CookieLocaleResolver localeResolver = new CookieLocaleResolver("lang-cookie");
//        localeResolver.setCookieMaxAge(Duration.of(1000, ChronoUnit.SECONDS));

        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();

//        CustomHeaderLocaleResolver localeResolver = new CustomHeaderLocaleResolver();
//        localeResolver.setDefaultLocale(Locale.FRANCE);
//        localeResolver.setHeaderName("lang-header");
        return localeResolver;
    }

    // only for sessionLocaleResolver
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language123");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}

// to custom header when using header for i18n
class CustomHeaderLocaleResolver extends AcceptHeaderLocaleResolver {
    private String headerName = "Accept-Language";

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String langHeader = request.getHeader(headerName);
        System.out.println("in config: " + request.getLocale());
        if (langHeader != null && !langHeader.isBlank()) return Locale.forLanguageTag(langHeader);
        return super.resolveLocale(request);
    }
}
