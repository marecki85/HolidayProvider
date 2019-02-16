package com.bluestone.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.bluestone.rest.controller.holidayController;
import com.bluestone.rest.service.HolidayProvider;

@SpringBootApplication
@ComponentScan(basePackageClasses = MainAppConfig.class)
public class MainAppConfig {
	public static void main(String[] args) {
		SpringApplication.run(MainAppConfig.class, args);
	}
}
