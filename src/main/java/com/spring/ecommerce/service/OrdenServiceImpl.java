package com.spring.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService{

	@Autowired
	private IOrdenRepository ordenRepository;
	
	@Override
	public Orden save(Orden orden) {
		
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		
		return ordenRepository.findAll();
	}
	
	
	public String generarNumeroOrden() {
		int numero = 0;
		String numeroConcatenado = "";
		
		List<Orden> ordenes = findAll();
		
		List<Integer> numeros = new ArrayList<Integer>();
  		
		//Le paso el numero de orden de la lista ordenes en formato String y recorro toda la lista para pasar a formato Int
		// -> Función anonima
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		
		if(ordenes.isEmpty()) {
			numero = 1;
		}else {
			//Utilizo una funcion de java 8 que es max, le paso el tipo de variable a comparar
			//Y utilizo la funcion get para obtener la comparación de los números de orden y obtener el máximo
			numero = numeros.stream().max(Integer::compare).get();
			numero++;
		}
		
		if(numero<10) {
			numeroConcatenado = "000000000"+String.valueOf(numero);
		}else if(numero<100){
			numeroConcatenado = "00000000"+String.valueOf(numero);
		}
		else if(numero<1000){
			numeroConcatenado = "0000000"+String.valueOf(numero);
		}
		else if(numero<10000){
			numeroConcatenado = "000000"+String.valueOf(numero);
		}
		else if(numero<100000){
			numeroConcatenado = "00000"+String.valueOf(numero);
		}
		
		
		return numeroConcatenado;
	}

}
