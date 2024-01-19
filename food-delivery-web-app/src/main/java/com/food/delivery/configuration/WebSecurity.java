package com.food.delivery.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.food.delivery.filter.AuthEntryPointJwt;
import com.food.delivery.filter.JwtTokenSecurityFilter;

import com.food.delivery.service.CustomUserDetailsMyImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true,prePostEnabled = true)

// prePostEnabled = true) // by default
public class WebSecurity {
  
	
  @Autowired
  CustomUserDetailsMyImpl customUserDetailsMyImpl;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public JwtTokenSecurityFilter authenticationJwtTokenFilter() {
    return new JwtTokenSecurityFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(customUserDetailsMyImpl);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
      return web -> web.debug(false).ignoring().requestMatchers("/css/**", "/js/**", "/lib/**", "/favicon.ico", "/images/**", "/webjars/**", "/swagger-ui/**", "/v3/api-docs/**");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> 
        authorizationManagerRequestMatcherRegistry
        .requestMatchers(HttpMethod.DELETE).hasRole("ROLE_ADMIN")
        .requestMatchers("/admin/**").hasAnyRole("ROLE_ADMIN")
        .requestMatchers("/user/**").hasAnyRole("ROLE_USER", "ROLE_ADMIN")
        .requestMatchers("/mod/**").hasAnyRole("ROLE_MODERATOR")
        .requestMatchers("/login/**").permitAll()
        .requestMatchers("/api/auth/**", "/users/signup", "/users/login","/users/refreshtoken", "/swagger-ui.html", "/swagger-ui/**", "/swagger-ui/index.html", "/v3/api-docs").permitAll()
        .requestMatchers("/api/test/**").permitAll()
        .anyRequest().authenticated()
        );
    
    http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    
    return http.build();
  }
}