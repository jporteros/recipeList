package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import com.avaje.ebean.annotation.JsonIgnore;

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

	public void setEvent(Event event) {
		this.event = event;
	}
}
