package cl.duoc.dsy2205.forum.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BearerTokenFilter extends OncePerRequestFilter{
    private final TokenService tokens; public BearerTokenFilter(TokenService tokens){this.tokens=tokens;}
    @Override protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException{
        String h=req.getHeader("Authorization");if(h!=null&&h.startsWith("Bearer ")){Map<String,Object> c=tokens.verify(h.substring(7));if(c!=null&&SecurityContextHolder.getContext().getAuthentication()==null){
            String role=String.valueOf(c.get("role"));SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(String.valueOf(c.get("sub")),null,List.of(new SimpleGrantedAuthority("ROLE_"+role))));}}
        chain.doFilter(req,res);
    }
}
