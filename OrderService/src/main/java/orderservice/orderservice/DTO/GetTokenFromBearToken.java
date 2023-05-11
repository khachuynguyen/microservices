package orderservice.orderservice.DTO;

import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GetTokenFromBearToken {
    @Value("${jwt.app.jwtSecret}")
    String jwtSecret;
    String getTokenFromAuthorizationHeader(String accessToken){
        String[] parts = accessToken.split("\\s");
        return parts[1];
    }
    public String getUserName(String accessToken){
        accessToken = getTokenFromAuthorizationHeader(accessToken);
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(accessToken)
                .getBody();
        return claims.getSubject();
    }
    public int getUserId(String accessToken){
        accessToken = getTokenFromAuthorizationHeader(accessToken);
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(accessToken)
                .getBody();
        return (int) claims.get("id");
    }
}
