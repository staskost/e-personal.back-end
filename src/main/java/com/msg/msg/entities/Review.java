package com.msg.msg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "review")
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@OneToOne
	@JoinColumn(name = "fk_session_id", referencedColumnName = "idtraining_session")
	private TrainingSession session;

	@Column(name = "comment")
	private String comment;

	@Column(name = "date")
	private Date date;

	@Column(name = "rating")
	private int rating;

	public Review() {
	}

	public Review(TrainingSession session, String comment, int rating) {
		this.session = session;
		this.comment = comment;
		this.date = new Date();
		this.rating = rating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TrainingSession getSession() {
		return session;
	}

	public void setSession(TrainingSession session) {
		this.session = session;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "Review [id=" + id + ", session=" + session + ", comment=" + comment + ", date=" + date + ", rating="
				+ rating + "]";
	}

}
