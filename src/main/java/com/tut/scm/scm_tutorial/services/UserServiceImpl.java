package com.tut.scm.scm_tutorial.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tut.scm.scm_tutorial.Repository.UserRepository;
import com.tut.scm.scm_tutorial.entities.User;
import com.tut.scm.scm_tutorial.helpers.AppConstants;
import com.tut.scm.scm_tutorial.helpers.Helper;
import com.tut.scm.scm_tutorial.helpers.ResourceNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


    @Override
    public User saveUser(User user) {

        String userId=UUID.randomUUID().toString();
        user.setUserId(userId);

        //password ko encode karkw set kar dena database me
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRoleList(List.of(AppConstants.ROLE_USER));



        String emailToken=UUID.randomUUID().toString();
        user.setEmailToken(emailToken);
        
        User savedUser=userRepository.save(user);
        String emailLink=Helper.getLinkForEmailVerification(emailToken);

        
        emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart  Contact Manager", emailLink);
        return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
       
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
      User oldUser=  userRepository.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));

      oldUser.setName(user.getName());
      oldUser.setEmail(user.getEmail());
      oldUser.setPassword(user.getPassword());
      oldUser.setAbout(user.getAbout());
      oldUser.setPhoneNumber(user.getPhoneNumber());
      oldUser.setGender(user.getGender());
      oldUser.setProfilePic(user.getProfilePic());
      oldUser.setEnabled(user.isEnabled());
      oldUser.setEmailVerified(user.isEmailVerified());
      oldUser.setPhoneVerified(user.isPhoneVerified());
      oldUser.setProvider(user.getProvider());
      oldUser.setProvidersUserId(user.getProvidersUserId());

      User save=userRepository.save(oldUser);
      return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user=  userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
    userRepository.delete(user);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user=  userRepository.findById(userId).orElseThrow(null);
        return user!=null ?true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        
       User user= userRepository.findByEmail(email).orElse(null);
       return user!=null?true:false;

    }

    @Override
    public List<User> getAllUsers() {
      return userRepository.findAll();

    }

    @Override
    public User getUserByEmail(String email ) {
        return userRepository.findByEmail(email).orElse(null);
    }

}
