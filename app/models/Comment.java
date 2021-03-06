package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.data.validation.Constraints.Max;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

@Entity
public class Comment extends BaseModel {

	@Required
	@MinLength(3)
	private String author;

	@Min(0)
	@Max(5)
	private Integer stars;

	@Required
	@MinLength(2)
	private String body;

	@ManyToOne
	@JsonIgnore
	private Event event;

	/* GETTERS AND SETTERS */
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getStars() {
		return stars;
	}

	public void setStars(Integer stars) {
		this.stars = stars;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	public String getDate(){
		return this.whenUpdate.toString();
	}
}
