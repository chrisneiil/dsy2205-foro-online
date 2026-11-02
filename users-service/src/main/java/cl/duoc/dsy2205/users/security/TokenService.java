package cl.duoc.dsy2205.users.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class TokenService {
    private final JwtEncoder encoder;
    private final long ttlSeconds;
    public TokenService(JwtEncoder encoder, @Value("${app.jwt-ttl-minutes}") long ttlMinutes) {
        this.encoder=encoder;
        this.ttlSeconds=ttlMinutes*60;
    }
    public long getTtlSeconds(){return ttlSeconds;}
    public String issue(String username,String role){
        Instant now=Instant.now();
        JwtClaimsSet claims=JwtClaimsSet.builder().subject(username).issuedAt(now)
            .expiresAt(now.plusSeconds(ttlSeconds)).claim("role",role).build();
        JwsHeader header=JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
        return encoder.encode(JwtEncoderParameters.from(header,claims)).getTokenValue();
    }
}
