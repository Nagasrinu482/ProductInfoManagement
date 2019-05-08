package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;

public class ProductController {

	@Autowired
	private ProductService service;
	
	@RequestMapping("/")
	public String viewProducts(Model model)
	{
		List<Product> products=service.findAll();
		
		model.addAttribute("products",products);
		
		return "index";
	}
	
	@RequestMapping("/new")
	public String addProduct(Model model)
	{
		model.addAttribute("product",new Product());
		return "addproduct";
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String addProduct(@ModelAttribute("product") Product product)
	{
		service.save(product);
		return "redirect:/";
	}
	
	@RequestMapping("/edit/{id}")
	public ModelAndView viewProduct(@PathVariable(name ="id") int id)
	{
		ModelAndView modelAndView=new ModelAndView("editproduct");
		modelAndView.addObject("product",service.findById(id));
		return modelAndView;
	}
	
	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name ="id") int id)
	{
		service.delete(id);
		return "redirect:/";
	}
}

