package io.github.spring.api_parking_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.github.spring.api_parking_manager.security.SecurityFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
  
  private final SecurityFilter securityFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

      httpSecurity.csrf(AbstractHttpConfigurer::disable)
              .httpBasic(Customizer.withDefaults())
              .authorizeHttpRequests(authorize -> {
                  authorize.requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll();
                  authorize.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll();
                  authorize.anyRequest().authenticated();
              })
              .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling(handling -> handling
              .authenticationEntryPoint(authenticationEntryPoint())
              .accessDeniedHandler(accessDeniedHandler()));
    return httpSecurity.build();
  }

  // Desabilitando segurança para o SWAGGER
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return web -> web.ignoring().requestMatchers(
      "/v2/api-docs/**",
      "/v3/api-docs/**",
      "/swagger-resources/**",
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/webjars/**"
    );
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
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return (req, res, authExeption) -> {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      res.getWriter().write("Unauthorized!!" + authExeption.getMessage());
    };
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return (req, res, accessDeniedException) -> {
      res.setStatus(HttpServletResponse.SC_FORBIDDEN);
      res.getWriter().write("Access prohibited" + accessDeniedException.getMessage());
    };
  }
}
