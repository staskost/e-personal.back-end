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
@Table(name = "area")
public class Area {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idarea")
	private int id;

	@Column(name = "city")
	private String city;

	@OneToMany
	@JoinColumn(name = "fk_area_id", referencedColumnName = "idarea")
	@JsonIgnore
	private List<TrainingSession> areaSessions;

	@ManyToMany(mappedBy = "trainerAreas")
	List<User> trainers;

	public Area() {
	}

	public Area(int id, String city) {
		this.id = id;
		this.city = city;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<TrainingSession> getAreaSessions() {
		return areaSessions;
	}

	public void setAreaSessions(List<TrainingSession> areaSessions) {
		this.areaSessions = areaSessions;
	}

//	public List<User> getTrainers() {
//		return trainers;
//	}

	public void setTrainers(List<User> trainers) {
		this.trainers = trainers;
	}

	public static void validateArea(Area area) {
		if (area == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Area Not Found");
		}
	}

//	@Override
//	public String toString() {
//		return "Area [id=" + id + ", city=" + city + ", address=" + address + ", areaSessions=" + areaSessions + "]";
//	}

}
