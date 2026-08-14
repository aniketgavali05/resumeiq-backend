package com.resumeiq.backend.config;

import com.resumeiq.backend.entity.Role;
import com.resumeiq.backend.entity.enums.RoleType;
import com.resumeiq.backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        if (roleRepository.findByName(RoleType.ROLE_USER).isEmpty()) {

            Role role = new Role();
            role.setName(RoleType.ROLE_USER);

            roleRepository.save(role);
        }

        if (roleRepository.findByName(RoleType.ROLE_ADMIN).isEmpty()) {

            Role role = new Role();
            role.setName(RoleType.ROLE_ADMIN);

            roleRepository.save(role);
        }

        System.out.println("Default roles initialized.");
    }
}