package com.supercoding.backend_project_02.service.users;

import com.supercoding.backend_project_02.Token.JwtProvider;
import com.supercoding.backend_project_02.dto.users.LoginDto;
import com.supercoding.backend_project_02.dto.users.UserDto;

import com.supercoding.backend_project_02.entity.users.Users;
import com.supercoding.backend_project_02.repository.users.UserRepository;



import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private  final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void join(UserDto userDto) {

        String email = userDto.getEmail();
        String password = userDto.getPassword();

        Users data = new Users();

        data.setEmail(email);
        data.setPassword(bCryptPasswordEncoder.encode(password));

        userRepository.save(data);

    }
    public Users findById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public String login(LoginDto loginDto) {
        String email = loginDto.getEmail();
        String rawPassword = loginDto.getPassword();

        Users FindEmail = userRepository.findByEmail(email);

        // 비밀번호 일치 여부 확인
        if(bCryptPasswordEncoder.matches(rawPassword, FindEmail.getPassword())){

                // JWT 토큰 반환
                String jwtToken = JwtProvider.generateJwtToken(FindEmail.getId(), FindEmail.getEmail());
                return "로그인 성공: "
                        + jwtToken;

        }
        return "로그인 실패";
    }


    }




