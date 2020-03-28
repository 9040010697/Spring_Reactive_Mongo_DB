package com.reactive.resource;

import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reactive.model.Employee;
import com.reactive.model.EmployeeEvent;
import com.reactive.repository.EmployeeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
@RequestMapping("/rest/employee")
public class EmployeeResource {

	@Autowired
	private EmployeeRepository employeeRepository;

	@GetMapping("/all")
	public Flux<Employee> getAll() {
		return employeeRepository.findAll();
	}

	@GetMapping("/{id}")
	public Mono<Employee> getId(@PathVariable("id") final String empId) {
		return employeeRepository.findById(empId);
	}

	@GetMapping(value = "/{id}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<EmployeeEvent> getEvents(@PathVariable("id") final String empId) {

		return employeeRepository.findById(empId).flatMapMany(employee -> {

			Flux<Long> interval = Flux.interval(Duration.ofSeconds(2));

			Flux<EmployeeEvent> employeeEventFlux = Flux
					.fromStream(Stream.generate(() -> new EmployeeEvent(employee, new Date())));

			// Combining results
			return Flux.zip(interval, employeeEventFlux).map(Tuple2::getT2);

		});
	}

	@GetMapping(value = "/timeEvent", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Date> getTimeEvents() {

		Flux<Long> interval = Flux.interval(Duration.ofSeconds(2));

		Flux<Date> dateEvent = Flux.fromStream(Stream.generate(() -> new Date()));

		// Combining results
		return Flux.zip(interval, dateEvent).map(Tuple2::getT2);

	}

}
