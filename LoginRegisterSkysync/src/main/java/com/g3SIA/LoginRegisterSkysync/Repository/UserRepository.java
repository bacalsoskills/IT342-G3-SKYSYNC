package com.g3SIA.LoginRegisterSkysync.Repository;

import com.g3SIA.LoginRegisterSkysync.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByPasswordResetToken(String token);
}
