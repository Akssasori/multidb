package com.lucas.multidb.oracle.repository;

import com.lucas.multidb.oracle.model.UserLegacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLegacyRepository extends JpaRepository<UserLegacy, Long> {
}
