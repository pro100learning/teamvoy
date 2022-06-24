package com.teamvoy.shop.security.jwt;

import com.teamvoy.shop.security.UserSecurity;
import com.teamvoy.shop.security.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserSecurityService userSecurityService;

    @Value("${app.auth.not-auth-endpoints}")
    private String[] allowedNotAuthEndpoints;

    @Value("${security.jwt.header}")
    private String Authorization;

    @Value("${security.jwt.prefix}")
    private String Prefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getLoginFromToken(token);
            UserSecurity userSecurity = userSecurityService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userSecurity, null, userSecurity.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(Authorization);
        if (hasText(bearer) && bearer.startsWith(Prefix + " ")) {
            return bearer.substring(7);
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String[] inauthenticationEndpoints = allowedNotAuthEndpoints;
        return Stream.of(inauthenticationEndpoints).anyMatch(path::matches);
    }
}
