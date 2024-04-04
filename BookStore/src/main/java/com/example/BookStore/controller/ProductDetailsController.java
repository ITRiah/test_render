package com.example.BookStore.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.BookStore.dto.ProductDetailsDTO;
import com.example.BookStore.dto.ResponseDTO;
import com.example.BookStore.dto.SearchDTO;
import com.example.BookStore.repo.ProductRepo;
import com.example.BookStore.service.ProductDetailsService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin/product-details")
public class ProductDetailsController {

	@Autowired
	ProductDetailsService ProductDetailsService;
	
	@Autowired
	ProductRepo productRepo;

	@PostMapping("/")
	public ResponseDTO<Void> create(@ModelAttribute ProductDetailsDTO ProductDetailsDTO) throws IllegalStateException, IOException {
		MultipartFile file = ProductDetailsDTO.getFile();		
		
		if(file != null) {
			String fileName = file.getOriginalFilename();
			String filePath = "E:\\Studying\\Java Backend\\Springboot\\BookStore\\src\\main\\java\\com\\example\\BookStore\\image\\product\\"
					+ fileName;
			file.transferTo(new File(filePath));
			ProductDetailsDTO.setImage(filePath);
		}
		
		ProductDetailsService.create(ProductDetailsDTO);
		return ResponseDTO.<Void>builder()
				.status(200)
				.msg("ok")
				.build();

	}

	@GetMapping("/")
	public ResponseDTO<Page<ProductDetailsDTO>> getAll(@ModelAttribute SearchDTO searchDTO) {
		return ResponseDTO.<Page<ProductDetailsDTO>>builder().status(200).msg("ok")
				.data(ProductDetailsService.getAll(searchDTO)).build();
	}
	
	@GetMapping("/get-by-product-id")
	public ResponseDTO<List<ProductDetailsDTO>> getByProductId(@RequestParam("id") int id) {
		return ResponseDTO.<List<ProductDetailsDTO>>builder()
				.status(200)
				.msg("ok")
				.data(ProductDetailsService.findByProductId(id))
				.build();
	}


	@GetMapping("/download")
	public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) throws IOException {
		File file = new File(
				"E:\\Studying\\Java Backend\\Springboot\\BookStore\\src\\main\\java\\com\\example\\BookStore\\image\\product\\"
						+ fileName);
		Files.copy(file.toPath(), response.getOutputStream());// lấy dữ liệu từ file để tải về hình ảnh cho web
	}
}
