package models;

import java.util.Set;

import javax.persistence.ManyToMany;

import com.avaje.ebean.annotation.JsonIgnore;

import play.data.validation.Constraints.Required;

public class Tag extends BaseModel{

	@Required
	private String name;
	
	@ManyToMany(mappedBy = "tags")
	@JsonIgnore
	public Set<Event> events;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}
	
}
