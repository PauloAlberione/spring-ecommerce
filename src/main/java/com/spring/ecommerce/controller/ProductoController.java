package com.spring.ecommerce.controller;


import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.spring.ecommerce.model.Producto;
import com.spring.ecommerce.model.Usuario;
import com.spring.ecommerce.service.IUsuarioService;
import com.spring.ecommerce.service.ProductoService;
import com.spring.ecommerce.service.UploadFileService;
import com.spring.ecommerce.service.UsuarioServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller 
@RequestMapping("/productos")
public class ProductoController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private UploadFileService upload;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	
	@PostMapping("/save")
	public String save(Producto producto, HttpSession sesssion, HttpSession session, @RequestParam("img") MultipartFile file) throws IOException {
		
		LOGGER.info("Este es el objeto de la vista {}", producto);
		
		Usuario u = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		producto.setUsuario(u);
		
		//Lógica para subir la imagen y guardarla en la base de datos
		
		//Verificamos si es la primera vez que se sube la imagen
		//Es decir, cuando se crea un producto, ya que no tiene ID
		if(producto.getId()==null) {
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			
		}
		
		productoService.save(producto);
		
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto= new Producto();
		
		Optional<Producto> optionalProducto= productoService.get(id);
		
		producto= optionalProducto.get();
		
		LOGGER.info("Producto buscado: {}",producto);
		model.addAttribute("producto", producto);
		
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		
		Producto p = new Producto();
		
		//Obtengo la imagen que tenia y se la paso al producto que se está editando
		p = productoService.get(producto.getId()).get();
		
		
		//Cuando se modifica un producto y se carga la misma imagen
		if(file.isEmpty()) {
			
			producto.setImagen(p.getImagen());
		}else {
			//Cuando se modifica el producto y se cambia la imagen cargada
			
			//Si la imagen cargada no es la que está por defecto
			if(!p.getImagen().equals("default.jpg")) {
				upload.deleteImage(p.getImagen());
			}
			
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		
		//Para que se mantenga el id del usuario al realizar cambios
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		
		Producto p = new Producto();
		p=productoService.get(id).get();
		
		//Si la imagen cargada no es la que está por defecto
		if(!p.getImagen().equals("default.jpg")) {
			upload.deleteImage(p.getImagen());
		}
		
		productoService.delete(id);
		
		return "redirect:/productos";
		
	}
	
}
