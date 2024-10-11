package com.tre.centralkitchen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @description
 * @author: JDev
 * @create: 2021-07-29 09:53
 **/
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.tre.*.mapper")
@ComponentScan({"com.tre.*"})
@EnableScheduling
public class CentralKitchenApplication {

    public static void main(String[] args) {
        SpringApplication.run(CentralKitchenApplication.class, args);
    }

}
