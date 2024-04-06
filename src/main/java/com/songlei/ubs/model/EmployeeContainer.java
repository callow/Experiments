package com.songlei.ubs.model;

import java.util.List;

public class EmployeeContainer {
	
	/**
	 * Imagine : Employee Name and Age will not be duplicated
	 * 
	 * TODO 1. adapt the code to receive the input list either as a static list
	 */
	public static final List<Employee> EMPLOYEES = List.of(
			new Employee("Mary", 25), 
			new Employee("Alina", 30), 
			new Employee("Mary", 42), 
			new Employee("John", 24), 
			new Employee("Nicole", 25), 
			new Employee("Mike", 22));
}
