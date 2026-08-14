package com.resumeiq.backend.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.resumeiq.backend.constant.SecurityConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService) {

        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Do not run JWT authentication for public Swagger/OpenAPI resources.
     */
    @Override
    protected boolean shouldNotFilter(
            @NonNull HttpServletRequest request) {

        String path = request.getServletPath();

        return path.equals("/swagger-ui.html")
                || path.startsWith("/swagger-ui/")
                || path.equals("/v3/api-docs")
                || path.startsWith("/v3/api-docs/")
                || path.equals("/favicon.ico")
                || path.startsWith("/webjars/");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String requestUri = request.getRequestURI();

        final String authHeader =
                request.getHeader(SecurityConstants.AUTH_HEADER);

        log.debug("Processing JWT authentication for [{}]", requestUri);

        /*
         * No Authorization header.
         *
         * This is not necessarily an error because some endpoints may
         * be public. Simply continue the filter chain.
         */
        if (authHeader == null
                || !authHeader.startsWith(
                        SecurityConstants.TOKEN_PREFIX)) {

            log.debug(
                    "No Bearer token provided for [{}]",
                    requestUri
            );

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader
                .substring(SecurityConstants.TOKEN_PREFIX.length())
                .trim();

        if (jwt.isBlank()) {

            log.debug(
                    "Empty JWT token for [{}]",
                    requestUri
            );

            filterChain.doFilter(request, response);
            return;
        }

        try {

            String username =
                    jwtService.extractUsername(jwt);

            if (username != null
                    && SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                UserDetails userDetails =
                        customUserDetailsService
                                .loadUserByUsername(username);

                boolean valid =
                        jwtService.isTokenValid(
                                jwt,
                                userDetails
                        );

                if (valid) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);

                    log.debug(
                            "JWT authentication successful for user [{}]",
                            username
                    );

                } else {

                    log.debug(
                            "Invalid JWT token for [{}]",
                            requestUri
                    );
                }
            }

        } catch (Exception ex) {

            /*
             * Invalid JWT should not crash the entire request.
             * The SecurityConfig will decide whether the endpoint
             * requires authentication.
             */
            log.debug(
                    "JWT validation failed for [{}]: {}",
                    requestUri,
                    ex.getMessage()
            );

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}