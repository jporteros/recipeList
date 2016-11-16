package models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;

import play.data.validation.Constraints.Required;

@Entity
public class Event extends Model{
	@Id
	private Long id;
	
	@Required
	private String name;
	
	private List<Date> dates;
	
	@Required
	private String description;
	
	@Required 
	private String type;
	
	@Required
	private String author;
	
	@CreatedTimestamp
	Timestamp whenCreated;
	
	@UpdatedTimestamp
	Timestamp whenUpdate;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="event")
	//@JsonIgnore
	public List<Comment> eventComment;
	
	

	public List<Comment> getEventComment() {
		return eventComment;
	}

	public void setEventComment(List<Comment> eventComment) {
		this.eventComment = eventComment;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Date> getDates() {
		return dates;
	}

	public void setDates(List<Date> dates) {
		this.dates = dates;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	
}
