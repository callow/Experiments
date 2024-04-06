package com.songlei.ubs.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.songlei.ubs.model.EmployeeContainer;

@SpringBootApplication
public class UBSApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext  springApplicationContext = SpringApplication.run(UBSApplication.class,args);
		
		// Start to do Tasks:
		EmployeeStatisticService statisticBean = springApplicationContext.getBean(EmployeeStatisticService.class);
		
		statisticBean.processEmployeeWithAgeRange(EmployeeContainer.EMPLOYEES, 1, 25);
		
		
	}
}
