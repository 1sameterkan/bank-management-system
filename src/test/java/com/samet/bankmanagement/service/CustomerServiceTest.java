package com.samet.bankmanagement.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.samet.bankmanagement.entity.Customer;
import com.samet.bankmanagement.repository.CustomerRepository;

/*
Testimiz şu soruya cevap verecek:

Repository iki müşteri döndürürse CustomerService gerçekten bu iki müşteriyi bize geri veriyor mu?

Burada Mockito kullanacağız:
- @Mock: Gerçek veritabanına bağlanmayan sahte Repository oluşturur.
- @InjectMocks: Sahte Repository'yi gerçek CustomerService nesnesine enjekte eder.
- when().thenReturn(): Sahte Repository'nin hangi sonucu döndüreceğini belirler.
- assertEquals(): Beklediğimiz sonuç ile gerçek sonucu karşılaştırır.
- verify(): Repository metodunun gerçekten çağrıldığını kontrol eder.
Bu sayede PostgreSQL'i çalıştırmadan Service sınıfını test edebiliriz.
 */

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	
	@Mock
	private CustomerRepository customerRepository;
	
	@InjectMocks
	private CustomerService customerService;
	
	@Test
	void getAllCustomers_shouldReturnAllCustomers() {
	// 1. Test için iki müşteri oluşturuyoruz.
        Customer customer1 = new Customer();
        customer1.setFirstName("Ahmet");
        customer1.setLastName("Yilmaz");
        customer1.setEmail("ahmet@example.com");

        Customer customer2 = new Customer();
        customer2.setFirstName("Mehmet");
        customer2.setLastName("Kaya");
        customer2.setEmail("mehmet@example.com");
        
        List<Customer> customers = List.of(customer1, customer2);	// List.of --> Değiştirilemez, sonradan ekleme yapılamaz.                                                        
        
     // 2. Repository'nin ne döndüreceğini belirliyoruz.
        when(customerRepository.findAll()).thenReturn(customers);
        
     // 3. Test ettiğimiz metodu çalıştırıyoruz.
        List<Customer> result = customerService.getAllCustomers();
        
     // 4. Sonuçları kontrol ediyoruz.
        assertEquals(2, result.size());
        assertEquals("Ahmet", result.get(0).getFirstName());
        assertEquals("Mehmet", result.get(1).getFirstName());
        
     // 5. Repository metodunun çağrıldığını doğruluyoruz.
        verify(customerRepository).findAll();
        
	}
	
	@Test
	void createCustomer_shouldSaveAndReturnCustomer() {
		
		 // 1. Test edeceğimiz müşteriyi oluşturuyoruz.
		Customer customer = new Customer();
		
		customer.setFirstName("Ali");
		customer.setLastName("Demir");
		customer.setEmail("ali@example.com");
		
		 // 2. Repository'nin kaydedilen müşteriyi geri döndürmesini sağlıyoruz.
		when(customerRepository.save(customer)).thenReturn(customer);
		
		// 3. Test ettiğimiz metodu çalıştırıyoruz.
		Customer resultCustomer = customerService.createCustomer(customer);
		
		// 4. Dönen müşterinin bilgilerini kontrol ediyoruz.
		//assertEquals : (Beklediğimiz değer ile gerçekten elde ettiğimiz değerin eşit olup olmadığını kontrol etmek) --> assertEquals(beklenenDeger, gercekDeger);
		assertEquals("Ali", resultCustomer.getFirstName());
		assertEquals("Demir", resultCustomer.getLastName());
		assertEquals("ali@example.com", resultCustomer.getEmail());
		
		// 5. Repository save metodunun çağrıldığını doğruluyoruz.
		verify(customerRepository).save(customer);
		
	}
	
	@Test
	void getCustomerById_shouldReturnCustomer() {
		
		// 1. Test edeceğimiz müşteriyi oluşturuyoruz.
		Customer customer = new Customer();
		
		customer.setFirstName("Samet");
		customer.setLastName("Erkan");
		customer.setEmail("samet@example.com");
		
		// 2. ID belirliyoruz.
		Long customerId = 1L;
		
	    // 3. Repository'nin bu ID için müşteriyi döndürmesini sağlıyoruz.
		when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));
		
	    // 4. Test ettiğimiz metodu çalıştırıyoruz.
		Customer result = customerService.getCustomerById(customerId);
		
	    // 5. Sonuçları kontrol ediyoruz.
		assertEquals("Samet", result.getFirstName());
		assertEquals("Erkan", result.getLastName());
		assertEquals("samet@example.com", result.getEmail());
		
	    // 6. Repository'nin doğru ID ile çağrıldığını doğruluyoruz.
		verify(customerRepository).findById(customerId);
	}

	@Test
	void getCustomerById_shouldThrowExceptionWhenCustomerNotFound() {
		
	    // 1. Var olmayan bir müşteri ID'si belirliyoruz.
		Long customerId = 999L;
		
	    // 2. Repository'nin bu ID için boş sonuç döndürmesini sağlıyoruz.
		when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.empty());
		
	    // 3. Metodun ResponseStatusException fırlatmasını bekliyoruz.
		org.springframework.web.server.ResponseStatusException exception = 
				org.junit.jupiter.api.Assertions.assertThrows(
						org.springframework.web.server.ResponseStatusException.class,
						() -> customerService.getCustomerById(customerId)
				);
		
	    // 4. HTTP durum kodunun 404 olduğunu kontrol ediyoruz.
		assertEquals(org.springframework.http.HttpStatus.NOT_FOUND, exception.getStatusCode());
		
	    // 5. Repository'nin doğru ID ile çağrıldığını doğruluyoruz.
		verify(customerRepository).findById(customerId);
		
	}
	
	@Test
	void updateCustomer_shouldUpdateAndReturnCustomer() {
		
	    // 1. Veritabanında zaten var olduğunu varsaydığımız müşteriyi oluşturuyoruz.
		Long customerId = 1L;
		
		Customer existingCustomer = new Customer(); // Var olan müşteri.
		existingCustomer.setFirstName("Ahmet");
		existingCustomer.setLastName("Yilmaz");
		existingCustomer.setEmail("ahmet@example.com");
		
	    // 2. Güncelleme için göndereceğimiz yeni müşteri bilgilerini oluşturuyoruz.
		Customer updatedCustomer = new Customer();
		updatedCustomer.setFirstName("Mehmet");
		updatedCustomer.setLastName("Kaya");
		updatedCustomer.setEmail("mehmet@example.com");
		
/*
Bunlar neden ayrı?
- existingCustomer: Sistemde zaten kayıtlı olan müşteri.
- updatedCustomer: Kullanıcının güncellemek istediği yeni bilgiler.
Service sınıfımız yeni bir müşteri oluşturmak yerine mevcut müşterinin bilgilerini değiştiriyor.		
*/
		
	    // 3. Repository'nin müşteriyi bulacağını varsayıyoruz.
		when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(existingCustomer));
		
	    // 4. Repository'nin kaydettiği müşteriyi geri döndürmesini sağlıyoruz.
		when(customerRepository.save(existingCustomer)).thenReturn(existingCustomer);
		
	    // 5. Güncelleme metodunu çalıştırıyoruz.
		Customer result = customerService.updateCustomer(customerId, updatedCustomer);
		
	    // 6. Güncellenen bilgileri kontrol ediyoruz.
		assertEquals("Mehmet", result.getFirstName());
		assertEquals("Kaya", result.getLastName());
	    assertEquals("mehmet@example.com", result.getEmail());
		
	    // 7. Repository metotlarının çağrıldığını doğruluyoruz.
	    verify(customerRepository).findById(customerId);
	    verify(customerRepository).save(existingCustomer);
		
	}	
	
	@Test
	void deleteCustomer_shouldDeleteCustomer() {
	
	    // 1. Silinecek müşteriyi oluşturuyoruz.
		Long customerId = 1L;
		
		Customer customer = new Customer();
		customer.setFirstName("Ahmet");
	    customer.setLastName("Yilmaz");
	    customer.setEmail("ahmet@example.com");
		
	    // 2. Repository'nin müşteriyi bulmasını sağlıyoruz.
	    when(customerRepository.findById(customerId)).thenReturn(java.util.Optional.of(customer));
	    
	    // 3. Silme metodunu çalıştırıyoruz.
	    customerService.deleteCustomer(customerId);	// Bu metot void döndürüyor. Yani bize bir Customer nesnesi vermiyor.
                                                    // Bu nedenle assertEquals() kullanarak dönen bir müşteri kontrol etmiyoruz.   
	    
	    // 4. Repository metotlarının çağrıldığını doğruluyoruz.
	    verify(customerRepository).findById(customerId);
	    verify(customerRepository).delete(customer);
	    
	}
	
	@Test
	void deleteCustomer_shouldThrowExceptionWhenCustomerNotFound() {
		 
		// 1. Veritabanında bulunmayan müşteri ID'si.
	    Long customerId = 999L;

	    // 2. Repository müşteriyi bulamasın.
	    when(customerRepository.findById(customerId))
	            .thenReturn(java.util.Optional.empty());

	    // 3. Silme işleminin hata fırlatmasını bekliyoruz.
	    ResponseStatusException exception = assertThrows(
	            ResponseStatusException.class,
	            () -> customerService.deleteCustomer(customerId)
	    );

	    // 4. Hatanın HTTP 404 olduğunu doğruluyoruz.
	    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
	}
 
	
}
