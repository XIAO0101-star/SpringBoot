package com.example.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


// A controller advice is applied to all other controllers
// (you can put filtering rules to state for a particular advice, which URL it applies to)
@ControllerAdvice
public class GlobalControllerAdvice {
    
    // This advice is applied whenever the templates refer to the `currentUser` variable
    @ModelAttribute("currentUser")
    public String getCurrentUser() {
        // get the current authenticatio context (i.e, the current details of the logged in user)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // if no user is logged in, return
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        // return the name of he currently logged in user
        return authentication.getName();

    }

     // This advice is applied whenever the templates refer to the `currentUser` variable
     @ModelAttribute("isAuthenticated")
     public boolean isAuthenticated() {
         // get the current authenticatio context (i.e, the current details of the logged in user)
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication.getName().equals("anonymousUser")) {
            return false;
         }
         return true;
 
     }

}