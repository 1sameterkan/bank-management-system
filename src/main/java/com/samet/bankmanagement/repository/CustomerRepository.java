package com.samet.bankmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samet.bankmanagement.entity.Customer;

//Müşteri verilerini veritabanında kaydetmemizi ve sorgulamamızı sağlar.
public interface CustomerRepository extends JpaRepository<Customer, Long>{

	
}
