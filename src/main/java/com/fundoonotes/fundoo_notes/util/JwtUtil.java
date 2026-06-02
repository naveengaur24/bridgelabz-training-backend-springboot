package com.fundoonotes.fundoo_notes.util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component   // beans create kro..
public class JwtUtil {

    @Value("${jwt.secret}")   // .env se value inject kro
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Secret string → Key object convert karo  beacuse jwt want only object..not string..
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // here we generate token..
    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))  // it's the main subject of our token..
                .claim("email", email)   // add extra data like - email..
                .setIssuedAt(new Date())    // when token generate..
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) //  set expiration time..
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)  //  secret key ko sign krte hai HS256 algo. se...
                .compact();   // it generate final token string..  base 64 encoded m..
    }

    // Token se userId nikalo
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()   // we create parser..  means split -decode - read claims - signature verify - expiry check..
                .setSigningKey(getSigningKey())  // same secret key se verify..
                .build()   //parser ready..
                .parseClaimsJws(token)  // token ko parse krega - signature verify , expiry check , decode the token..
                .getBody();  //payload objcet nikalenge
        return Long.parseLong(claims.getSubject());  // return user id.."1" -> 1L
    }

    // Token valid hai ya nahi --check signature and expiry
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()   // parserbuilder --> it crea the parser means split and  create parser object
                    .setSigningKey(getSigningKey())   // sign verify with current sign key and previous one..
                    .build()   // final parser object ready
                    .parseClaimsJws(token);  //jit decode the token and read claims and  verify the signature and expiry check..
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}