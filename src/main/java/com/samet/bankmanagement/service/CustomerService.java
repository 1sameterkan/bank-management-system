package com.samet.bankmanagement.service;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.samet.bankmanagement.entity.Customer;
import com.samet.bankmanagement.repository.CustomerRepository;

//ileride e-posta kontrolü, müşteri kuralları veya hesap açma işlemleri gerektiğinde bunları nereye yazacağız? İşte bunun için CustomerService oluşturuyoruz.

@Service
public class CustomerService {

	private final CustomerRepository customerRepostitory;
	
	public CustomerService(CustomerRepository customerRepostitory) {
		this.customerRepostitory = customerRepostitory;
	}
	
	public List<Customer> getAllCustomers(){
		return customerRepostitory.findAll();
	} 
	
	public Customer createCustomer(Customer customer) {
		return customerRepostitory.save(customer);
	}
	
	public Customer getCustomerById(Long id) {
		return customerRepostitory.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bu id'ye sahip müsteri yok : " +id));
	}
}
