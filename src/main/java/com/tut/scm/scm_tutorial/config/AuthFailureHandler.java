package com.tut.scm.scm_tutorial.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {


        if(exception instanceof DisabledException){

            //user is disabled...
            System.out.println("Inside disabled user handler.............................");



            // String message = (String) session.getAttribute("message");
            // if (message != null) {
            //     model.addAttribute("message", message);
            //     session.removeAttribute("message"); // Clear the message after displaying it
            // }
        
            

            HttpSession session=request.getSession();
            session.setAttribute("message", "User is disabled, Email with Verification Link is sent on register Email id...");
            // session.removeAttribute("message");
            response.sendRedirect("/login");
        }else{
            response.sendRedirect("/login?error=true");
        }
    }

}
