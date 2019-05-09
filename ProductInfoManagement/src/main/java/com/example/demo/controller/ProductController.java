package com.example.demo.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.exception.ProductNotfoundException;
import com.example.demo.model.FileUploadBean;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import com.example.util.MediaTypeUtils;

@Controller
public class ProductController {

	@Autowired
	private ProductService service;

	@Autowired
	private ServletContext servletContext;

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
		if(!service.isExist(id))throw new ProductNotfoundException();
		ModelAndView modelAndView=new ModelAndView("editproduct");
		modelAndView.addObject("product",service.findById(id));
		return modelAndView;
	}

	@RequestMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name ="id") int id)
	{	
		if(!service.isExist(id))throw new ProductNotfoundException();
		service.delete(id);
		return "redirect:/";
	}

	//=============================================================================

	// GET: Show upload form page.
	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.GET)
	public String uploadMultiFileHandler(Model model) {

		FileUploadBean myUploadForm = new FileUploadBean();
		model.addAttribute("myUploadForm", myUploadForm);

		return "uploadMultiFile";
	}

	// POST: Do Upload
	@RequestMapping(value = "/uploadMultiFile", method = RequestMethod.POST)
	public String uploadMultiFileHandlerPOST(HttpServletRequest request, Model model, @ModelAttribute("myUploadForm") FileUploadBean myUploadForm) {

		return this.doUpload(request, model, myUploadForm);

	}

	@RequestMapping("/download/{fileName}")
	public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request,
			@PathVariable(name="fileName") String fileName) throws IOException {

		File file = new File(request.getServletContext().getRealPath("upload")+"/"+fileName);
		
		MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
		System.out.println("fileName: " + fileName);
		System.out.println("mediaType: " + mediaType);

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

		return ResponseEntity.ok()
				// Content-Disposition
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
				// Content-Type
				.contentType(mediaType)
				// Contet-Length
				.contentLength(file.length()) //
				.body(resource);
	}

	private String doUpload(HttpServletRequest request, Model model, FileUploadBean myUploadForm) {

		String description = myUploadForm.getDescription();
		System.out.println("Description: " + description);

		// Root Directory.
		String uploadRootPath = request.getServletContext().getRealPath("upload");
		System.out.println("uploadRootPath=" + uploadRootPath);

		File uploadRootDir = new File(uploadRootPath);
		// Create directory if it not exists.
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}

		MultipartFile[] fileDatas = myUploadForm.getFileDatas();

		List<File> uploadedFiles = new ArrayList<File>();
		List<String> failedFiles = new ArrayList<String>();

		for (MultipartFile fileData : fileDatas) {

			// Client File Name
			String name = fileData.getOriginalFilename();
			System.out.println("Client File Name = " + name);

			if (name != null && name.length() > 0) {
				try {
					// Create the file at server
					File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(fileData.getBytes());
					stream.close();
					//
					uploadedFiles.add(serverFile);
					System.out.println("Write file: " + serverFile);
				} catch (Exception e) {
					System.out.println("Error Write file: " + name);
					failedFiles.add(name);
				}
			}
		}
		model.addAttribute("description", description);
		model.addAttribute("uploadedFiles", uploadedFiles);
		model.addAttribute("failedFiles", failedFiles);
		return "uploadResult";
	}
}

