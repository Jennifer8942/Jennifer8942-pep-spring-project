package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT c FROM Message c WHERE c.posted_by = ?1")
    List<Message> findAllMessagesByPostedBy(Integer posted_by);
    //public Message findById(Integer i);
}
