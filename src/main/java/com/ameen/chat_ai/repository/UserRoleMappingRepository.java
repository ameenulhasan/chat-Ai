package com.ameen.chat_ai.repository;

import com.ameen.chat_ai.model.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMappingRepository extends JpaRepository<UserRoleMapping,Long> {

    @Query("SELECT ur FROM UserRoleMapping ur WHERE ur.user.id = :userId")
    List<UserRoleMapping> findByUser(@Param("userId") Long userId);

}
