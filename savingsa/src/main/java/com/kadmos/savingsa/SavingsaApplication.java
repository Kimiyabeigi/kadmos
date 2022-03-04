package com.kadmos.savingsa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SavingsaApplication {

  public static void main(String[] args) {
    SpringApplication.run(SavingsaApplication.class, args);
  }
}
