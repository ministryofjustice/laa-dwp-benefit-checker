package uk.gov.justice.laa.bc.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml5AuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.SimpleRedirectInvalidSessionStrategy;

/**
 * Configuration class for customizing Spring Security settings.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {


  @Bean
  SecurityFilterChain configure(HttpSecurity http) {
    return http.authorizeHttpRequests(
                    authorize ->
                            authorize
                                    .requestMatchers(
                                            HttpMethod.GET,
                                            "/actuator/prometheus",
                                            "/actuator/health/**",
                                            "/actuator/info",
                                            "/actuator/metrics")
                                    .permitAll() // Ensure Actuator endpoints are excluded
                                    .anyRequest().authenticated())
            .sessionManagement(
                    sessionManagement ->
                            sessionManagement.invalidSessionStrategy(
                                    new SimpleRedirectInvalidSessionStrategy("/home")))
            .build();
  }
}
