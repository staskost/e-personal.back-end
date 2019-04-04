package com.msg.msg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "message")
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idmessage")
	private int id;

	@JoinColumn(name = "fk_sender_id", referencedColumnName = "iduser")
	@ManyToOne(optional = false)
	private User sender;

	@JoinColumn(name = "fk_receiver_id", referencedColumnName = "iduser")
	@ManyToOne
	private User receiver;

	@Column(name = "content")
	private String text;

	@Column(name = "time_sent")
	private Date date;

	public Message() {
	}

	public Message(User sender, User receiver, String text) {
		this.sender = sender;
		this.receiver = receiver;
		this.text = text;
		this.date = new Date();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", sender=" + sender + ", receiver=" + receiver + ", text=" + text + ", date="
				+ date + "]";
	}

}
