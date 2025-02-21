package com.tut.scm.scm_tutorial.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tut.scm.scm_tutorial.entities.User;
import com.tut.scm.scm_tutorial.forms.UserForm;
import com.tut.scm.scm_tutorial.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageControllers {

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String index(){
        return "home";
    }

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home Page Handler..");
        model.addAttribute("name", "Jidnyesh");
        model.addAttribute("github", "https://github.com");
        return "home";
    }

    @RequestMapping("/about")
    public String about(){
        System.out.println("About Page");
        return "about";
    }

    @RequestMapping("/services")
    public String services(){
        System.out.println("services Page");
        return "services";
    }

    @RequestMapping("/base")
    public String base(){
        System.out.println("base Page");
        return "base";
    }

    @GetMapping("/contact")
    public String contact(){
        return "contact";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

   

    @GetMapping("/register")
    public String registerPage(Model model, HttpSession session){

        UserForm userForm=new UserForm();
        //blank userForm bhejna hai
        model.addAttribute("userForm", userForm);

        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message"); // Clear the message after displaying it
        }
        
        return "register";
    }

    //processing register form

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm ,BindingResult result, HttpSession session){
        System.out.println("Inside Register process form");
        System.out.println(userForm);

        if(result.hasErrors()){
            return "register";
        }
        
        User user=new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setGender(userForm.getGender());
        user.setEnabled(false);//by default user is disabled..
        user.setProfilePic("https://media.istockphoto.com/id/610003972/vector/vector-businessman-black-silhouette-isolated.jpg?s=612x612&w=0&k=20&c=Iu6j0zFZBkswfq8VLVW8XmTLLxTLM63bfvI6uXdkacM=");
        userService.saveUser(user);

        session.setAttribute("message", "Registration Successful!!");

        return "redirect:/register";
    }



}
