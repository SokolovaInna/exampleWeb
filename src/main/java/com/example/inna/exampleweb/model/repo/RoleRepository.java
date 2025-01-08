package com.example.inna.exampleweb.model.repo;

import com.example.inna.exampleweb.model.Role;
import com.example.inna.exampleweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Optional<Role> findById(String id);
}
