package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.entities.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends AppRepository<User> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}
