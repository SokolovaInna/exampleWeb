package com.example.inna.exampleweb.model.repo;

import com.example.inna.exampleweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByMsisdn(String msisdn);
}
