package com.example.demo.controllers;

import java.util.List;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Product;
import com.example.demo.models.Tag;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.TagRepo;
import com.example.demo.repo.CategoryRepo;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final TagRepo tagRepo;

    @Autowired
    // Dependency injection = when Spring Boot creates an instance
    // of ProductController, it will automatically create an instance
    // of ProductRepo and pass it to the new instance of ProductController
    public ProductController(ProductRepo productRepo, CategoryRepo categoryRepo, TagRepo tagRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.tagRepo = tagRepo;
    }
    
    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = productRepo.findAllWithCategoriesAndTags();
        model.addAttribute("products", products);
        return "products/index";
    }

    // When we want to add forms, we always need 2 routes
    // 1 route to display the form
    // 1 route to process the form
    @GetMapping("/products/create")
    public String showCreateProductForm(Model model) {

        // send an empty instance of the Product model to the template
        var newProduct = new Product();

        // find all the categories and add it to the view model 
        model.addAttribute("categories", categoryRepo.findAll());

        // add the instance of the new product model to the view model
        model.addAttribute("product", newProduct);

        // get all the tags and add it to the view model
        model.addAttribute("allTags", tagRepo.findAll());

        return "products/create";
    }

    // the results of the validation will be in the bindingResult parameter
    @PostMapping("/products/create")
    public String processCreateProductForm(@Valid @ModelAttribute Product newProduct, 
        BindingResult bindingResult,
        Model model,
        @RequestParam(required=false) List<Long> tagIds
        ) {
    
        if (bindingResult.hasErrors()) {
            
            model.addAttribute("categories", categoryRepo.findAll());
            model.addAttribute("allTags", tagRepo.findAll());
            
            // re-render the create form if there are any validation errors
            // and skip the saving of the new product
            return "products/create";
        }

        // check if the user has selected any tags
        if (tagIds != null) {
            var tags = new HashSet<Tag>(tagRepo.findAllById(tagIds));
            newProduct.setTags(tags);
        }

        // save the new product
        productRepo.save(newProduct);

        // a redirect tell the client to go a different URL
        return "redirect:/products";
    }

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) {

        // find the product with the matching id
         Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException());

        System.out.println(product);

        // add it to the view model
        model.addAttribute("product", product);

        // details.html in the products folder
        return "products/details";
    }

  
    // 2. display the form which contains the existing data of the product
    @GetMapping("/products/{id}/edit")
    public String showUpdateProduct(@PathVariable Long id, Model model) {

        // 1. find by ID the product that the user wants to update
        var product = productRepo.findById(id)
                        .orElseThrow( () -> new RuntimeException("Product Not Found"));

        // find all the categories
        model.addAttribute("categories", categoryRepo.findAll());

        // find all the tags and add them to the view model
        model.addAttribute("allTags", tagRepo.findAll());
        
        
        // 2. Pass it to the view model
        model.addAttribute("product", product);

        return "products/edit";

    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id, 
            @Valid @ModelAttribute Product product, 
            @RequestParam List<Long> tagIds,
            BindingResult bindingResult, Model model) {
        product.setId(id);  // ensure that we are updating the correct id
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("categories",categoryRepo.findAll());
            model.addAttribute("allTags", tagRepo.findAll());
            return "redirect:/products/" + id + "/edit";
        }
        
        // update the tags on the product
        if (tagIds != null && !tagIds.isEmpty()) {
            var tags = new HashSet<Tag>(tagRepo.findAllById(tagIds));
            product.setTags(tags);
        }  else {
            product.getTags().clear();
        }

        productRepo.save(product);
        return "redirect:/products";
    }

    // we have two routes for deleting
    // 1. showing a delete form (asking the user if they really want to delete)

    @GetMapping("/products/{id}/delete")
    public String showDeleteProductForm(@PathVariable Long id, Model model) {

        // find the product that we want to delete
        var product = productRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("Proudct not found"));

       model.addAttribute("product", product);                 

        return "products/delete";
    }

    // 2. process the delete
    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }
}