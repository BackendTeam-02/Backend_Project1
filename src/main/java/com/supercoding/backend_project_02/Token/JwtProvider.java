package com.supercoding.backend_project_02.Token;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.supercoding.backend_project_02.entity.users.Users;
import com.supercoding.backend_project_02.repository.users.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


import java.util.Base64;
import java.util.Date;
import java.util.Optional;


@Primary
@Component
@RequiredArgsConstructor
public class JwtProvider {


   private final UserRepository userRepository;


   static Long EXPIRE_TIME = 60L * 60L * 1000L; // 만료 시간 1시간


   private static String secretKey = "Vwgff4uvzQ4pes0Zt7sDNtL6pxGIkg2k95ZIrVhvlGmxcDRq9O1fnLN2lEzItsNE4w_lQ3f7xd09ukYxzIYS6InrYfg4ed2BSP0wmZ2RJEswzDsCLNqwRRXW780o";


   private static Algorithm getSign() {
       return Algorithm.HMAC512(secretKey);
   }


   @PostConstruct
   protected void init() {
       this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
   }


   public String generateJwtToken(int id, String email) {
       Date tokenExpiration = new Date(System.currentTimeMillis() + EXPIRE_TIME);
       String token = JWT.create()
                         .withSubject(email)
                         .withExpiresAt(tokenExpiration)
                         .withClaim("id", id)
                         .withClaim("email", email)
                         .sign(getSign());
       System.out.println("Generated token: " + token);
       return token;
   }


   public Users validToken(String jwtToken) {
       try {
           String email = JWT.require(getSign())
                             .build().verify(jwtToken).getClaim("email").asString();


           if (email == null) {
               return null;
           }


           Optional<Users> optionalUser = userRepository.findByEmail(email);
           return optionalUser.orElse(null);


       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }


   public String getEmailFromToken(String token) {
       if (token == null || !token.contains(".")) {
           throw new IllegalArgumentException("Invalid JWT token format");
       }
       try {
           System.out.println("JWT token: " + token);


           String email = JWT.require(getSign())
                             .build()
                             .verify(token)
                             .getClaim("email")
                             .asString();


           System.out.println("Extracted email from token: " + email);
           return email;
       } catch (JWTDecodeException exception) {
           exception.printStackTrace();
           throw new IllegalArgumentException("Invalid JWT token format", exception);
       } catch (TokenExpiredException exception) {
           exception.printStackTrace();
           throw new IllegalArgumentException("JWT token has expired", exception);
       } catch (SignatureVerificationException exception) {
           exception.printStackTrace();
           throw new IllegalArgumentException("JWT token signature verification failed", exception);
       } catch (Exception e) {
           e.printStackTrace();
           throw new IllegalArgumentException("Invalid JWT token");
       }
   }


   public String getTokenForEmail(String email) {
       Optional<Users> user = userRepository.findByEmail(email);
       if (user.isPresent()) {
           return generateJwtToken(user.get().getId(), user.get().getEmail());
       }
       throw new IllegalArgumentException("Invalid email");
   }
}
