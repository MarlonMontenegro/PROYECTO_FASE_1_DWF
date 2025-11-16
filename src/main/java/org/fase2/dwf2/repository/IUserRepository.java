package org.fase2.dwf2.repository;

import org.fase2.dwf2.entities.User;
import org.fase2.dwf2.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDui(String dui);
    Optional<User> findByDui(String email);
    List<User> findByRole(Role role);
}
