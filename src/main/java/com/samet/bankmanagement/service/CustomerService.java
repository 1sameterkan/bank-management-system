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

	private final CustomerRepository customerRepository;
	
	public CustomerService(CustomerRepository customerRepostitory) {
		this.customerRepository = customerRepostitory;
	}
	
	public List<Customer> getAllCustomers(){
		return customerRepository.findAll();
	} 
	
	public Customer createCustomer(Customer customer) {
		return customerRepository.save(customer);
	}
	
	public Customer getCustomerById(Long id) {
		return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bu id'ye sahip müsteri yok : " +id));
	}
	
	public Customer updateCustomer(Long id, Customer updateCustomer) { // Customer updateCustomer --> Güncelleyecek olan customer'ı alıyoruz.(Buradaki değerleri)
		
		// Customer existingCustomer --> Zaten var olan güncelleyeceğimiz mevcut customer
		Customer existingCustomer = getCustomerById(id);	// Güncellenecek olan customer'ın id sini alıyoruz. var mı yok mu kontrolü.
		
		existingCustomer.setFirstName(updateCustomer.getFirstName());
		existingCustomer.setLastName(updateCustomer.getLastName());
		existingCustomer.setEmail(updateCustomer.getEmail());
		
		return customerRepository.save(existingCustomer);
	}
	
	public void deleteCustomer(Long id) {
		
		Customer existingCustomer = getCustomerById(id);	// Müşteri var mı yok mu id ile kontrolü.
		
		customerRepository.delete(existingCustomer);
	}
	
	
}
