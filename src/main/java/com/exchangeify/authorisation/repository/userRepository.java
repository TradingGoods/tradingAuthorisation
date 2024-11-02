package com.exchangeify.authorisation.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.exchangeify.authorisation.model.User;


@Repository
public interface userRepository extends MongoRepository<User, String> {

    
    
}
