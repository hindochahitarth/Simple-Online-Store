package org.example.simpleonlinestore.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value(value = "${security.jwt.secret-key}")
    private String secretKey;

    @Value(value = "${security.jwt.expiration-time}")
    private long jwtExpiration;

    // extracting username from jwt by reading subject field

    public String extractUsername(String token) {
        // helper met   hod opens token and reads it secret data(claims)
        return extractClaim(token, Claims::getSubject);// look for specific part
        // sub-field
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // jwt structure::header payload signature
    // header specify algorithm,payload custom metadata (claims) and subject and
    // expiry
    // signature safety lock
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        if (userDetails != null && userDetails.getAuthorities() != null) {
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            extraClaims.put("roles", roles);
        }
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public long getExpirationTime() {
        return jwtExpiration;
    }

    //
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) {
        // map for json key value pair
        // key is string because all json keys are double quoted cant have key as
        // integer in json

        return Jwts
                .builder()// creates a new internal object to store details
                .setClaims(extraClaims)// takes custom map and inserts in payload
                .setSubject(userDetails.getUsername())// sets email
                .setIssuedAt(new Date(System.currentTimeMillis()))// takes current timw
                .setExpiration(new Date(System.currentTimeMillis() + expiration))// creates a future timestamp
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)// locks token,encodes json using base64url
                .compact();// bsae64url encodes binary signature stores into single string
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        // checks username is matches and token is still fresh then user logged in
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        // checks if token expiration time is before than current then return true means
        // token is expired
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts// jwts main helper class of open source jjwt by okta tool to process jwt raw
                // string
                .parserBuilder()
                .setSigningKey(getSignInKey()) // verification of token
                .build()// start tool
                .parseClaimsJws(token)// scans all token
                .getBody();// get raw data inside
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);// takes long text based secret key and cnverts to base64
        // into array of bytes
        return Keys.hmacShaKeyFor(keyBytes);// take those raw bytes and format into secure java key object
    }
}


