package com.agrilink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agrilink.model.Expert;
import com.agrilink.repository.ExpertRepository;
import java.util.List;

@Service
public class ExpertService {

    @Autowired
    private ExpertRepository expertRepository;

    public Expert registerExpert(Expert expert) {
        return expertRepository.save(expert);
    }

    public Expert loginExpert(String email, String password) {
        List<Expert> experts = expertRepository.findAllByEmail(email);
        for (Expert expert : experts) {
            if (expert.getPassword().equals(password)) {
                return expert;
            }
        }
        return null;
    }
}
