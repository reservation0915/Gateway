package com.example.gateway.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public Boolean parseToken(String token) {
//        SecretKeySpec key = getKey();
        try{
            Claims body= (Claims) Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build()
                    .parse(token)
                    .getBody();

            TokenInfo info = TokenInfo.builder()
                    .id(UUID.fromString(body.get("id", String.class)))
                    .number(body.get("number", String.class))
                    .name(body.get("name", String.class))
                    .role(body.get("role", String.class))
                    .build();

            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }


    }

}
