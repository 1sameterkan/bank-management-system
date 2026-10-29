package com.samet.bankmanagement.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samet.bankmanagement.entity.Customer;
import com.samet.bankmanagement.service.CustomerService;

import jakarta.validation.Valid;

/*
Şu an CustomerService sınıfımız müşterileri listeleyebiliyor ve müşteri kaydedebiliyor. 
Ancak bu metotları henüz tarayıcıdan veya Postman üzerinden çağırabileceğimiz bir adresimiz yok.
CustomerController, dışarıdan gelen HTTP isteklerini karşılayıp ilgili Service metodunu çağıracak.
 */

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CustomerService customerService;
	
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	@GetMapping
	public List<Customer> getAllCustomers(){
		return customerService.getAllCustomers();
	}
	
	@PostMapping
	public Customer createCustomers(@Valid @RequestBody Customer customer) {	// Controller'da @Valid kullanarak gelen JSON'un doğrulanmasını isteriz. Bu da Customer' da olur. 
		return customerService.createCustomer(customer);
	}
	
	@GetMapping("/{id}")
	public Customer getCustomerById(@PathVariable Long id) {
		return customerService.getCustomerById(id);
	}
	
	@PutMapping("/{id}")
	public Customer updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer updatedCustomer) {
		return customerService.updateCustomer(id, updatedCustomer);
	}
	
	@DeleteMapping("/{id}")
	public void deleteCustomer(@PathVariable Long id) {
		customerService.deleteCustomer(id);
	}
	
	
}



