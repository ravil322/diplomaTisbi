package ru.ravnasybullin.DoiReg.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ravnasybullin.DoiReg.dto.UserData;
import ru.ravnasybullin.DoiReg.models.User;
import ru.ravnasybullin.DoiReg.repositories.UserRepository;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);
    public static final String JWT_SECRET = "AVeryLongLongJWTSecretArmOperator";

    @Autowired
    private UserRepository userRepository;

    public UserData generateToken(User user) {
        Instant expirationTime = Instant.now().plus(365, ChronoUnit.DAYS);
        Date expirationDate = Date.from(expirationTime);

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

        String compactTokenString = Jwts.builder()
                .claim("login", user.getLogin())
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        UserData userData = new UserData();
        userData.setAccessToken(compactTokenString);
        userData.setLogin(user.getLogin());
        LOGGER.info("Пользователь {} авторизовался", user.getLogin());
        return userData;
    }

    public User parseToken(String token) throws Exception {
        byte[] secretBytes = JWT_SECRET.getBytes();

        Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token);

        String name = jwsClaims.getBody()
                .get("name", String.class);
        String login = jwsClaims.getBody()
                .get("login", String.class);

        return userRepository.findByLogin(login);
    }
}
