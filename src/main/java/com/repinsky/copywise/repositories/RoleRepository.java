package com.repinsky.copywise.repositories;

import com.repinsky.copywise.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByTitle(String title);

    @Query("select r from Role r join r.users u where u.email = :userEmail")
    List<Role> findByUserEmail(String userEmail);
}
