package com.tut.scm.scm_tutorial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tut.scm.scm_tutorial.Repository.UserRepository;
import com.tut.scm.scm_tutorial.entities.User;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("verify-email")
    public String verifyEmail(Model model
    ,@RequestParam String token 
    ,HttpSession session){

        System.out.println("Verify Email");
        System.out.println(token);
        model.addAttribute("message", session.getAttribute("message"));
    
       User user = userRepository.findByEmailToken(token).orElse(null);
        System.out.println(user.getEmailToken());
       {

        if(user.getEmailToken().equals(token)){

            System.out.println("Inside email verifed token.....");
            user.setEmailVerified(true);
            user.setEnabled(true);
            userRepository.save(user);
            session.setAttribute("message", "Email verified Successfully...");
            return "success_page";
        }
        System.out.println("Email not verified...");
        session.setAttribute("message", "Email is not verified !! Token is not associated..!");
        return "error_page";
       }
    }
}
