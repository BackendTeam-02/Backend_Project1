package com.supercoding.backend_project_02.repository.users;

import com.supercoding.backend_project_02.entity.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    Boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
}