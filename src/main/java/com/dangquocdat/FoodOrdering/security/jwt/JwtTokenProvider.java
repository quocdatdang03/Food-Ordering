package com.dangquocdat.FoodOrdering.security.jwt;

import com.dangquocdat.FoodOrdering.exception.ApiException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    // Generate jwt token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName(); // getName() -> return email (username)

        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime()+jwtExpirationDate);

        String jwtToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact(); // compact() -> create jwt token

        return jwtToken;
    }

    private Key key() {

        // decode jwt secret key
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // Get username from Jwt token
    public String getUsername(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();

        return username;
    }

    // Validate JWT token
    public boolean validateToken(String token) {

        /*Jwts.parser()
                .setSigningKey(key())
                .build()
                .parse(token);*/
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parseSignedClaims(token);

            return getExpiredDate(token).after(new Date());
        }
        catch(MalformedJwtException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid jwt token");
        }
        catch(ExpiredJwtException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Expired jwt token");
        }
        catch(UnsupportedJwtException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Unsupported jwt token");
        }
        catch(IllegalArgumentException ex) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Jwt claims string is empty");
        }
    }

    public Date getExpiredDate(long expiredTime) {
        Date currentDate = new Date();
        return new Date(currentDate.getTime()+expiredTime);
    }

    public Date getExpiredDate(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    public Claims getClaims(String token) {

        Claims claims = Jwts.parser()
                            .verifyWith((SecretKey) key())
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();

        return claims;
    }
}
