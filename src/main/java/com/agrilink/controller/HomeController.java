package com.agrilink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String homePage() {
        return "index";  // Looks for templates/index.html
    }

    @GetMapping("/browse")
    public String browsePage() {
        return "browse";
    }
}
