package com.ems.ems.security;

import com.ems.ems.exception.SomethingWentWrongException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
        String token = null;
        if(requestToken != null && requestToken.startsWith("Bearer ")) {
            token = requestToken.substring(7);
        }
        if(token == null && request.getCookies() != null){
            for(Cookie c : request.getCookies()){
                if("token".equals(c.getName())){
                    token = c.getValue();
                    break;
                }
            }
        }
        if(token == null){
            filterChain.doFilter(request,response);
            return;
        }
        try{
            String username = jwtTokenHelper.getUserNameFromToken(token);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtTokenHelper.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request,response);
        }catch (ExpiredJwtException ex){
            throw new SomethingWentWrongException("TOKEN_EXPIRED");
        } catch (JwtException ex){
            throw new SomethingWentWrongException("INVALID_TOKEN");
        }
    }
}
