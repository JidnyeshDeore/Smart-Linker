package com.tut.scm.scm_tutorial.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.tut.scm.scm_tutorial.services.SecurityCustomUserDetailsServiceImpl;

@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailsServiceImpl securityCustomUserDetailsServiceImpl;

    @Autowired
    private OAuthAuthenticationSuccessHandler oAuthHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        // This bean configures a DaoAuthenticationProvider, which is used for
        // authenticating users based on user details stored in a database.

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // UserDetailService ka object
        // Sets the user details service to load user-specific data.
        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailsServiceImpl);

        // BCryptPasswordEncoder
        // password encode ho jayega
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    // konsa route login kar sakta hai
    // kon login kar sakata hai
    // This method configures the security filter chain
    // which defines how requests are secured.

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // urls configure kiya hai
        httpSecurity.authorizeHttpRequests(authorize -> {
            // authorize.requestMatchers("/home").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();

        });

        // login form se related
        httpSecurity.formLogin(formLogin -> {

            formLogin.loginPage("/login");  //Users are directed to /login for authentication.
            formLogin.loginProcessingUrl("/authenticate");//When submitting login credentials, the request is sent to /authenticate.
            formLogin.successForwardUrl("/user/dashboard");//On successful login, users are redirected to /user/dashboard.
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            // formLogin.failureHandler(new AuthenticationFailureHandler() {
            // @Override
            // public void onAuthenticationFailure(HttpServletRequest request,
            // HttpServletResponse response, AuthenticationException exception) throws
            // IOException, ServletException {
            // throw new UnsupportedOperationException("Failure");
            // }

            // });

            // formLogin.successHandler(new AuthenticationSuccessHandler() {
            // @Override
            // public void onAuthenticationSuccess(HttpServletRequest request,
            // HttpServletResponse response, Authentication authentication) throws
            // IOException, ServletException {
            // throw new UnsupportedOperationException("Successful");
            // }

            // });

            formLogin.failureHandler(authFailureHandler);

        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");

        });

        // oauth Configuaration
        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(oAuthHandler);
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

}
