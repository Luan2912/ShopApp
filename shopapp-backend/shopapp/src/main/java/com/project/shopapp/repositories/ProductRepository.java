package com.project.shopapp.repositories;

import com.project.shopapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //Kiểm tra tên sản phẩm có tồn tại hay không
    boolean existsByName(String name);

//    Page<Product> findAll(Pageable pageable);//Phân trang
}
