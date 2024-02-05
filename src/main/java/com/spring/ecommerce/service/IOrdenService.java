package com.spring.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Orden;

@Service
public interface IOrdenService {
	
	List<Orden> findAll();
	
	Orden save (Orden orden);
	String generarNumeroOrden();
}
