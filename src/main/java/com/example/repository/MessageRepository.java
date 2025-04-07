package com.example.repository;

import com.example.entity.Message;
import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    // Cystom query to fetch messages posted by a particular or specific user
    List<Message> findByPostedBy(Integer postedBy);
}
