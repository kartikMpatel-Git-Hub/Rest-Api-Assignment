package com.ems.ems.security;

import com.ems.ems.exception.SomethingWentWrongException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenHelper jwtTokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String requestToken = request.getHeader("Authorization");

        String userName = null;

        String token = null;

        if(requestToken != null && requestToken.startsWith("Bearer ")){
            token = requestToken.substring(7);
            try {
                userName = jwtTokenHelper.getUserNameFromToken(token);
            }catch (IllegalArgumentException e){
                throw new SomethingWentWrongException("Unable To Get Jwt Token !");
            }catch (ExpiredJwtException e){
                throw new SomethingWentWrongException("Token Expire !");
            }catch (MalformedJwtException e){
                throw new SomethingWentWrongException("Invalid Jwt Token !");
            }
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            if(jwtTokenHelper.validateToken(token,userDetails)){
                if (!userDetails.isEnabled()) {
                    throw new SomethingWentWrongException("User is disabled!");
                }
                if (!userDetails.isAccountNonLocked()) {
                    throw new SomethingWentWrongException("User account is locked!");
                }
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }else{
                throw new SomethingWentWrongException("Invalid Jwt Token !");
            }
        }

        filterChain.doFilter(request,response);
    }
}
