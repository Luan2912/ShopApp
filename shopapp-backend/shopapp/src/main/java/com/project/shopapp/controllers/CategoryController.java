package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.services.iservices.ICategoryService;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Builder
@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
@RequiredArgsConstructor
public class CategoryController {

    //injection Dempendence
    private final ICategoryService categoryService;

    @PostMapping("")
    //Nếu tham số truyền vào là một obj thì sao? => Data Transfer Object = Request Object
    public ResponseEntity<?> createCategory(@Valid  @RequestBody List<CategoryDTO> categoryDTOList,
                                            BindingResult result)


    {
        if(result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);

        }
    for(CategoryDTO categoryDTO : categoryDTOList){
        categoryService.createCategory(categoryDTO);
    }
        return ResponseEntity.ok("Insert category successfully");
    }


    //Hiển thị tất cả Categories
    @GetMapping("") //http://localhost:8088/api/v1/categories?page=1&limit=10
    public ResponseEntity<?> getAllCategories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Category> categories=categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable int id,
                                                @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok("Update category successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete category with id = "+id+" successfully");
    }

}
