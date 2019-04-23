package com.msg.msg.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.msg.msg.entities.Message;
import com.msg.msg.entities.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	Message findById(int id);

	List<Message> findBySenderOrderByDateDesc(User sender, Pageable pageable);// not used

	List<Message> findByReceiverOrderByDateDesc(User receiver, Pageable pageable);// not used

	List<Message> findByReceiverAndIsRead(User receiver, int status);

	@Query(value = "SELECT * FROM tseam_six_3.message WHERE fk_sender_id=?1 ORDER BY time_sent DESC LIMIT ?2,?3", nativeQuery = true)
	List<Message> findSentMessages(int fk_sender_id, int start, int count);

	@Query(value = "SELECT * FROM tseam_six_3.message WHERE fk_receiver_id=?1 ORDER BY time_sent DESC LIMIT ?2,?3", nativeQuery = true)
	List<Message> findInboxMessages(int fk_receiver_id, int start, int count);

	List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByDateDesc(User sender, User receiver, User receiver2,
			User sender2, Pageable pageable);// not used

	@Query(value = "SELECT * FROM tseam_six_3.message WHERE fk_receiver_id=?1 AND fk_sender_id=?2 or fk_receiver_id=?3 AND fk_sender_id=?4 ORDER BY  time_sent DESC LIMIT ?4,?5", nativeQuery = true)
	List<Message> findUserMessages(int fk_receiver_id, int fk_sender_id, int fk_sender_id1, int fk_receiver_id1,
			int index1, int index2);// not used

}
