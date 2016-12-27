package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.avaje.ebean.Model.Find;
import com.fasterxml.jackson.annotation.JsonIgnore;

import play.data.validation.Constraints.Required;

@Entity
public class Tag extends BaseModel{
	
	@Required
	private String name;
	
	@ManyToMany(mappedBy = "eventTags")
	@JsonIgnore
	public Set<Event> events;
	
	
	private static final Find<Long, Tag> find = new Find<Long, Tag>() {
	};

	public static Tag findByName(String name) {
		return find.where().eq("name", name).findUnique();
	}
	
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
