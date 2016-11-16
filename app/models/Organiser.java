package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Organiser extends BaseModel{
	
	@OneToOne(mappedBy="organiser")
	@JsonIgnore
	private Event event;
	
	@Required
	@MinLength(3)
	private String name;
	
	private String surname;
	
	@Required
	@Email
	private String email;
	
	
	/*GETTERS AND SETTERS*/
	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
