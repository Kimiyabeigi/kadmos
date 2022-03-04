package com.kadmos.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UaaApplication {

  public static void main(String[] args) {
    SpringApplication.run(UaaApplication.class, args);
  }
}
