package com.reactive;

import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.reactive.model.Employee;
import com.reactive.repository.EmployeeRepository;

@SpringBootApplication
public class ReactiveMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveMongoApplication.class, args);
	}

	/**
	 * This method will delete the existing data in the table and It will insert new record to Mongo DB
	 * @param employeeRepository
	 * @return
	 */
	@Bean
	public CommandLineRunner employees(@Autowired EmployeeRepository employeeRepository) {
		return args -> {
			employeeRepository.deleteAll().subscribe(null, null, () -> saveStaticData(employeeRepository));
		};
	}

	private static void saveStaticData(EmployeeRepository employeeRepository) {
		getStreamOfStaticEmployeeData()
		.forEach(employee -> {
			employeeRepository.save(employee).subscribe(System.out::println);
		});
	}

	private static Stream<Employee> getStreamOfStaticEmployeeData() {
		return Stream.of(
				new Employee(UUID.randomUUID().toString(), "Dhananjay", 6522),
				new Employee(UUID.randomUUID().toString(), "Liza", 5000),
				new Employee(UUID.randomUUID().toString(), "Baby", 8000),
				new Employee(UUID.randomUUID().toString(), "Mita", 2000)
			);
	}

}
