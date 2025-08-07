package com.example.pnmbackend.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import com.example.pnmbackend.model.type.Role;
import com.example.pnmbackend.jwt.SignInService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SignInService signInService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize.requestMatchers("api/user/createUser","/api/admin/login",
                                "api/admin/register","api/producer/login",
                                "api/producer/register","api/producer/producersDetails/**",
                                "api/producer/getProducers/**","api/admin/allNews","api/admin/news/**",
                                "api/producer/retrieveLogo/{id}","api/producer/retrieveAllImages/{id}","api/producer/passwordRecovered/**").permitAll()
                        .requestMatchers("api/admin/approvedProducer","api/admin/dashboard",
                                "api/admin/deleteProducer","api/admin/rejectedProducer",
                                "api/admin/allProducer/notActive", "api/admin/approvedNewsLetter/{id}"
                                ,"api/admin/rejectedNewsLetter/{id}", "api/admin/deleteNewsletter/{id}",
                                "api/admin/createNews", "api/admin/approvedNewsLetter").hasAuthority(Role.ADMIN.name())
                        .requestMatchers("api/producer/dashboard","api/producer/updateProducer", "api/producer/deleteAllImages/**",
                                "api/producer/uploadImages","api/producer/uploadLogo",
                                "api/admin/rejectedNewsLetter", "api/admin/deleteNewsletter",
                                "api/producer/createNewsLetter","/deleteAllImages/{id}").hasAuthority(Role.PRODUCER.name())
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(signInService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
