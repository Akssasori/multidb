package com.lucas.multidb.oracle.repository;

import com.lucas.multidb.oracle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
