package com.example.springbootdemo.config.interceptor;

import com.example.springbootdemo.config.ZoneIdHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZoneId;

public class TimezoneInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        ZoneIdHolder.setDeferredZoneId(() -> getZoneId(request));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        ZoneIdHolder.clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        ZoneIdHolder.clear();
    }

    private ZoneId getZoneId(HttpServletRequest request) {
        String timezone = request.getHeader("timezone");
        ZoneId zoneId;
        try {
            zoneId = ZoneId.of(timezone);
        } catch (Exception ex) {
            zoneId = ZoneId.systemDefault();
        }
        return zoneId;
    }
}
