package com.samet.bankmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

//Müşterinin hangi bilgilere sahip olduğunu tanımlar.
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Ad boş bırakılamaz!")	// Kontrol
	@Column(nullable = false)
	private String firstName;
	
	@NotBlank(message = "Soyad boş bırakılamaz!")	// Kontrol
	@Column(nullable = false)
	private String lastName;
	
	@NotBlank(message = "E-posta boş bırakılamaz!")		// Kontrol
	@Email(message = "Geçerli bir e-posta adresi giriniz!")		// Kontrol
	@Column(nullable = false, unique = true)
	private String email;
	
	
	public Customer() {
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
