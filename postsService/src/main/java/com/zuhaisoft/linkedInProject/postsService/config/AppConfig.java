package com.zuhaisoft.linkedInProject.postsService.config;

import com.zuhaisoft.linkedInProject.postsService.auth.AuthContextHolder;
import feign.RequestInterceptor;
import feign.form.spring.SpringFormEncoder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public SpringFormEncoder feignFormEncoder(){
        return new SpringFormEncoder();
    }


}
