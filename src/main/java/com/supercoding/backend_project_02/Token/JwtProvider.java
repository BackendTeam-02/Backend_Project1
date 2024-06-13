package com.supercoding.backend_project_02.Token;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.supercoding.backend_project_02.entity.users.Users;
import com.supercoding.backend_project_02.repository.users.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;





@RequiredArgsConstructor
public class JwtProvider {


    private final UserRepository userRepository;

    static Long EXPIRE_TIME = 60L * 60L * 1000L; // 만료 시간 1시간

    private static String secretKey = "Vwgff4uvzQ4pes0Zt7sDNtL6pxGIkg2k95ZIrVhvlGmxcDRq9O1fnLN2lEzItsNE4w_lQ3f7xd09ukYxzIYS6InrYfg4ed2BSP0wmZ2RJEswzDsCLNqwRRXW780o";




    private static Algorithm getSign(){
        return Algorithm.HMAC512(secretKey);
    }

    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
    }

    public static String generateJwtToken(int id, String email){

        Date tokenExpiration = new Date(System.currentTimeMillis() + (EXPIRE_TIME));
        String jwtToken = JWT.create()
                .withSubject(email) //토큰 이름
                .withExpiresAt(tokenExpiration)
                .withClaim("id", id)
                .withClaim("email", email)
                .sign(getSign());

        return jwtToken;
    }
    public Users validToken(String jwtToken){
        try {

            String email = JWT.require(this.getSign())
                    .build().verify(jwtToken).getClaim("email").asString();

            // 비어있는 값이다.
            if (email == null){
                return null;
            }

            // EXPIRE_TIME이 지나지 않았는지 확인
            Date expiresAt = JWT.require(this.getSign()).acceptExpiresAt(EXPIRE_TIME).build().verify(jwtToken).getExpiresAt();
            if (!this.validExpiredTime(expiresAt)) {
                // 만료시간이 지났다.
                return null;
            }

            Users tokenUser = userRepository.findByEmail(email);

            return tokenUser;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    // 만료 시간 검증
    private boolean validExpiredTime(Date expiresAt){
        // LocalDateTime으로 만료시간 변경
        LocalDateTime localTimeExpired = expiresAt.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        // 현재 시간이 만료시간의 이전이다
        return LocalDateTime.now().isBefore(localTimeExpired);

    }
}
