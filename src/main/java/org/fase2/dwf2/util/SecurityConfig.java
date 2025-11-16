package org.fase2.dwf2.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login","/register","/api/users/register","/api/users/search","/user/test","/api/users/login","/api/users/user/roles",
                                "/api/users/user/test", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/index.html",
                                "/access-denied", "/api/loans/**","/api/branches/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/client/**").hasRole("CLIENT")
                        .requestMatchers("/cliente/**").hasRole("CLIENT")
                        .requestMatchers("/dependiente/**").hasRole("DEPENDIENTE")
                        .requestMatchers("/cajero/**").hasRole("CAJERO")
                        .requestMatchers("/gerente_sucursal/**").hasRole("GERENTE_SUCURSAL")
                        .requestMatchers("/gerente_general/**").hasRole("GERENTE_GENERAL")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Use session if required
                )
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(new HttpSessionSecurityContextRepository()) // Explicitly set repository
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login")  // Redirects to /login if the session is invalidated
                        .maximumSessions(1)           // Limits the user to one session at a time
                        .maxSessionsPreventsLogin(false)  // Allows a new session to terminate the old one
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)  // Invalidates the session on logout
                        .deleteCookies("JSESSIONID")  // Clears cookies to ensure credentials are removed
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler()) // Custom access denied handler
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (savedRequest != null) {
                // Redirect to the previously requested URL
                response.sendRedirect(savedRequest.getRedirectUrl());
            } else if (authentication != null && authentication.isAuthenticated()) {
                // Redirect based on the user's role
                if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENT"))) {
                    response.sendRedirect("/client/dashboard");
                }else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_DEPENDIENTE"))) {
                    response.sendRedirect("/dependiente/dashboard");
                } else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_CAJERO"))) {
                    response.sendRedirect("/cajero/dashboard");
                } else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_GERENTE_SUCURSAL"))) {
                    response.sendRedirect("/gerente_sucursal/dashboard");
                } else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_GERENTE_GENERAL"))) {
                    response.sendRedirect("/gerente_general/dashboard");
                } else {
                    response.sendRedirect("/access-denied");
                }
            } else {
                response.sendRedirect("/login"); // Fallback for unauthenticated users
            }
        };
    }


}
