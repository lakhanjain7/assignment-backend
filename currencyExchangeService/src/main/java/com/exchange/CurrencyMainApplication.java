package com.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.exchange.controller.ExchangeController; 

@SpringBootApplication 
@ComponentScan(basePackageClasses = ExchangeController.class)
public class CurrencyMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyMainApplication.class, args);
    }
}
