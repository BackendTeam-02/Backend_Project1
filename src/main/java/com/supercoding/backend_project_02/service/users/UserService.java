package com.supercoding.backend_project_02.service.users;

import com.supercoding.backend_project_02.Token.JwtProvider;
import com.supercoding.backend_project_02.dto.users.LoginDto;
import com.supercoding.backend_project_02.dto.users.UserDto;
import com.supercoding.backend_project_02.entity.users.Users;
import com.supercoding.backend_project_02.repository.users.UserRepository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    // 사용자 데이터를 데이터베이스에서 조회하거나 저장하는 데 사용
    private final UserRepository userRepository;
    // 비밀번호를 암호화하거나 암호화된 비밀번호를 검증하는 데 사용
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    // JWT 토큰을 생성하거나 검증하는 데 사용
    private final JwtProvider jwtProvider;


    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, @Qualifier("jwtTokenProvider") JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public void join(UserDto userDto) {
        String email = userDto.getEmail();
        String password = userDto.getPassword();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        }

        Users data = new Users();
        data.setEmail(email);
        data.setPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(data);
        System.out.println("회원가입 성공: " + email);
    }

    public String login(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String rawPassword = loginDto.getPassword();

        Optional<Users> optionalUsers = userRepository.findByEmail(email);

        if (optionalUsers.isEmpty()) {
            return "로그인 실패: 존재하지 않는 사용자";
        }

        Users user = optionalUsers.get();

        if (bCryptPasswordEncoder.matches(rawPassword, user.getPassword())) {
            String jwtToken = jwtProvider.generateJwtToken(user.getId(), user.getEmail());
            return jwtToken;
        } else {
            return "로그인 실패: 비밀번호 불일치";
        }
    }
}