package com.msg.msg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Entity
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_id")
	private int token_id;

	@Column(name = "alphanumeric")
	private String alphanumeric;

	@ManyToOne
	@JoinColumn(name = "iduser")
	private User user;

	@Column(name = "date_of_creation")
	private Date date_of_creation;

	public Token() {
	}

	public Token(String alphanumeric, User user) {
		this.alphanumeric = alphanumeric;
		this.user = user;
		this.date_of_creation = new Date();
	}

	public int getToken_id() {
		return token_id;
	}

	public void setToken_id(int token_id) {
		this.token_id = token_id;
	}

	public String getAlphanumeric() {
		return alphanumeric;
	}

	public void setAlphanumeric(String alphanumeric) {
		this.alphanumeric = alphanumeric;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getDate_of_creation() {
		return date_of_creation;
	}

	public void setDate_of_creation(Date date_of_creation) {
		this.date_of_creation = date_of_creation;
	}

	@Override
	public String toString() {
		return "Token [token_id=" + token_id + ", alphanumeric=" + alphanumeric + ", user=" + user
				+ ", date_of_creation=" + date_of_creation + "]";
	}

}
