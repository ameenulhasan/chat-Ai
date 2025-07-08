package com.ameen.chat_ai.repository;

import com.ameen.chat_ai.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT DISTINCT r FROM Role r " +
            "WHERE r.roleName LIKE %:search% " +
            "AND r.isActive = true " +
            "ORDER BY r.id DESC")
    Page<Role> findByPageableRoleName(@Param("search") String search, Pageable pageable);

    @Query("SELECT r FROM Role r WHERE r.isActive = true ORDER BY r.id DESC")
    Page<Role> findByRoleIsActive(Pageable pageable);

    @Query("SELECT r FROM Role r WHERE r.id = :id AND r.isActive = true")
    Optional<Role> findByRoleIsActiveTrue(@Param("id") Long id);

    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
    Optional<Role> findByRoleName(String roleName);

    @Query("SELECT r FROM Role r WHERE r.isActive = true AND r.roleName=:user")
    Optional<Role> findByRoleUserIsActive(String user);
}
