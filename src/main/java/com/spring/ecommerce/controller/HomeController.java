package com.spring.ecommerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.ecommerce.model.DetalleOrden;
import com.spring.ecommerce.model.Orden;
import com.spring.ecommerce.model.Producto;
import com.spring.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	private final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private ProductoService productoService;
	
	//Para almacenar los detalles de la orden de compra
	List<DetalleOrden> detalles= new ArrayList<DetalleOrden>();
	
	//Almacena los datos de la orden
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model) {
		
		model.addAttribute("productos", productoService.findAll());
		
		return "usuario/home";
	}
	
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		
		log.info("Id producto enviado como parámetro {}",id);
		
		Producto producto = new Producto();
		Optional<Producto> productoOptional = productoService.get(id);
		producto = productoOptional.get();	//Obtiene el producto de la base de datos
		
		model.addAttribute("producto", producto);
		
		
		return "usuario/productohome";
	}
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double sumaTotal = 0;
		
		Optional<Producto> optionalProducto = productoService.get(id);
		
		log.info("Producto añadido : {}", optionalProducto.get());
		log.info("Cantidad : {}", cantidad);
		
		producto = optionalProducto.get();
		
		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio()*cantidad);
		detalleOrden.setProducto(producto);
		
		//Validar que el producto no se añada 2 veces
		Integer idProducto = producto.getId();
		//El anyMatch es como un for, pero mas rapido para recorrer la Lista
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
		
		if(!ingresado) {
			//Con esto añado cada detalle orden mencionado anteriormente a la Lista
			detalles.add(detalleOrden);
		}
		
	
		//Utilizo un métodolo lambda
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		
		//Paso los datos del carrito para que se muestre en la vista del usuario
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	//Quitar producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductCart(@PathVariable Integer id, Model model) {
		
		//Nueva lista de productos
		List<DetalleOrden> ordenesNuevas = new ArrayList<DetalleOrden>();
		
		for(DetalleOrden detalleOrden : detalles) {
			if(detalleOrden.getProducto().getId()!=id) {
				ordenesNuevas.add(detalleOrden);
			}
		}
		
		//Agrego la nueva lista con los productos actualizados
		detalles=ordenesNuevas;
		
		double sumaTotal = 0;
		
		sumaTotal = detalles.stream().mapToDouble(dt->dt.getTotal()).sum();
		
		orden.setTotal(sumaTotal);
		
		//Paso los datos del carrito para que se muestre en la vista del usuario
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "usuario/carrito";
	}
	
	@GetMapping("/getCart")
	public String getCart(Model model) {
		
		
		
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		
		return "/usuario/carrito";
	}
	
}
