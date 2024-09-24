package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorsMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessages);
            }
            return ResponseEntity.ok().body("Successfully");

    }catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());}
    }

    @GetMapping("/{user_id}")//Thêm biến đường dẫn "user_id"
    //GET http://localhost:8088/api/v1/orders/2
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId) {
        try {
            return ResponseEntity.ok().body("Lấy ra danh sách từ user_id");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{user_id}")//PUT http://localhost:8088/api/v1/orders/2
    //Công việc của admin
    public ResponseEntity<?> updateOrder(@Valid @PathVariable long id,
                                         @RequestBody @Valid OrderDTO orderDTO) {
        return ResponseEntity.ok().body("Successfully updated order");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable long id) {
        // Xóa mềm => cập nhật trường active = false
        return ResponseEntity.ok().body("Successfully deleted order");
    }

}
