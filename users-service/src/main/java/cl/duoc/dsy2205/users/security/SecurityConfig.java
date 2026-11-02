package cl.duoc.dsy2205.users.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableMethodSecurity
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean BearerTokenFilter bearerTokenFilter(TokenService tokens) { return new BearerTokenFilter(tokens); }
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, BearerTokenFilter filter) throws Exception {
        return http.csrf(csrf->csrf.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a->a.requestMatchers("/api/auth/**","/actuator/health").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class).build();
    }
}
