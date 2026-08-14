package com.resumeiq.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.resumeiq.backend.entity.Role;
import com.resumeiq.backend.entity.enums.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);
}