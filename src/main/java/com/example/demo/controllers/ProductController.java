package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.CategoryRepo;

import jakarta.validation.Valid;

import com.example.demo.models.Product;

@Controller
public class ProductController {

    // when we want to add forms, we always need 2 routes
    // 1 route to display the form
    // 1 route to process the form
    
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    @Autowired
    // Dependency injection = when Spring Boot creates an instance of ProductController,
    // it will automatically create an instance of ProductRepo and pass it to the new instance of ProductController
    public ProductController(ProductRepo productRepo, CategoryRepo categoryRepo){
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping("/products/create")
    public String showCreateProductForm (Model model){
        //send an empty instance of the Product
        var newProduct = new Product();
        model.addAttribute("product", newProduct);
        model.addAttribute("categories", categoryRepo.findAll());
        return "products/create";
    }

    @PostMapping("/products/create")
    public String processCreateProduct(@Valid @ModelAttribute Product newProduct, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("categories", categoryRepo.findAll());
            return "products/create";
        }
        productRepo.save(newProduct);
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String listProducts(Model model){
        List<Product> products = productRepo.findAllWithCategories();
        model.addAttribute("products", products);
        return "products/index";
    }

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
      var product = productRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Product not found"));
      model.addAttribute("product", product);
      return "products/details";
    }

    @GetMapping("/products/{id}/edit")
    public String showUpdateProduct(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id).orElseThrow( () -> new RuntimeException("Product not found"));
        model.addAttribute(product);
        model.addAttribute("categories", categoryRepo.findAll());
        return "products/edit";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        //product.setId(id); // Ensure we're updating the correct product
        if(bindingResult.hasErrors()){
            model.addAttribute("categories", categoryRepo.findAll());
            return "products/edit";
        }
        productRepo.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}/delete")
    public String showDeleteProductForm(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id)
               .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "products/delete";
   }
   
   @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }

}
