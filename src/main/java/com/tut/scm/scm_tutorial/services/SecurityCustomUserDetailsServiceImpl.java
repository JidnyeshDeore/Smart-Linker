package com.tut.scm.scm_tutorial.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tut.scm.scm_tutorial.Repository.UserRepository;

@Service
public class SecurityCustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // database se user ka emial and password nikalana
        return userRepository.findByEmail(username)
        .orElseThrow(()->new UsernameNotFoundException("User not found with email "+username));
        

    }

}
