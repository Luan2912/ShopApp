package com.project.shopapp.controllers;

import com.project.shopapp.dtos.OrderDTO;
import com.project.shopapp.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@RequestBody @Valid OrderDetailDTO orderDetailDTO,
                                         BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorsMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessages);
            }
            return ResponseEntity.ok().body("Create orderDetail here");

        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());}
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body("Get orderDetail with id " + id);

    }

    //Lấy ra danh sách các order_detail của một order nào đó
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetailByOrderId(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok().body("Get orderDetail with orderId = " + orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable("id") Long id,
                                               @RequestBody OrderDetailDTO orderDetailData) {
        return ResponseEntity.ok().body("Update orderDetail with id = " + id + ", newOrderDetailData: " + orderDetailData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@PathVariable("id") Long id) {
        return ResponseEntity.noContent().build();
    }



}