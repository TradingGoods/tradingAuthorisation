package com.exchangeify.authorisation.utils;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;


import io.jsonwebtoken.*;
import java.util.function.Function;
@Service
public class JwtUtil {

   private final Map<String, PublicKey> googlePublicKeys = new HashMap<>();
   private static final String GOOGLE_CERTS_URL = "https://www.googleapis.com/oauth2/v3/certs";
   private final ObjectMapper objectMapper = new ObjectMapper();
   
    public String generateToken(String userName, String password) {
        return Jwts.builder()
                .setSubject(userName)
                .setIssuer("backend")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Token validity
                .signWith(createKey(password)) // Use the secret key to sign the JWT
                .compact();
    }

    private Key createKey(String password){
        return Keys.hmacShaKeyFor(password.getBytes(StandardCharsets.UTF_8));
    }

    public String getUsernameFromToken(String token) {
        String kid = JwtUtil.getKidFromToken(token);
        try {
          PublicKey publicKey = getGooglePublicKey(kid);
          Claims claims = getClaimsFromToken(token,publicKey);
          if(isTokenExpired(token,publicKey)){
            return claims.get("email", String.class);
          } else{
            return null;
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Claims getClaimsFromToken(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getKidFromToken(String token) {
        // Split the token to separate the header, payload, and signature
        String[] tokenParts = token.split("\\.");
        
        if (tokenParts.length < 2) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
        
        // Decode the header
        String headerJson = new String(Base64.getUrlDecoder().decode(tokenParts[0]));
        
        // Parse the header as JSON to retrieve the 'kid'
        JSONObject header = new JSONObject(headerJson);
        return header.optString("kid", null);
    }


    // Method to fetch and parse the public key based on the `kid`
    private PublicKey getGooglePublicKey(String kid) throws Exception {
        // Fetch Google’s public keys
        JsonNode keys = objectMapper.readTree(new URL(GOOGLE_CERTS_URL)).get("keys");

        // Locate the key with matching `kid`
        for (JsonNode key : keys) {
            if (key.get("kid").asText().equals(kid)) {
                String modulus = key.get("n").asText();
                String exponent = key.get("e").asText();

                // Decode and build the RSA public key
                byte[] decodedModulus = Base64.getUrlDecoder().decode(modulus);
                byte[] decodedExponent = Base64.getUrlDecoder().decode(exponent);
                return buildRsaPublicKey(decodedModulus, decodedExponent);
            }
        }
        throw new IllegalArgumentException("No matching key found for kid: " + kid);
    }

    // Helper method to build RSA public key
    private PublicKey buildRsaPublicKey(byte[] modulus, byte[] exponent) throws Exception {
        java.math.BigInteger modBigInt = new java.math.BigInteger(1, modulus);
        java.math.BigInteger expBigInt = new java.math.BigInteger(1, exponent);
        java.security.spec.RSAPublicKeySpec keySpec = new java.security.spec.RSAPublicKeySpec(modBigInt, expBigInt);
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    public Claims verifyToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                    @Override
                    public PublicKey resolveSigningKey(io.jsonwebtoken.JwsHeader header, Claims claims) {
                        String kid = header.getKeyId();
                        return googlePublicKeys.get(kid); // Use the kid to find the correct key
                    }
                })
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    @PostConstruct
    public void initializeKeys() {
        fetchGooglePublicKeys();
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fetchGooglePublicKeys();
            }
        }, 0, 6 * 60 * 60 * 1000); // Every 6 hours
    }
    private void fetchGooglePublicKeys() {
        try {
            URL url = new URL(GOOGLE_CERTS_URL);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode keysNode = objectMapper.readTree(url).get("keys");

            googlePublicKeys.clear();
            for (JsonNode keyNode : keysNode) {
                String kid = keyNode.get("kid").asText();
                String modulus = keyNode.get("n").asText();
                String exponent = keyNode.get("e").asText();
                PublicKey publicKey = generatePublicKey(modulus, exponent);
                googlePublicKeys.put(kid, publicKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PublicKey generatePublicKey(String modulus, String exponent) throws Exception {
        BigInteger n = new BigInteger(1, Base64.getUrlDecoder().decode(modulus));
        BigInteger e = new BigInteger(1, Base64.getUrlDecoder().decode(exponent));
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public boolean validateToken(String token, String username, String password) {
        String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token, createKey(password)));
    }

    private boolean isTokenExpired(String token, Key key) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    private final String SECRET_KEY = "your-very-secret-key-must-be-at-least-256-bits";
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
