package com.kadmos.savingsb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan({ "com.kadmos.*" })
public class SavingsbApplication {

  public static void main(String[] args) {
    SpringApplication.run(SavingsbApplication.class, args);
  }
}
