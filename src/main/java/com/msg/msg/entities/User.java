package com.msg.msg.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iduser")
	private int id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@JoinColumn(name = "fk_role_id", referencedColumnName = "id")
	@ManyToOne
	private Role role;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "price")
	private double price;

	@Column(name = "description")
	private String description;

	@Column(name = "photo_link")
	private String photoLink;

	@Column(name = "is_banned")
	private int bannedStatus;

	@Column(name = "random_num")
	private String randomNum;

	@OneToMany
	@JoinColumn(name = "fk_sender_id", referencedColumnName = "iduser")
	@JsonIgnore
	private List<Message> fromMsgs;

	@OneToMany
	@JoinColumn(name = "fk_receiver_id", referencedColumnName = "iduser")
	@JsonIgnore
	private List<Message> toMsgs;

	@OneToMany
	@JoinColumn(name = "fk_client_id", referencedColumnName = "iduser")
	@JsonIgnore
	private List<TrainingSession> clientSessions;

	@OneToMany
	@JoinColumn(name = "fk_trainer_id", referencedColumnName = "iduser")
	@JsonIgnore
	private List<TrainingSession> trainerSessions;

	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Token> tokens;

	@ManyToMany
	@JoinTable(name = "trainer_area", joinColumns = @JoinColumn(name = "fk_trainer_id"), inverseJoinColumns = @JoinColumn(name = "fk_area_id"))
	List<Area> trainerAreas;

	@ManyToMany
	@JoinTable(name = "trainer_specialization", joinColumns = @JoinColumn(name = "fk_trainer_id"), inverseJoinColumns = @JoinColumn(name = "fk_training_type"))
	List<TrainingType> trainerTypes;

	public User() {
	}

	public User(String username, String password, Role role, String firstName, String lastName, String email,
			double price, String description) {
		this.username = username;
		this.password = password;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.price = price;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String retrievePassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhotoLink() {
		return photoLink;
	}

	public void setPhotoLink(String photoLink) {
		this.photoLink = photoLink;
	}

	public int getBannedStatus() {
		return bannedStatus;
	}

	public void setBannedStatus(int activeStatus) {
		this.bannedStatus = activeStatus;
	}

	public String getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(String randomNum) {
		this.randomNum = randomNum;
	}

//	public List<Message> getFromMsgs() {
//		return fromMsgs;
//	}
//
//	public void setFromMsgs(List<Message> fromMsgs) {
//		this.fromMsgs = fromMsgs;
//	}
//
//	public List<Message> getToMsgs() {
//		return toMsgs;
//	}
//
//	public void setToMsgs(List<Message> toMsgs) {
//		this.toMsgs = toMsgs;
//	}
//
//	public List<TrainingSession> getClientSessions() {
//		return clientSessions;
//	}
//
//	public void setClientSessions(List<TrainingSession> clientSessions) {
//		this.clientSessions = clientSessions;
//	}
//
//	public List<TrainingSession> getTrainerSessions() {
//		return trainerSessions;
//	}
//
//	public void setTrainerSessions(List<TrainingSession> trainerSessions) {
//		this.trainerSessions = trainerSessions;
//	}
//
//	public List<Token> getTokens() {
//		return tokens;
//	}
//
//	public void setTokens(List<Token> tokens) {
//		this.tokens = tokens;
//	}
//

	public List<TrainingType> getTrainerTypes() {
		return trainerTypes;
	}

	public void setTrainerTypes(List<TrainingType> trainerTypes) {
		this.trainerTypes = trainerTypes;
	}

	public List<Area> getTrainerAreas() {
		return trainerAreas;
	}

	public void setTrainerAreas(List<Area> trainerAreas) {
		this.trainerAreas = trainerAreas;
	}

	public void addTrainingArea(Area area) {
		this.trainerAreas.add(area);
	}

	public void removeTrainingArea(Area area) {
		this.trainerAreas.remove(area);
	}

	public void addTrainingType(TrainingType trainingType) {
		this.trainerTypes.add(trainingType);
	}

	public void removeTrainingType(TrainingType trainingType) {
		this.trainerTypes.remove(trainingType);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", role=" + role + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", price=" + price + ", description=" + description
				+ "]";
	}

}
