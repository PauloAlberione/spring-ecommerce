package com.spring.ecommerce.service;

import org.springframework.stereotype.Service;

import com.spring.ecommerce.model.DetalleOrden;

@Service
public interface IDetalleOrdenService {

	DetalleOrden save(DetalleOrden detalleOrden);
}
