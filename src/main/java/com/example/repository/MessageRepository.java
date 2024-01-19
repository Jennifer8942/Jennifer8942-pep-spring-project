package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

/**
 * Repository Interface for Message entities.
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * Find all messages posted by an account
     * @param posted_by the account_id of the user who posted the message
     * @return List of Messages posted by the user
     */
    @Query("SELECT c FROM Message c WHERE c.posted_by = ?1")
    List<Message> findAllMessagesByPostedBy(Integer posted_by);
}
