package com.project.shopapp.controllers;


import com.project.shopapp.dtos.ProductDTO;

import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.services.ProductService;
import com.project.shopapp.services.iservices.IProductService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;


@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;


    @GetMapping("") //http://localhost:8088/api/v1/products
    public ResponseEntity<String> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {
        return ResponseEntity.ok("getProducts here");
    }

    @GetMapping("/{id}")//http://localhost:8088/api/v1/products/6
    public ResponseEntity<String> getProductById(@PathVariable("id") String productId) {
        return ResponseEntity.ok("Product with ID: " + productId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable long id) {
        return ResponseEntity.ok(String.format("Product with id = %d deleted successfully", id));
    }

    @PostMapping(value = "")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorsMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessages);
            }
            Product newProduct = productService.createProduct(productDTO);

            return ResponseEntity.ok(newProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long productId,
                                         @ModelAttribute("files") List<MultipartFile> files) {
        try {
            Product existingProduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size()>ProductImage.MAXIMUM_IMAGE_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only upload 5 images");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if(file.getSize()==0){
                    continue;
                }
                // Kiểm tra kích thước tệp
                if (file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large! Maximum size is 10MB");
                }
                // Kiểm tra loại tệp`
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                // Lưu tệp và Cập nhật thumbnail trong DTO
                String filename = storeFile(file);
                // Lưu sản phẩm vào cơ sở dữ liệu
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(filename)
                                .build()
                );
                productImages.add(productImage);
                //Lưu vào bảng product_images
            }
            return ResponseEntity.ok(productImages);
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils
                .cleanPath(file.getOriginalFilename());
        // Thêm UUID vào tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến folder mà muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("upload");
        // Kiểm tra và tạo folder nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
}


