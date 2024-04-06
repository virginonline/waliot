package com.virginonline.waliot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WaliotApplication {

  public static void main(String[] args) {
    SpringApplication.run(WaliotApplication.class, args);
  }
}
