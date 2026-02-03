package com.ems.ems.security;

import com.ems.ems.exception.ResourceNotFoundException;
import com.ems.ems.model.UserModel;
import com.ems.ems.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenHelper {
    public static final long JWT_TOKEN_VALIDITY = 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private final UserRepository userRepository;

    @PostConstruct
    public void initializeKey(){
        this.key  = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String getUserNameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationTime(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().
                setSigningKey(key).
                build().
                parseClaimsJws(token).
                getBody();
    }

    public  Boolean isTokenExpire(String token){
        final Date expiration = getExpirationTime(token);
        return expiration.before(new Date());
    }

    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        UserModel user = userRepository.findByUserName(userDetails.getUsername()).orElseThrow(
                ()-> new ResourceNotFoundException("User","userName",userDetails.getUsername())
        );
        claims.put("userType","ADMIN");
        claims.put("userName",user.getUsername());
        return doGenerateToken(claims,user.getUsername());
    }

    private String doGenerateToken(Map<String,Object> claims,String subject){

        return Jwts.
                builder().
                setClaims(claims).
                setSubject(subject).
                setIssuedAt(new Date(System.currentTimeMillis())).
                setExpiration(new Date(System.currentTimeMillis()+ JWT_TOKEN_VALIDITY * 1000)).
                signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token,UserDetails userDetails){
        final String userName = getUserNameFromToken(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpire(token);
    }

}
