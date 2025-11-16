package org.fase2.dwf2.repository;

import org.fase2.dwf2.entities.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findAllByManagerIsNull();
    boolean existsByName(String name);
    @Query("SELECT b FROM Branch b WHERE b.manager IS NULL")
    List<Branch> findUnassignedBranches();
}
