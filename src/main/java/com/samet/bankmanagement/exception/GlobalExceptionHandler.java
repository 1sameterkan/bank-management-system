package com.samet.bankmanagement.exception;

import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice		// Spring'e bu sınıfın Controller'lar genelindeki hata yönetimine katılacağını söyler.
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidatiyonException(MethodArgumentNotValidException ex){
		
		List<String> messages = ex.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " +error.getDefaultMessage()).toList();
		
		Map<String, Object> response = new LinkedHashMap<String, Object>();
		response.put("status", 400);
		response.put("error", "Validation failed");
		response.put("messages", messages);
		
		return ResponseEntity.badRequest().body(response);
	}

}
