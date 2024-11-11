package com.lmzfm.cors;

import com.lmzfm.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Bean
    public WebMvcConfigurer corsConfigs(){
        return new WebMvcConfigurer(){
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedHeaders("*")
                        .allowedMethods("*")
                        // .allowCredentials(true)
                        .allowedOrigins("*")
                        .maxAge(3600L);
            }
        };
    }
    private CorsConfiguration corsConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");  //允许所有域名访问
        corsConfiguration.addAllowedHeader("*");  //允许所有请求头
        corsConfiguration.addAllowedMethod("*");  //允许所有的请求类型
        corsConfiguration.setMaxAge(3600L);
        corsConfiguration.setAllowCredentials(true); //允许请求携带验证信息（cookie）
        return corsConfiguration;
    }
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration=
                registry.addInterceptor(new LoginInterceptor());
        //添加拦截内容
        registration.addPathPatterns("/**");
        //添加不拦截内容
        registration.excludePathPatterns("/users/loginIn",
                "users/queryuserInfo","team/queryTeamAll",
                "activity/findByTeamId","typeInfo/queryTypeList","team/queryTeamList");
    }
}
