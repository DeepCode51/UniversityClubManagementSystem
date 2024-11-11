package com.lmzfm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.lmzfm.dao")
public class CommunityManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommunityManagementApplication.class, args);
    }

}
