package cl.duoc.dsy2205.forum.security;

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
    private final byte[] secret; private final ObjectMapper mapper=new ObjectMapper();
    public TokenService(@Value("${app.jwt-secret}") String secret){this.secret=secret.getBytes(StandardCharsets.UTF_8);if(this.secret.length<32)throw new IllegalArgumentException("JWT_SECRET must contain at least 32 bytes");}
    public Map<String,Object> verify(String token){
        try{String[] p=token.split("\\.");if(p.length!=3||!java.security.MessageDigest.isEqual(sign(p[0]+"."+p[1]).getBytes(StandardCharsets.UTF_8),p[2].getBytes(StandardCharsets.UTF_8)))return null;
            Map<String,Object> c=mapper.readValue(Base64.getUrlDecoder().decode(p[1]),Map.class);if(((Number)c.get("exp")).longValue()<=Instant.now().getEpochSecond())return null;return c;
        }catch(Exception e){return null;}}
    private String sign(String value){try{Mac mac=Mac.getInstance("HmacSHA256");mac.init(new SecretKeySpec(secret,"HmacSHA256"));return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
}
