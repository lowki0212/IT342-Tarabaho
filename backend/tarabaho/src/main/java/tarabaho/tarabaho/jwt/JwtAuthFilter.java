package tarabaho.tarabaho.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tarabaho.tarabaho.service.AdminService;
import tarabaho.tarabaho.service.UserService;
import tarabaho.tarabaho.service.WorkerService;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private WorkerService workerService;

    private static final List<String> SKIP_FILTER_PATHS = Arrays.asList(
            "/api/admin/login",
            "/api/admin/register",
            "/api/admin/logout",
            "/api/user/login",
            "/api/user/register",
            "/api/user/token",
            "/api/worker/register",
            "/api/worker/check-duplicates",
            "/api/worker/token",
            "/api/worker/login",
            "/api/worker/*/upload-initial-picture",
            "/api/certificate/worker/**",
            "/oauth2/**",
            "/login/**",
            "/oauth2-success",
            "/profiles/**"
    );

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        System.out.println("JwtAuthFilter: Checking URI: " + path + ", Method: " + method);
        boolean shouldNotFilter = SKIP_FILTER_PATHS.stream()
                .peek(pattern -> System.out.println("JwtAuthFilter: Comparing with pattern: " + pattern))
                .anyMatch(pattern -> {
                    boolean match = pathMatcher.match(pattern, path);
                    System.out.println("JwtAuthFilter: Pattern " + pattern + " matches: " + match);
                    return match;
                });
        System.out.println("JwtAuthFilter: shouldNotFilter = " + shouldNotFilter + " for URI: " + path);
        return shouldNotFilter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("JwtAuthFilter: Processing request for URI: " + path);

        String jwtToken = null;

        // ✅ Check Authorization header first (for Android app)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
            System.out.println("JwtAuthFilter: Found jwtToken in Authorization header (truncated): " +
                    jwtToken.substring(0, Math.min(jwtToken.length(), 10)) + "...");
        }

        // ✅ Fallback to cookie (for web clients)
        if (jwtToken == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) {
                        jwtToken = cookie.getValue();
                        System.out.println("JwtAuthFilter: Found jwtToken in cookie (truncated): " +
                                jwtToken.substring(0, Math.min(jwtToken.length(), 10)) + "...");
                        break;
                    }
                }
            }
        }

        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                System.out.println("JwtAuthFilter: Validating token...");
                String username = jwtUtil.getUsernameFromToken(jwtToken);

                if (username != null && jwtUtil.validateToken(jwtToken)) {
                    System.out.println("JwtAuthFilter: Token valid, username: " + username);

                    // Check Admin first
                    tarabaho.tarabaho.entity.Admin admin = adminService.findByUsername(username);
                    if (admin != null) {
                        System.out.println("JwtAuthFilter: Authenticated as Admin: " + username);
                        UserDetails userDetails = new User(admin.getUsername(), admin.getPassword(), Collections.emptyList());
                        setAuthentication(request, userDetails);
                    } else {
                        // Check User
                        tarabaho.tarabaho.entity.User user = userService.findByUsername(username).orElse(null);
                        if (user != null) {
                            System.out.println("JwtAuthFilter: Authenticated as User: " + username);
                            UserDetails userDetails = new User(user.getUsername(), user.getPassword(), Collections.emptyList());
                            setAuthentication(request, userDetails);
                        } else {
                            // Check Worker
                            tarabaho.tarabaho.entity.Worker worker = workerService.findByUsername(username).orElse(null);
                            if (worker != null) {
                                System.out.println("JwtAuthFilter: Authenticated as Worker: " + username);
                                UserDetails userDetails = new User(worker.getUsername(), worker.getPassword(), Collections.emptyList());
                                setAuthentication(request, userDetails);
                            } else {
                                System.out.println("JwtAuthFilter: No Admin, User, or Worker found for username: " + username);
                            }
                        }
                    }
                } else {
                    System.out.println("JwtAuthFilter: Token invalid or username not found in token.");
                }
            } catch (Exception e) {
                System.out.println("JwtAuthFilter: Token validation failed: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        } else {
            System.out.println("JwtAuthFilter: No jwtToken found or authentication already set or skipping filter.");
        }

        System.out.println("JwtAuthFilter: Proceeding to next filter...");
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}