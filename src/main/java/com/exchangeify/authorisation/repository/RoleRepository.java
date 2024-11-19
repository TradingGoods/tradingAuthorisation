package com.exchangeify.authorisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchangeify.authorisation.model.RoleEntity;

public interface RoleRepository extends JpaRepository<RoleEntity,Integer>{

    Optional<RoleEntity> findByName(String name);
    
}
