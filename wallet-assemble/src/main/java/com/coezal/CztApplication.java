package com.coezal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableTransactionManagement
@MapperScan("com.coezal.wallet.dal.dao")
@EnableAsync //异步生效
@EnableScheduling //开启定时任务支持
public class CztApplication {

    public static void main(String[] args) {
        SpringApplication.run(CztApplication.class, args);
    }

}
