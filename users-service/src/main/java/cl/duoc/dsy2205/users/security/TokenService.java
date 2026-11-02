package cl.duoc.dsy2205.users.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class TokenService {
    private final byte[] secret;
    private final long ttlSeconds;
    private final ObjectMapper mapper = new ObjectMapper();
    public TokenService(@Value("${app.jwt-secret}") String secret, @Value("${app.jwt-ttl-minutes}") long ttlMinutes) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        if (this.secret.length < 32) throw new IllegalArgumentException("JWT_SECRET must contain at least 32 bytes");
        this.ttlSeconds = ttlMinutes * 60;
    }
    public long getTtlSeconds() { return ttlSeconds; }
    public String issue(String username, String role) {
        try {
            String header = encode(mapper.writeValueAsBytes(Map.of("alg", "HS256", "typ", "JWT")));
            String payload = encode(mapper.writeValueAsBytes(Map.of("sub", username, "role", role, "exp", Instant.now().getEpochSecond() + ttlSeconds)));
            String data = header + "." + payload;
            return data + "." + sign(data);
        } catch (Exception e) { throw new IllegalStateException("No se pudo generar el token", e); }
    }
    public Map<String,Object> verify(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3 || !java.security.MessageDigest.isEqual(sign(parts[0]+"."+parts[1]).getBytes(StandardCharsets.UTF_8), parts[2].getBytes(StandardCharsets.UTF_8))) return null;
            Map<String,Object> claims = mapper.readValue(Base64.getUrlDecoder().decode(parts[1]), Map.class);
            if (((Number) claims.get("exp")).longValue() <= Instant.now().getEpochSecond()) return null;
            return claims;
        } catch (Exception e) { return null; }
    }
    private String sign(String value) {
        try { Mac mac=Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(secret,"HmacSHA256")); return encode(mac.doFinal(value.getBytes(StandardCharsets.UTF_8))); }
        catch (Exception e) { throw new IllegalStateException(e); }
    }
    private static String encode(byte[] bytes) { return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); }
}
