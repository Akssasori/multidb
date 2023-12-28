package com.lucas.multidb.postgres.repository;

import com.lucas.multidb.postgres.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
