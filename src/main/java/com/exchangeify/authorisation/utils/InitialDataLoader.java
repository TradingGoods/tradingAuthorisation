package com.exchangeify.authorisation.utils;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.exchangeify.authorisation.model.RoleEntity;
import com.exchangeify.authorisation.repository.RoleRepository;

@Component
public class InitialDataLoader implements ApplicationRunner{
    
    private final RoleRepository roleRepository;

    public InitialDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;

    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        long count = roleRepository.count();
        if (count == 0) {
            // Load initial data
            RoleEntity userRole = new RoleEntity();
            userRole.setName("USER");
            RoleEntity adminRole = new RoleEntity();
            adminRole.setName("ADMIN");
            roleRepository.saveAll(List.of(userRole, adminRole));
        }
        
    }
    
}
