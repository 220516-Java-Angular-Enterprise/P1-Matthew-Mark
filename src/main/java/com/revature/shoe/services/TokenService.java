package com.revature.shoe.services;

import com.revature.shoe.dtos.responses.Principal;
import com.revature.shoe.util.JwtConfig;
import com.revature.shoe.util.annotations.Inject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class TokenService {
    @Inject
    private JwtConfig jwtConfig;

    public TokenService() {
        super();
    }

    @Inject
    public TokenService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(Principal subject){
        long now = System.currentTimeMillis();
        JwtBuilder tokenBuilder = Jwts.builder()
                .setId(subject.getId())
                .setIssuer("Shoe")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration()))
                .setSubject(subject.getUsername())
                .claim("role", subject.getRole())
                .signWith(jwtConfig.getSigAlg(), jwtConfig.getSigningKey());

        return tokenBuilder.compact();
    }

    public Principal extractRequesterDetails(String token){
        try{
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();

            return new Principal(claims.getId(), claims.getSubject(), claims.get("role", String.class));
        }catch (Exception e){
            return null;
        }
    }
}
