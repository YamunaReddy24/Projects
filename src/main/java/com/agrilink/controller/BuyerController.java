package com.agrilink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.agrilink.model.Buyer;
import com.agrilink.model.Crop;
import com.agrilink.model.Order;
import com.agrilink.repository.BuyerRepository;
import com.agrilink.repository.CropRepository;
import com.agrilink.repository.OrderRepository;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Static counter for sequential order IDs (starts at 1000)
    private static long orderCounter = 1000;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("buyer", new Buyer());
        return "buyer_register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute Buyer buyer, Model model) {
        buyerRepository.save(buyer);
        model.addAttribute("success", true);
        return "buyer_login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "buyer_login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        Buyer buyer = buyerRepository.findByEmailAndPassword(email, password);
        if (buyer != null) {
            session.setAttribute("buyer", buyer);
            return "redirect:/buyer/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "buyer_login";
        }
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        if (buyer == null) {
            return "redirect:/buyer/login";
        }
        model.addAttribute("buyer", buyer);
        return "buyer_dashboard";
    }

    @GetMapping("/available-crops")
    public String viewAvailableCrops(Model model) {
        model.addAttribute("crops", cropRepository.findAll());
        return "available_crops";
    }

    // Show buy crop page
    @GetMapping("/buy/{id}")
    public String showBuyCropPage(@PathVariable Long id, Model model) {
        Crop crop = cropRepository.findById(id).orElse(null);
        if (crop == null) {
            model.addAttribute("error", "Crop not found!");
            return "redirect:/buyer/available-crops";
        }
        model.addAttribute("crop", crop);
        return "buy_crop";
    }

    // Show checkout page (receives quantity from buy_crop)
    @PostMapping("/checkout/{cropId}")
    public String showCheckoutPage(@PathVariable Long cropId,
                                   @RequestParam int quantity,
                                   Model model,
                                   HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        if (buyer == null) {
            return "redirect:/buyer/login";
        }

        Crop crop = cropRepository.findById(cropId).orElse(null);
        if (crop == null) {
            model.addAttribute("error", "Crop not found!");
            return "redirect:/buyer/available-crops";
        }

        if (quantity > crop.getQuantity()) {
            model.addAttribute("error", "Not enough quantity available! Only " + crop.getQuantity() + " kg left!");
            model.addAttribute("crop", crop);
            return "buy_crop";
        }

        model.addAttribute("crop", crop);
        model.addAttribute("quantity", quantity);
        return "checkout";
    }

    // Place order
    @PostMapping("/place-order/{cropId}")
    public String placeOrder(@PathVariable Long cropId,
                             @RequestParam int quantity,
                             @RequestParam String fullName,
                             @RequestParam String mobileNumber,
                             @RequestParam String houseNoStreet,
                             @RequestParam String villageCity,
                             @RequestParam String district,
                             @RequestParam String state,
                             @RequestParam String pincode,
                             @RequestParam String paymentMethod,
                             Model model,
                             HttpSession session) {
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        if (buyer == null) {
            return "redirect:/buyer/login";
        }

        Crop crop = cropRepository.findById(cropId).orElse(null);
        if (crop == null) {
            model.addAttribute("error", "Crop not found!");
            return "redirect:/buyer/available-crops";
        }

        if (quantity > crop.getQuantity()) {
            model.addAttribute("error", "Not enough quantity available! Only " + crop.getQuantity() + " kg left!");
            model.addAttribute("crop", crop);
            return "checkout";
        }

        // Generate Order ID like ORD1001, ORD1002, etc.
        orderCounter++;
        String orderId = "ORD" + orderCounter;

        // Create Order
        Order order = new Order();
        order.setOrderId(orderId);
        order.setBuyer(buyer);
        order.setCrop(crop);
        order.setFarmerId(crop.getFarmerId());
        order.setQuantity(quantity);
        order.setPricePerKg(crop.getPrice());
        order.setTotalPrice(crop.getPrice() * quantity);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.ORDER_PLACED);

        // Set address fields
        order.setFullName(fullName);
        order.setMobileNumber(mobileNumber);
        order.setHouseNoStreet(houseNoStreet);
        order.setVillageCity(villageCity);
        order.setDistrict(district);
        order.setState(state);
        order.setPincode(pincode);

        // Set payment method and payment status
        order.setPaymentMethod(paymentMethod);
        if ("Cash on Delivery".equals(paymentMethod)) {
            order.setPaymentStatus(Order.PaymentStatus.PENDING);
        } else {
            order.setPaymentStatus(Order.PaymentStatus.PAID);
        }

        orderRepository.save(order);

        // Update crop quantity
        crop.setQuantity(crop.getQuantity() - quantity);
        cropRepository.save(crop);

        // Pass to success page
        model.addAttribute("order", order);
        return "order_success";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/my-orders")
    public String viewMyOrders(HttpSession session, Model model) {
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        if (buyer == null) {
            return "redirect:/buyer/login";
        }
        List<Order> orders = orderRepository.findByBuyer(buyer);
        model.addAttribute("orders", orders);
        return "buyer_orders";
    }
}
