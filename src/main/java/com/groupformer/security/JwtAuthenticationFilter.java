package com.groupformer.security;

import com.groupformer.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);

            try {
                username = jwtUtil.extractUsername(jwtToken);
                System.out.println("JWT Token found for user: " + username);
            } catch (Exception e) {
                System.out.println("Invalid JWT Token: " + e.getMessage());
            }
        } else {
            System.out.println("No JWT Token found in request to: " + request.getRequestURI());
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("Loaded user details for: " + userDetails.getUsername());

                if (jwtUtil.validateToken(jwtToken, userDetails.getUsername())) {
                    System.out.println("JWT Token is valid");

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("User authenticated successfully with role: " + userDetails.getAuthorities());

                } else {
                    System.out.println("JWT Token validation failed");
                }

            } catch (Exception e) {
                System.out.println(" Error during JWT authentication: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}