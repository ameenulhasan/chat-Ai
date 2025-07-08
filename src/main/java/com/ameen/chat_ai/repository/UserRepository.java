package com.ameen.chat_ai.repository;

import com.ameen.chat_ai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.emailId = :mail")
    User findByMail(@Param("mail") String mail);

    @Query("SELECT u FROM User u WHERE u.emailId = :emailId AND u.isActive = true")
    Optional<User> findByEmailId(@Param("emailId") String emailId);

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.isActive = true")
    Optional<User> findByIdIsActive(Long id);

}
