package com.spring.ecommerce.service;

import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Orden;

@Service
public interface IOrdenService {
	
	Orden save (Orden orden);
	
}
