package tarabaho.tarabaho.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import tarabaho.tarabaho.service.AdminService;
import tarabaho.tarabaho.service.UserService;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("JwtAuthFilter processing path: " + path); // Debug log

        // Skip authentication for public endpoints
        if (path.equals("/api/admin/login") || path.equals("/api/admin/register") || path.equals("/api/admin/logout") ||
            path.equals("/api/user/login") || path.equals("/api/user/register") || path.equals("/api/user/token") ||
            path.startsWith("/oauth2/") || path.startsWith("/login/") || path.equals("/oauth2-success")) {
            System.out.println("Skipping authentication for: " + path); // Debug log
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtUtil.getUsernameFromToken(jwtToken);
            if (username != null && jwtUtil.validateToken(jwtToken)) {
                // Try Admin first
                tarabaho.tarabaho.entity.Admin admin = adminService.findByUsername(username);
                if (admin != null) {
                    UserDetails userDetails = new User(admin.getUsername(), admin.getPassword(), Collections.emptyList());
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // Try User
                    tarabaho.tarabaho.entity.User user = userService.findByUsername(username).orElse(null);
                    if (user != null) {
                        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), Collections.emptyList());
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}