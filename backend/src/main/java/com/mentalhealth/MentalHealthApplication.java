package com.mentalhealth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MentalHealthApplication {
    public static void main(String[] args) {
        SpringApplication.run(MentalHealthApplication.class, args);
        System.out.println("========================================");
        System.out.println("  大学生心理健康咨询系统启动成功!");
        System.out.println("  http://localhost:8080");
        System.out.println("========================================");
    }
}
