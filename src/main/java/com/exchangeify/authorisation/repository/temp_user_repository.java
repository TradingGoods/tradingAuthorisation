package com.exchangeify.authorisation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchangeify.authorisation.model.UserEntity;


public interface temp_user_repository extends JpaRepository<UserEntity,Integer>{

    Optional<UserEntity> findByEmailId(String emailId);

    Boolean existsByEmailId(String emailId);
    
}
