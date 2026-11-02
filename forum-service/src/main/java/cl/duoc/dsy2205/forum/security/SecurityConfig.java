package cl.duoc.dsy2205.forum.security;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration @EnableMethodSecurity
public class SecurityConfig{
    @Bean BearerTokenFilter bearerTokenFilter(TokenService tokens){return new BearerTokenFilter(tokens);}
    @Bean SecurityFilterChain securityFilterChain(HttpSecurity http,BearerTokenFilter filter)throws Exception{
        return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a->a.requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET,"/api/categories/**","/api/topics/**","/api/reports/**").permitAll()
                .anyRequest().authenticated()).addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class).build();
    }
}
