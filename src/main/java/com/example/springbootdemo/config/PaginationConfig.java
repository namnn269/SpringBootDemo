package com.example.springbootdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolverSupport;
import org.springframework.data.web.SortHandlerMethodArgumentResolverSupport;
import org.springframework.jdbc.config.SortedResourcesFactoryBean;

@Configuration
public class PaginationConfig {

    @Autowired
    public void changePaginationProperties(PageableHandlerMethodArgumentResolverSupport paginationConfig) {
        paginationConfig.setSizeParameterName("size");
        paginationConfig.setPageParameterName("page");
    }

    @Autowired
    public void changeSortProperties(SortHandlerMethodArgumentResolverSupport sortConfig) {
        sortConfig.setSortParameter("sort");
        sortConfig.setPropertyDelimiter(",");
    }
}
