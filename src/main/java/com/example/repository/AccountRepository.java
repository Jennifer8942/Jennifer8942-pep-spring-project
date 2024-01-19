package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

/**
 * Repository Interface for Account entities.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Find Account by Username
     * @param username
     * @return The Account with matching username
     */
    public Account findByUsername(String username);
}
