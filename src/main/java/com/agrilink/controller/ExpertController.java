package com.agrilink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.agrilink.model.Expert;
import com.agrilink.service.ExpertService;
import com.agrilink.service.FarmerQueryService;
import com.agrilink.service.ExpertResponseService;
import com.agrilink.model.FarmerQuery;
import com.agrilink.model.ExpertResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/expert")
public class ExpertController {

    @Autowired
    private ExpertService expertService;

    @Autowired
    private FarmerQueryService farmerQueryService;

    @Autowired
    private ExpertResponseService expertResponseService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("expert", new Expert());
        return "expert_register";
    }

    @PostMapping("/register")
    public String registerExpert(@ModelAttribute Expert expert, Model model) {
        expertService.registerExpert(expert);
        model.addAttribute("successMessage", "Registration successful! Please login now.");
        model.addAttribute("expert", new Expert());
        return "expert_login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("expert", new Expert());
        return "expert_login";
    }

    @PostMapping("/login")
    public String loginExpert(@ModelAttribute Expert expert, Model model, HttpSession session) {
        Expert loggedInExpert = expertService.loginExpert(expert.getEmail(), expert.getPassword());
        if (loggedInExpert != null) {
            session.setAttribute("expert", loggedInExpert);
            return "redirect:/expert/dashboard";
        } else {
            model.addAttribute("errorMessage", "Invalid email or password!");
            return "expert_login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Expert expert = (Expert) session.getAttribute("expert");
        if (expert == null) {
            return "redirect:/expert/login";
        }
        model.addAttribute("expert", expert);
        model.addAttribute("pendingQueries", farmerQueryService.getQueriesByStatus("PENDING"));
        return "expert_dashboard";
    }

    @GetMapping("/reply/{queryId}")
    public String showReplyForm(@PathVariable Long queryId, HttpSession session, Model model) {
        Expert expert = (Expert) session.getAttribute("expert");
        if (expert == null) {
            return "redirect:/expert/login";
        }
        FarmerQuery query = farmerQueryService.getQueryById(queryId);
        if (query == null) {
            return "redirect:/expert/dashboard";
        }
        model.addAttribute("expert", expert);
        model.addAttribute("query", query);
        return "expert_reply";
    }

    @PostMapping("/reply/{queryId}")
    public String replyToQuery(@PathVariable Long queryId, @RequestParam String response, HttpSession session) {
        Expert expert = (Expert) session.getAttribute("expert");
        if (expert == null) {
            return "redirect:/expert/login";
        }
        FarmerQuery query = farmerQueryService.getQueryById(queryId);
        if (query != null) {
            expertResponseService.createResponse(query, expert, response);
            farmerQueryService.updateQueryStatus(queryId, "ANSWERED");
        }
        return "redirect:/expert/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
