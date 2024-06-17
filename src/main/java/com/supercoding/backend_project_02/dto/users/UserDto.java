package com.supercoding.backend_project_02.dto.users;


import lombok.*;


@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private String email;
    private String password;
}