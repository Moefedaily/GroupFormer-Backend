package com.groupformer.security;

import com.groupformer.model.User;
import com.groupformer.service.UserService;
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
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

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
                if (jwtUtil.validateToken(jwtToken, username)) {

                    String tokenPurpose = jwtUtil.extractTokenPurpose(jwtToken);

                    if (isValidLoginToken(tokenPurpose)) {

                        Optional<User> userOptional = userService.getUserByEmail(username);

                        if (userOptional.isPresent()) {
                            User user = userOptional.get();

                            Boolean emailVerified = jwtUtil.extractEmailVerified(jwtToken);

                            CustomUserDetails userDetails = new CustomUserDetails(user, emailVerified);

                            System.out.println("User authenticated: " + userDetails.getUsername() +
                                    ", Email verified: " + emailVerified +
                                    ", Enabled: " + userDetails.isEnabled());

                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails,
                                            null,
                                            userDetails.getAuthorities()
                                    );

                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(authToken);

                            if (!userDetails.isEnabled()) {
                                System.out.println("Access denied: Email not verified for user " + username);
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.getWriter().write("{\"error\":\"Email verification required\",\"code\":\"EMAIL_NOT_VERIFIED\"}");
                                response.setContentType("application/json");
                                return;
                            }

                            System.out.println("User authenticated successfully with role: " + userDetails.getAuthorities());

                        } else {
                            System.out.println("User not found in database: " + username);
                        }

                    } else {
                        System.out.println("Invalid token purpose for authentication: " + tokenPurpose);
                    }

                } else {
                    System.out.println("JWT Token validation failed");
                }

            } catch (Exception e) {
                System.out.println("Error during JWT authentication: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidLoginToken(String tokenPurpose) {
        return "LOGIN".equals(tokenPurpose) ||
                "UNVERIFIED_USER".equals(tokenPurpose) ||
                tokenPurpose == null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/") ||
                path.startsWith("/api/studentlists/public/") ||
                path.equals("/api/auth/verify-email") ||
                path.startsWith("/api/auth/verify-email/");
    }
}