package com.stn.ester.services;

import com.stn.ester.entities.User;
import com.stn.ester.repositories.jpa.UserRepository;
import com.stn.ester.services.crud.SystemProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService {

    private UserRepository userRepository;
    private SystemProfileService systemProfileService;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 SystemProfileService systemProfileService) {
        this.userRepository = userRepository;
        this.systemProfileService = systemProfileService;
    }

    public void setLastLogin(Long userId, LocalDateTime loginTime) {
        User user = this.userRepository.findById(userId).get();
        user.setLastLogin(loginTime);
        this.userRepository.save(user);
    }

    public boolean isNewestToken(String username, LocalDateTime iat) {
        User user = this.userRepository.findByUsername(username).get();
        return user.getLastLogin().compareTo(iat) == 0;
    }

    public Boolean isMultipleSessionAllowed() {
        return this.systemProfileService.get().getMultipleSession();
    }
}
