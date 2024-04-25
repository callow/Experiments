package com.songlei.ubs.starter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.songlei.ubs.model.Employee;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@SuppressWarnings("deprecation")
public class EmployeeStatisticService {
	
	
	private static final String[] HEADERS = {"name", "age"};
	public static int LENGTH_4_NAME_COUNTER = 0;
	
	
	/**
	 * TODO 2. all files must be read from a configurable folder, the b.txt is configured under resource folder
	 */
	@Value("${employee.output}")
	private String csvPath;

		
	/**
	 * This method will count/log all employee with name.length == 4 whatever the age range. 
	 * and save those with specified age range into ‘b.txt’ in csv format with only {name, age} as Header as requested.
	 * 
	 * if you want to ignore the age range and save into 'b.txt', just pass =>  ageFrom = 0 , ageTo = 200 will do.
	 * 
	 * TODO 3. Log the count of names with length 4
	 * TODO 4. Update the above code to receive the name and age for each person and save them as CSV in b.txt file
	 * TODO 5. Implement a way to write into the b.txt file only persons over a specified age
	 * 
	 */
	public void processEmployeeWithAgeRange(List<Employee> employee, int ageFrom, int ageTo) {
		LENGTH_4_NAME_COUNTER = 0;
		try (FileWriter out = new FileWriter(csvPath);CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
			for(Employee i : employee) {
    			String name = i.getName();
    			int age = i.getAge();
    			
    			if(StringUtils.isNotBlank(name) && name.length() == 4) {
    				LENGTH_4_NAME_COUNTER++;
    			}
    			
    			if (age >= ageFrom && age <= ageTo) {
    				printer.printRecord(i.getName(), i.getAge());
    			}
			}
	    } catch (IOException e1) {
			e1.printStackTrace();
		}
		log.info("Count of names with length 4 is {} ", LENGTH_4_NAME_COUNTER);
	}
	
	
	
	
	/**
	 * For Junit Test Purpose only
	 */
	public List<Employee> readBDotTxt() {
		List<Employee> employees = new ArrayList<>();
		try(Reader in = new FileReader(csvPath)) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader(HEADERS).withFirstRecordAsHeader().parse(in);
			for (CSVRecord record : records) {
				employees.add(new Employee(record.get("name"),Integer.parseInt(record.get("age"))));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return employees;
	}
	
	
}
