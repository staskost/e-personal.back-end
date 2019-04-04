package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.msg.msg.entities.Message;
import com.msg.msg.entities.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	List<Message> findBySenderOrderByDateDesc(User sender, Pageable pageable);

	List<Message> findByReceiverOrderByDateDesc(User receiver, Pageable pageable);

	List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByDateDesc(User sender, User receiver, User receiver2, User sender2, Pageable pageable);

	Message findById(int id);
}
