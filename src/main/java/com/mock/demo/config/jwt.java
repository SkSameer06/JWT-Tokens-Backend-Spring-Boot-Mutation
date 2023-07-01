package com.mock.demo.config;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mock.demo.entities.users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class jwt{

    // @Value("${app.jwt.secret}")
	// private String secret;

    private int refreshExpirationDateInMs;

    @Value("${jwt.refreshExpirationDateInMs}")
	public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
		this.refreshExpirationDateInMs = refreshExpirationDateInMs;
	}

    public String token(String name,String email) {

        System.out.println("Inside Token fxn");

        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                                SignatureAlgorithm.HS256.getJcaName());

        System.out.println("KEy::::::>"+hmacKey);

            Instant now = Instant.now();


        String jwtToken = Jwts.builder()
            .claim("name", name)
            .claim("email", email)
            .setSubject("Token")
            .setId(UUID.randomUUID().toString())
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(now.plus(15, ChronoUnit.MINUTES)))
            .signWith(hmacKey)
            .compact();
            System.out.println("JWT Token:::::::>"+ jwtToken);

        return jwtToken;
    }


    public static Jws<Claims> parseJwt(String jwtString) {
        String token = jwtString.trim();
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                                        SignatureAlgorithm.HS256.getJcaName());

        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);

        return jwt;
    }


    public String generateRefreshToken(String name,String email) throws NoSuchAlgorithmException {
        // Set the expiration time for the refresh token (e.g., 30 days)
        Date expiration = new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L);

        // Generate the secret key
        //Key secretKey = generateSecretKey();
        String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
        Key secretKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                                    SignatureAlgorithm.HS256.getJcaName());
        System.out.println("Secrete key1111:::::>"+secretKey);

        

        JwtBuilder builder = Jwts.builder()
                .claim("name", name)
                .claim("email", email)
                .setSubject("Token")
                .setExpiration(expiration)
                // You can add additional claims here as needed
                .signWith(secretKey, SignatureAlgorithm.HS256);

        return builder.compact();
    }

    

     public Boolean validateRefreshToken(String refreshToken1) throws NoSuchAlgorithmException {
        try {
            String refreshToken = refreshToken1.trim();
            String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
            Key secretKey = new SecretKeySpec(Base64.getDecoder().decode(secret), SignatureAlgorithm.HS256.getJcaName());
                                    
            System.out.println("Secrete Key:::::>"+secretKey);
            //secretKey = generateSecretKey();
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(refreshToken);

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

}



    
     

     

  
    
}