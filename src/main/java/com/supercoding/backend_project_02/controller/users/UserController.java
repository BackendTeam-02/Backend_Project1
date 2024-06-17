package com.supercoding.backend_project_02.controller.users;

import com.supercoding.backend_project_02.dto.users.LoginDto;
import com.supercoding.backend_project_02.dto.users.UserDto;
import com.supercoding.backend_project_02.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> join(@RequestBody UserDto userDto) {
        userService.join(userDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "회원가입이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDto loginDto) {
        String token = userService.login(loginDto);
        if (token.startsWith("로그인 실패")) {
            Map<String, String> response = new HashMap<>();
            response.put("message", token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "로그인이 성공적으로 완료되었습니다.");
            return ResponseEntity.ok(response);
        }
    }
}