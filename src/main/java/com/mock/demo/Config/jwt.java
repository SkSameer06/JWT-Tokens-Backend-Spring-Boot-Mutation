package com.mock.demo.Config;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
public class jwt {

        // @Value("${app.jwt.secret}")
        // private String secret;

        private int refreshExpirationDateInMs;

        @Value("${jwt.refreshExpirationDateInMs}")
        public void setRefreshExpirationDateInMs(int refreshExpirationDateInMs) {
                this.refreshExpirationDateInMs = refreshExpirationDateInMs;
        }

        public String token(int id, String name, String email) {

                System.out.println("Inside Token fxn");

                String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

                Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                                SignatureAlgorithm.HS256.getJcaName());

                System.out.println("KEy::::::>" + hmacKey);

                Instant now = Instant.now();
                System.out.println("Now:::::>" + now);
                System.out.println("Date:::::::::::> " + Date.from(now.plus(5, ChronoUnit.MINUTES)));
                System.out.println("Date 1:::::::::::> " + Date.from(now));

                String jwtToken = Jwts.builder()
                                .claim("id", id)
                                .claim("name", name)
                                .claim("email", email)
                                .setSubject("Token")
                                .setId(UUID.randomUUID().toString())
                                .setIssuedAt(Date.from(now))
                                .setExpiration(Date.from(now.plus(1, ChronoUnit.MINUTES)))
                                .signWith(hmacKey)
                                .compact();
                System.out.println("JWT Token:::::::>" + jwtToken);

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

                Claims claims = jwt.getBody();
                Date expirationDateTime = claims.getExpiration();

                Instant now = Instant.now();

                Date currentDateTime = Date.from(now);

                System.out.println("Expiration Time::::::>" + expirationDateTime);
                System.out.println("Current Time::::::>" + currentDateTime);

                if (currentDateTime.compareTo(expirationDateTime) > 0) {
                        System.out.println("JWT token has expired");

                } else {
                        System.out.println("JWT token is still valid");
                }
                return jwt;

        }

        public String generateRefreshToken(int id, String name, String email) throws NoSuchAlgorithmException {

                Date expiration = new Date(System.currentTimeMillis() + 30 * 24 * 60 * 60 * 1000L);

                String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
                Key secretKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                                SignatureAlgorithm.HS256.getJcaName());
                System.out.println("Secrete key1111:::::>" + secretKey);

                JwtBuilder builder = Jwts.builder()
                                .claim("id", id)
                                .claim("name", name)
                                .claim("email", email)
                                .setSubject("Token")
                                .setExpiration(expiration)
                                .signWith(secretKey, SignatureAlgorithm.HS256);

                return builder.compact();
        }

        public Jws<Claims> validateRefreshToken(String refreshToken1) throws NoSuchAlgorithmException {

                String refreshToken = refreshToken1.trim();
                String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
                Key secretKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                                SignatureAlgorithm.HS256.getJcaName());

                System.out.println("Secrete Key:::::>" + secretKey);
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                                .setSigningKey(secretKey)
                                .build()
                                .parseClaimsJws(refreshToken);

                return claimsJws;

        }

}