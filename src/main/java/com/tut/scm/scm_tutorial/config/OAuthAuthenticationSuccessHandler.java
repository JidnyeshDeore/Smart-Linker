package com.tut.scm.scm_tutorial.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tut.scm.scm_tutorial.Repository.UserRepository;
import com.tut.scm.scm_tutorial.entities.Providers;
import com.tut.scm.scm_tutorial.entities.User;
import com.tut.scm.scm_tutorial.helpers.AppConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
        //The OAuthAuthenticationSuccessHandler is responsible for processing user data after successful OAuth authentication. 
        //AuthenticationSuccessHandler: This interface allows the class to define actions taken on successful authentication.
        @Autowired
        private UserRepository userRepository;

        Logger logger=LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
                
                logger.info("OAuthenticationSuccessHandler");

                // DefaultOAuth2User user   =(DefaultOAuth2User)authentication.getPrincipal();

                // logger.info(user.getName());
                // user.getAttributes().forEach((key,value)->{
                //         logger.info("{} = {}",key,value);
                // });

                // logger.info(user.getAuthorities().toString());


                //Identify the user
                var oauth2AuthenticationToken=(OAuth2AuthenticationToken) authentication;

                String authorizedClientRegistrationId=oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

                logger.info(authorizedClientRegistrationId);
                
                
                var oathUser=(DefaultOAuth2User)authentication.getPrincipal();

                User user=new User();
                user.setUserId(UUID.randomUUID().toString());
                user.setRoleList(List.of(AppConstants.ROLE_USER));
                user.setEmailVerified(true);
                user.setEnabled(true);
                user.setPassword("123456");

                //google Login

                if(authorizedClientRegistrationId.equalsIgnoreCase("google")){
                        user.setEmail(oathUser.getAttribute("email").toString());
                        user.setProfilePic(oathUser.getAttribute("picture").toString());
                        user.setName(oathUser.getAttribute("name").toString());
                        user.setProvidersUserId(oathUser.getName());
                        user.setProvider(Providers.GOOGLE);
                        user.setAbout("ACoount created using google");



                }else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){


                        String email=oathUser.getAttribute("email")!=null?
                                oathUser.getAttribute("email").toString() : oathUser.getAttribute("login").toString()+"@gmail.com";

                        String picture=oathUser.getAttribute("avatar_url").toString();

                        String name=oathUser.getAttribute("login").toString();

                        String providerUserId=oathUser.getName();

                        user.setEmail(email);
                        user.setProfilePic(picture);
                        user.setName(name);
                        user.setProvidersUserId(providerUserId);
                        user.setProvider(Providers.GITHUB);
                        user.setAbout("ACoount created using github");


                }else{

                        logger.info("unknown Provider");
                }

                User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);

                 if (existingUser == null){
                        userRepository.save(user);
                 }else{
                        logger.info("user already exists"+user.getEmail());
                 }


        //         String email=user.getAttribute("email").toString();
        //         String name=user.getAttribute("name").toString();
        //         String picture=user.getAttribute("picture").toString();
               

        // // Check if user already exists
        

        // if (existingUser == null) {
        //         // User does not exist, create and save new user
        //         User newUser = new User();
        //         newUser.setEmail(email);
        //         newUser.setName(name);
        //         newUser.setProfilePic(picture);
        //         newUser.setPassword("Dummy");
        //         newUser.setUserId(UUID.randomUUID().toString());
        //         newUser.setProvider(Providers.GOOGLE);
        //         newUser.setEnabled(true);
        //         newUser.setGender("Male");
        //         newUser.setEmailVerified(true);
        //         newUser.setProvidersUserId(user.getName());
        //         newUser.setRoleList(List.of(AppConstants.ROLE_USER));
        //         newUser.setAbout("This account is created using Google.");

        //         userRepository.save(newUser);
        //         logger.info("User saved: " + email);
        // } else {
        //         logger.info("User already exists: " + email);
        // }

         new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

        }

    

}
