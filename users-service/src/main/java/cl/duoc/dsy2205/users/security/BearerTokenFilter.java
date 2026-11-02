package cl.duoc.dsy2205.users.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BearerTokenFilter extends OncePerRequestFilter {
    private final TokenService tokens;
    public BearerTokenFilter(TokenService tokens) { this.tokens = tokens; }
    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header=request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            Map<String,Object> claims=tokens.verify(header.substring(7));
            if (claims != null && SecurityContextHolder.getContext().getAuthentication()==null) {
                String role=String.valueOf(claims.get("role"));
                var auth=new UsernamePasswordAuthenticationToken(String.valueOf(claims.get("sub")), null, List.of(new SimpleGrantedAuthority("ROLE_"+role)));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request,response);
    }
}
