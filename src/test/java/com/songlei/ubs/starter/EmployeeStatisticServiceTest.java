package com.songlei.ubs.starter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.songlei.ubs.model.Employee;

@SpringBootTest
public class EmployeeStatisticServiceTest {

	@Autowired
	private EmployeeStatisticService employeeStatisticService;
		
	@Test
	@DisplayName("Test Age 1 ~ 12 = 1 and length 4 = 2")
	void testAge1_12_One_Name_Length_4_Two() throws Exception {
		
		List<Employee> sample1 = List.of(
				new Employee("Hook", 10), 
				new Employee("John", 50), 
				new Employee("Jenifer", 30));
		
		employeeStatisticService.processEmployeeWithAgeRange(sample1, 1, 12);
		
		assertTrue(EmployeeStatisticService.LENGTH_4_NAME_COUNTER == 2, "Employee with name length 4 is wrong");
		
		List<Employee> outputFileEmployees = employeeStatisticService.readBDotTxt();
		assertTrue(outputFileEmployees.size() == 1, "Age 1~12 count is wrong");
		
		Employee outputFileEmployee = outputFileEmployees.get(0);
		assertTrue(outputFileEmployee.getName().equals("Hook") &&outputFileEmployee.getAge() == 10, "Output is wrong");
	}
}
