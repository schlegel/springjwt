package com.github.schlegel.springjwt.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);
    User findByEmailAndCompanyId(String email, UUID companyId);
}
