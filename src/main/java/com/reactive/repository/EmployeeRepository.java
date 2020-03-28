package com.reactive.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.reactive.model.Employee;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}
