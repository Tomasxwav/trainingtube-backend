package com.traini.traini_backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class UsersController {

    /**
     * Check if a string is a palindrome
     * @param id String to check
     * @return String "Palindrome" if the string is a palindrome, otherwise "Not a palindrome"
     */
    @GetMapping("/users")
    public String GetUsers(@PathVariable String id) {
        String[] letters = id.split("");

        for (int i = 0; i < letters.length; i++) {
            if (!letters[i].equalsIgnoreCase(letters[letters.length - 1 - i])) {
                return "Not a palindrome";
            }
        }
        return "Palindrome";
    }

    @GetMapping("/users/{id}")
    public String GetUser(@PathVariable String id) {
        String[] letters = id.split("");

        for (int i = 0; i < letters.length; i++) {
            if (!letters[i].equalsIgnoreCase(letters[letters.length - 1 - i])) {
                return "Not a palindrome";
            }
        }
        return "Palindrome";
    }

    @PostMapping("path")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}

