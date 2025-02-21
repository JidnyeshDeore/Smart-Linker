package com.tut.scm.scm_tutorial.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.tut.scm.scm_tutorial.entities.User;
import com.tut.scm.scm_tutorial.helpers.Helper;
import com.tut.scm.scm_tutorial.services.UserService;

@ControllerAdvice
public class RootController {


    Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){

        if(authentication==null){
            return ;
        }

        String email=  Helper.getEmailOfLoggedInUser(authentication);
        System.out.println("Adding Logged in user Information to the Model..");
        logger.info(email);
    
        User user=userService.getUserByEmail(email);
        if(user==null){
            System.out.println("Null User logged in Model");
            model.addAttribute("loggedInUser", null);
        }else{
            System.out.println(user.getEmail());
            System.out.println(user.getUsername());
            model.addAttribute("loggedInUser",user);
        }


    }


}
