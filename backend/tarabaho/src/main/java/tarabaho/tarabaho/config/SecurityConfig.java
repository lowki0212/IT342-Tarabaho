package tarabaho.tarabaho.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import tarabaho.tarabaho.jwt.JwtAuthFilter;
import tarabaho.tarabaho.jwt.JwtUtil;
import tarabaho.tarabaho.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@Autowired
    private JwtAuthFilter jwtAuthenticationFilter;
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/api/admin/register", "/api/admin/login", "/api/admin/logout").permitAll()
                .requestMatchers("/api/admin/**").authenticated()
                .requestMatchers("/api/user/login", "/api/user/register", "/api/user/token").permitAll()
                .requestMatchers("/api/user/me", "/api/user/update-phone").authenticated()
                .requestMatchers("/oauth2/**", "/login/**", "/oauth2-success").permitAll()
                .requestMatchers("/profiles/**").permitAll() // Allow public access to images
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth	
                .defaultSuccessUrl("/oauth2-success", true)
                .failureUrl("http://localhost:5173/login-failed")
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService()))
            )
            .logout(logout -> logout
                .logoutUrl("/api/user/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    Cookie tokenCookie = new Cookie("jwtToken", null);
                    tokenCookie.setMaxAge(0);
                    tokenCookie.setPath("/");
                    tokenCookie.setDomain("localhost");
                    tokenCookie.setHttpOnly(true);
                    tokenCookie.setSecure(false);
                    response.addCookie(tokenCookie);
                    System.out.println("Logout: Sent Set-Cookie - jwtToken=; Path=/; Domain=localhost; Max-Age=0; HttpOnly");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("User logged out successfully.");
                })
                .permitAll()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                })
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://10.0.2.2:8080"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(List.of("*"));
    
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }
}