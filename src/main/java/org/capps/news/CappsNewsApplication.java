package org.capps.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CappsNewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CappsNewsApplication.class, args);
    }

}
