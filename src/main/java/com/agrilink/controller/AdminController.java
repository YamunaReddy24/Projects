package com.agrilink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.agrilink.repository.OrderRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders")
    public String viewAllOrders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "admin_orders";
    }
}
