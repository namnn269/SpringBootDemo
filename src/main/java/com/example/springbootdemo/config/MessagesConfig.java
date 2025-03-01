package com.example.springbootdemo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class MessagesConfig {


    @Bean
    public MessageSource messageSource() throws IOException {
//        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
//        source.setBasenames("classpath:i18n/messages-common");

        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources("**/messages*.properties");

        Pattern pattern = Pattern.compile("(?<basename>i18n/.*messages[a-zA-Z0-9-]*)_?\\w*\\.properties");

        Set<String> basenames = new HashSet<>();

//        basenames.add("ValidationMessages"); // messages in JPA
        basenames.add("org.hibernate.validator.ValidationMessages"); // messages in JPA

        for (Resource resource : resources) {
            String path = resource.getFile().getPath();
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                basenames.add(matcher.group("basename"));
            }
        }

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames(basenames.toArray(String[]::new));

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

//        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();

        CustomHeaderLocaleResolver localeResolver = new CustomHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.FRANCE);
        localeResolver.setHeaderName("lang-header");
        return localeResolver;
    }

    // only for sessionLocaleResolver
//    @Bean // no need as a bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
//        localeChangeInterceptor.setParamName("language123");
//        return localeChangeInterceptor;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(localeChangeInterceptor());
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }

    // to custom header when using header for i18n
    public static class CustomHeaderLocaleResolver extends AcceptHeaderLocaleResolver {
        private String headerName = "Accept-Language";

        public CustomHeaderLocaleResolver() {
        }

        public CustomHeaderLocaleResolver(String headerName) {
            this.headerName = headerName;
        }

        public void setHeaderName(String headerName) {
            this.headerName = headerName;
        }

        @Override
        public Locale resolveLocale(HttpServletRequest request) {
            String langHeader = request.getHeader(headerName);
            System.out.println("custom header locale resolver in config: " + request.getLocale());
            if (langHeader != null && !langHeader.isBlank())
                return Locale.forLanguageTag(langHeader);
            return super.resolveLocale(request);
        }

        @Override
        public void setLocale(HttpServletRequest request,
                              HttpServletResponse response,
                              Locale locale) {
            System.out.println("setting locale header");
        }
    }
}

