package com.exchangeify.authorisation.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.exchangeify.authorisation.model.RoleEntity;
import com.exchangeify.authorisation.model.UserEntity;
import com.exchangeify.authorisation.repository.temp_user_repository;


@Service
public class CustomUserDetailService implements UserDetailsService{

    @Autowired
    private temp_user_repository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmailId(username).orElseThrow(() -> new UsernameNotFoundException("email not found"));
        return new User(user.getEmailId(),user.getPassword(),grantAuthority(user.getRoles()));
    }

    private Collection<GrantedAuthority> grantAuthority(List<RoleEntity> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
    
}
