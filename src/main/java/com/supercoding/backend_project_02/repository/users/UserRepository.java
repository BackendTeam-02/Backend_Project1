package com.supercoding.backend_project_02.repository.users;

import com.supercoding.backend_project_02.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Users findByEmail(String email);


    Boolean existsByemail(String email);
}
