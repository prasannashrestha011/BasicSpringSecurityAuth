package application.source.backend.Filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import application.source.backend.Jwt.JwtService;
import application.source.backend.Services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Lazy
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // Skip JWT validation for specific paths
        if (requestURI.startsWith("/account/login")) {
            filterChain.doFilter(request, response);
            return; // Skip further processing in the filter for this request
        }
        String bearerToken = getJwtToken(request);
        String subject = jwtService.extractUsername(bearerToken);
        UserDetails user = userDetailsService.loadUserByUsername(subject);
        if (bearerToken != null && user != null && jwtService.isTokenValid(bearerToken, user)) {
            UsernamePasswordAuthenticationToken authUser = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities());
            authUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authUser);
        }
        filterChain.doFilter(request, response);
    }

    public String getJwtToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
