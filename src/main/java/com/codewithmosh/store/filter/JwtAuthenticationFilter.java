package com.codewithmosh.store.filter;

import com.codewithmosh.store.services.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = authHeader.replace("Bearer ", "");
        try {
            if (!jwtService.validateToken(token)) {
                // token expired or otherwise invalid
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }

            Long userId = jwtService.getId(token);
            System.out.println("userId = " + userId);
            if (userId == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token subject");
                return;
            }

            var authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    null);
            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            // invalid token (signature, parsing, subject, etc.)
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }
    }
}
