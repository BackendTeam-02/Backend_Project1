package com.supercoding.backend_project_02.Controller.users;

import com.supercoding.backend_project_02.dto.users.LoginDto;
import com.supercoding.backend_project_02.dto.users.UserDto;
import com.supercoding.backend_project_02.service.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;



    @PostMapping("/join")
    public String join(@RequestBody UserDto userDto) {
        userService.join(userDto);
       return "회원가입이 성공 했습니다." ;
    }
    @PostMapping("/login")
    public String login (@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }
}

