package models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.JsonIgnore;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.Required;

@Entity
public class Comment extends Model{
	
	@Id
	private Long id;
	
	@Required
	private String author;
	
	@Required
	private Integer stars;
	
	@Required
	private String body;
	
	@CreatedTimestamp
	Timestamp whenCreated;
	
	@UpdatedTimestamp
	Timestamp whenUpdate;
	
	@ManyToOne
	@JsonIgnore
	public Event event;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
	
	

}
