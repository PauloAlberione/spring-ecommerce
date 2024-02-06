package com.spring.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.model.Usuario;

@Repository
public interface IOrdenRepository extends JpaRepository<Orden, Integer> {

	List<Orden> findByUsuario(Usuario usuario);
	
}
