package com.sale.supermarketboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author aaa
 */
@SpringBootApplication
@MapperScan({"com.sale.supermarketboot.dao"})
public class SupermarketbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupermarketbootApplication.class, args);
    }
//,"com.sale.supermarketboot.service"
}
