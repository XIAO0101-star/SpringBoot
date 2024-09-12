package com.example.demo.controllers;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// The @Controller annoation tells Spring Boot that this is a controller that contains routes
// Spring Boot will automatically go through all the methods in routes
@Controller
public class Homecontroller {

    // When the user goes to the / URL on the server, call this method
    @GetMapping("/")
    // Tell Spring Boot this methods returns a HTTP response
    @ResponseBody
    public String helloworld(){
        return "<h1>Hello World<h1>";
    }

    @GetMapping("/about-us")
    // if the route is not marked with
    // @ResponseBody, then by default we are using templates
    // The Model model parameter is automatically passed to aboutUs when is called by Spring.
    // This is known as the "view model" and it also allows us to inject variables into out templates.
    public String aboutUs(Model model){

        // get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        model.addAttribute("currentDateTime", currentDateTime);
        return "about-us";
    }

    @GetMapping("/contact-us")
    public String contactUs(){
        return "contact-us";
    }
}
