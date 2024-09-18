package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// The @Controller annoation tells Spring Boot that this is a controller that contains routes
// Spring Boot will automatically go through all the methods marked as routes
// and register them
@Controller
public class HomeController {
    
    // When the user goes to the / URL on the server, call this method
    @GetMapping("/")
    // Tell Spring Boot this methods returns a HTTP response
    @ResponseBody
    public String helloWorld() {
        return "<h1>Hello World</h1>";
    }

    @GetMapping("/about-us")
    // if the route is  not marked with
    // @ResponeBody, then by default we are using templates
    // 
    // The `Model model` parameter is automatically passed
    // to aboutUs when is called by Spring. This is known 
    // as the 'view model' and it also allows us to inject
    // variables into our template
    public String aboutUs(Model model) {

        // get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // the view model is automatically passed to the template
        // and any attributes in it will be available as variables
        model.addAttribute("currentDateTime", currentDateTime);

        // we need to return the file path (without extension) to the template
        // RELATIVE To resources/templates
        return "about-us";
    }

    @GetMapping("/contact-us")
    public String contactUs() {
        return "contact-us";
    }
}