package com.zuhaisoft.linkedInProject.ConnectionsService.auth;

import feign.RequestInterceptor;
import org.springframework.stereotype.Component;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(feign.RequestTemplate requestTemplate) {
        Long userId = AuthContextHolder.getCurrentUserId();
        if (userId != null) {
            requestTemplate.header("X-User-Id", String.valueOf(userId));
        }
    }



}
