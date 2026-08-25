package org.example.simpleonlinestore.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService,
                                   @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.userDetailsService = userDetailsService;

    }

    //
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();


        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }
        // filter chain is chain of security checks that an HTTP request must pass
        // through before reaching code
        final String authHeader = request.getHeader("Authorization");
        // looks inside http headers for key authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {//part of O Auth authorization
            //Authorization: Bearer <JWT_TOKEN> for stateless jwt
            //Authorization: Basic<BASE64_Credentials> for raw username password
            // if header is missing or it does not start with bearer means user is trying to
            // access
            // public pages so it will pass the security check
            //filterChain.doFilter(request, response);
//            JwtTokenMissingException exception =
//                    new JwtTokenMissingException(
//                            "Authorization token not found. Please provide a Bearer token."
//                    );
//
//            handlerExceptionResolver.resolveException(
//                    request,
//                    response,
//                    null,
//                    exception
//            );


            return;
        }
        // token extraction
        try {
            // prefix bearer is exactly 7 characters long so it cuts first 7 characters
            // Bearer actual jwt string
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);// pass token decodes it and reads actual data

            // security context store details of user who is logged in currently
            //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // checks if we found userEmail and authentication
            if (userEmail != null && authentication == null) {
                // loads user by email
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                // used to check jwt is not expired ,internal signature matches
                // checks signature valid using secret key
                // compares expiration inside token against current time
                // compares username inside token with username loaded from database
                if (jwtService.isTokenValid(jwt, userDetails)) {

                    //used to target specific key "roles" inside jwt payload
                    //wildcard used java does not know roles are stored in plain strings ("ROLE_USER")
                    List<?> roles = jwtService.extractClaim(jwt, claims -> claims.get("roles", java.util.List.class));
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();//used to hold roles as security cant understand raw strings
                    if (roles != null) {
                        authorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority(role.toString()))
                                .toList();
                    }
                    // loads user profile from databases
                    // 2nd field is null because field is for psasword
                    // create authentication object ,3rd field for user roles /permissions
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, authorities);
                    // add extra details like user's ip and session id
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // saves token into security context
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            logger.error("JWT Filter interception error occurred: ", e);

            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        }

        catch (JwtException e){
            handlerExceptionResolver.resolveException(request,response,null,e);
            return;
        }
        //filterChain.doFilter(request, response);
        catch (IllegalArgumentException e) {
//
//            logger.warn("Invalid JWT input: {}", e.getMessage());

            handlerExceptionResolver.resolveException(
                    request, response, null, e
            );

            return;
        }
    }
}
