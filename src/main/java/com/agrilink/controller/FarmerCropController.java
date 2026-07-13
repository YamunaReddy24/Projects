package com.agrilink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.agrilink.model.Crop;
import com.agrilink.model.Farmer;
import com.agrilink.repository.CropRepository;

import jakarta.servlet.http.HttpSession;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.util.StringUtils;

@Controller
@RequestMapping("/farmer")
public class FarmerCropController {

    @Autowired
    private CropRepository cropRepository;

    @GetMapping("/add-crop")
    public String showAddCropForm(Model model, HttpSession session) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        model.addAttribute("crop", new Crop());
        return "farmer_add_crop";
    }

    @PostMapping("/add-crop")
    public String saveCrop(@ModelAttribute Crop crop, @RequestParam("imageFile") MultipartFile imageFile, HttpSession session) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        crop.setFarmerId(farmer.getId());
        crop.setCreatedAt(java.time.LocalDateTime.now());
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Path uploadsDir = Paths.get("uploads");
                Files.createDirectories(uploadsDir);
                String filename = UUID.randomUUID() + "-" + StringUtils.cleanPath(imageFile.getOriginalFilename());
                Path filePath = uploadsDir.resolve(filename);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                crop.setImageUrl("/uploads/" + filename);
            } catch (Exception e) {
                
            }
        }
        cropRepository.save(crop);
        return "redirect:/farmer/view-crops";
    }

    @GetMapping("/view-crops")
    public String viewCrops(Model model, HttpSession session) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        List<Crop> crops = cropRepository.findByFarmerId(farmer.getId());
        model.addAttribute("crops", crops);
        return "farmer_view_crops";
    }

    @GetMapping("/edit-crop/{id}")
    public String editCropForm(@PathVariable Long id, Model model, HttpSession session) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        Optional<Crop> cropOpt = cropRepository.findById(id);
        if (cropOpt.isEmpty()) {
            return "redirect:/farmer/view-crops";
        }
        model.addAttribute("crop", cropOpt.get());
        return "farmer_edit_crop";
    }

    @PostMapping("/edit-crop/{id}")
    public String updateCrop(@PathVariable Long id,
                             @ModelAttribute Crop form,
                             @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                             HttpSession session) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        Optional<Crop> cropOpt = cropRepository.findById(id);
        if (cropOpt.isEmpty()) {
            return "redirect:/farmer/view-crops";
        }
        Crop crop = cropOpt.get();
        crop.setCropName(form.getCropName());
        crop.setPrice(form.getPrice());
        crop.setQuantity(form.getQuantity());
        crop.setDescription(form.getDescription());

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Path uploadsDir = Paths.get("uploads");
                Files.createDirectories(uploadsDir);
                String filename = UUID.randomUUID() + "-" + StringUtils.cleanPath(imageFile.getOriginalFilename());
                Path filePath = uploadsDir.resolve(filename);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                crop.setImageUrl("/uploads/" + filename);
            } catch (Exception e) {
                
            }
        }

        cropRepository.save(crop);
        return "redirect:/farmer/view-crops";
    }

    @PostMapping("/delete-crop/{id}")
    public String deleteCrop(@PathVariable Long id, HttpSession session) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer == null) {
            return "redirect:/farmer/login";
        }
        Optional<Crop> cropOpt = cropRepository.findById(id);
        if (cropOpt.isPresent()) {
            Crop crop = cropOpt.get();
            String imageUrl = crop.getImageUrl();
            if (imageUrl != null && imageUrl.startsWith("/uploads/")) {
                try {
                    Path p = Paths.get("uploads", imageUrl.replace("/uploads/", ""));
                    Files.deleteIfExists(p);
                } catch (Exception e) {
                    
                }
            }
            cropRepository.deleteById(id);
        }
        return "redirect:/farmer/view-crops";
    }
}
