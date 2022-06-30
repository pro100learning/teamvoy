package com.teamvoy.shop.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

public class TokenGeneratorService {

    private static String SECRET = "JwtSecretKey";

    public static String createToken(String username) {
        Date date = Date.from(LocalDate.now()
                .plusDays(15)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .addClaims(Map.of("email", username))
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }
}
