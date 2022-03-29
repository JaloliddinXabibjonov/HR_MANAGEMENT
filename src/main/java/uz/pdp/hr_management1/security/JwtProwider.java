package uz.pdp.hr_management1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import uz.pdp.hr_management1.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProwider {
    private final String secretKey="maxfiyKalitSo`z";
    private static final long expireTime=1000*60*60*24;
    public String generateToken(String username, Set<Role> roles){
        Date expireDate=new Date(System.currentTimeMillis()+expireTime);
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    public String getEmailFromToken(String token){
        try{
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }catch(Exception e){
            return null;
        }
    }
}
