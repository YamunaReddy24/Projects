package com.agrilink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.agrilink.model.Farmer;
import com.agrilink.model.FarmerQuery;
import com.agrilink.model.ExpertResponse;
import com.agrilink.model.Order;
import com.agrilink.service.FarmerService;
import com.agrilink.service.FarmerQueryService;
import com.agrilink.service.ExpertResponseService;
import com.agrilink.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/farmer")
public class FarmerController {

    @Autowired
    private FarmerService farmerService;

    @Autowired
    private FarmerQueryService farmerQueryService;

    @Autowired
    private ExpertResponseService expertResponseService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("farmer", new Farmer());
        return "farmer_register";
    }

    @PostMapping("/register")
    public String registerFarmer(@ModelAttribute Farmer farmer, Model model) {
        farmerService.registerFarmer(farmer);
        model.addAttribute("successMessage", "Registration successful! Please login now.");
        model.addAttribute("farmer", new Farmer());
        return "farmer_login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("farmer", new Farmer());
        return "farmer_login";
    }

    @PostMapping("/login")
    public String loginFarmer(@ModelAttribute Farmer farmer, Model model, HttpSession session) {
        Farmer loggedInFarmer = farmerService.loginFarmer(farmer.getEmail(), farmer.getPassword());
        if (loggedInFarmer != null) {
            session.setAttribute("farmer", loggedInFarmer);
            return "redirect:/farmer/dashboard";
        } else {
            model.addAttribute("errorMessage", "Invalid email or password!");
            return "farmer_login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        model.addAttribute("farmer", farmer);
        model.addAttribute("queries", farmerQueryService.getQueriesByFarmer(farmer));
        return "farmer_dashboard";
    }

    @GetMapping("/ask-question")
    public String showAskQuestionForm(HttpSession session, Model model) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        model.addAttribute("farmer", farmer);
        return "farmer_ask_question";
    }

    @PostMapping("/ask-question")
    public String askQuestion(@RequestParam String subject, @RequestParam String question, HttpSession session) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        farmerQueryService.createQuery(farmer, subject, question);
        return "redirect:/farmer/dashboard";
    }

    @GetMapping("/query-history")
    public String showQueryHistory(HttpSession session, Model model) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        model.addAttribute("farmer", farmer);
        model.addAttribute("queries", farmerQueryService.getQueriesByFarmer(farmer));
        return "farmer_query_history";
    }

    @GetMapping("/query-detail/{queryId}")
    public String showQueryDetail(@PathVariable Long queryId, HttpSession session, Model model) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        FarmerQuery query = farmerQueryService.getQueryById(queryId);
        if (query == null || !query.getFarmer().getId().equals(farmer.getId())) {
            return "redirect:/farmer/query-history";
        }
        model.addAttribute("farmer", farmer);
        model.addAttribute("query", query);
        model.addAttribute("responses", expertResponseService.getResponsesByQuery(query));
        return "farmer_query_detail";
    }

    @GetMapping("/orders")
    public String showFarmerOrders(HttpSession session, Model model) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        List<Order> orders = orderRepository.findByCropFarmerId(farmer.getId());
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", Order.OrderStatus.values());
        return "farmer_orders";
    }

    @PostMapping("/orders/update-status/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, 
                                    @RequestParam Order.OrderStatus status, 
                                    HttpSession session) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getCrop().getFarmerId().equals(farmer.getId())) {
                order.setStatus(status);
                orderRepository.save(order);
            }
        }
        return "redirect:/farmer/orders";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
