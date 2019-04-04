package com.msg.msg.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "training_type")
public class TrainingType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idtraining_type")
	private int id;

	@Column(name = "specialization_title")
	private String title;

	@OneToMany
	@JoinColumn(name = "fk_training_type", referencedColumnName = "idtraining_type")
	@JsonIgnore
	private List<TrainingSession> trainerSessions;
	
	@ManyToMany(mappedBy = "trainerTypes")
	List<User> trainers;

	public TrainingType() {

	}

	public TrainingType(int id, String title) {
		this.id = id;
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static void validateTrainingType(TrainingType trainingType) {
		if (trainingType == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Training Type Not Found");
		}
	}

	@Override
	public String toString() {
		return "TrainingType [id=" + id + ", title=" + title + "]";
	}

}
