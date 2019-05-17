package com.example.demo.rest.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/api/v1")
public class ProductRestController {

	@Autowired
	private ProductService service;

	@RequestMapping("/")
	public ResponseEntity<List<Product>> viewProducts()
	{
		List<Product> products=service.findAll();
		return ResponseEntity.ok(products);
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product)
	{
		service.save(product);
		
		return ResponseEntity.ok(product);
	}
	
	@RequestMapping("/get/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable(name ="id") int id)
	{
		if(!service.isExist(id)) {
            ResponseEntity.badRequest().build();
        }
		
		return ResponseEntity.ok(service.findById(id));
	}

	@RequestMapping("/delete/{id}")
	public ResponseEntity deleteProduct(@PathVariable(name ="id") int id)
	{	
		if(!service.isExist(id)) {
            ResponseEntity.badRequest().build();
        }

		service.delete(id);

        return ResponseEntity.ok().build();
	}
}

