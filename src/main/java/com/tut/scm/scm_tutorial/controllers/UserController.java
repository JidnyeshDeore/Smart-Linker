package com.tut.scm.scm_tutorial.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/user")
public class UserController {


    // Logger logger= LoggerFactory.getLogger(UserController.class);

    // @Autowired
    // private UserService userService;

 
    @RequestMapping("/dashboard")
    public String userDashboard(){
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String UserProfile(Model model ,Authentication authentication){
        
        return "user/profile";
    }



}
