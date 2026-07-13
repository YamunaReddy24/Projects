package com.agrilink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agrilink.model.Farmer;
import com.agrilink.repository.FarmerRepository;
import java.util.List;

@Service
public class FarmerService {

    @Autowired
    private FarmerRepository farmerRepository;

    public Farmer registerFarmer(Farmer farmer) {
        return farmerRepository.save(farmer);
    }

    public Farmer loginFarmer(String email, String password) {
        List<Farmer> farmers = farmerRepository.findAllByEmail(email);
        for (Farmer farmer : farmers) {
            if (farmer.getPassword().equals(password)) {
                return farmer;
            }
        }
        return null;
    }
}
