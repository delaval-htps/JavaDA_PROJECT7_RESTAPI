package com.nnk.springboot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nnk.springboot.services.CustomUserDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http.authorizeRequests()
                .antMatchers("/css/**")
                .permitAll()

                .antMatchers("/")
                .permitAll()

            /*
             * don't use hasRole("ADMIN") cause Role in user is "ADMIN" and
             * not "ROLE_ADMIN" , use hasAuthority("ADMIN").In spring
             * security, hasRole() is the same as hasAuthority(), but
             * hasRole() function map with Authority without ROLE_ prefix.
            */
                .antMatchers("/bidList/**", "/curvePoint/**", "/rating/**", "ruleName/**","/trade/**","/user/home")
                .hasAnyAuthority("USER", "ADMIN")
                
                .antMatchers("/user/**", "/admin/**")
                .hasAuthority("ADMIN")

                .anyRequest().authenticated()

                .and()
                //using AccesDeniedPage make easy to redirect to error 403 page 
                .exceptionHandling().accessDeniedPage("/app/error");


        http.formLogin()
                .loginPage("/app/login")
                .loginProcessingUrl("/process-login")
                .permitAll();
        
        http.logout()
                .logoutUrl("/app-logout")
                .logoutSuccessUrl("/")
                .permitAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
