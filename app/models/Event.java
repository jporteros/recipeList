package models;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;
import play.data.format.*;

import javax.validation.Valid;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.data.validation.Constraints.Required;
import play.libs.Json;

@Entity
public class Event extends BaseModel {
	private static Integer eventsPerPage = 10;
	private static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	@Required
	private String name;

	@Formats.DateTime(pattern = "dd-MM-yyyy HH:mm:SSS")
	public Date eventDate = new Date();

	@Required
	private String description;

	@Required
	private String type;

	@Required
	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	private Organiser organiser;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
	public List<Comment> eventComments;
	
	@ManyToMany(cascade= CascadeType.ALL)
	public List<Tag> eventTags = new ArrayList<Tag>();
	
	private static final Find<Long, Event> find = new Find<Long, Event>() {
	};

	public static Event findById(Long id) {
		return find.where().eq("id", id).findUnique();
	}

	public static List<Event> findPage(Integer page) {
		/* First events will be shown in page 0 */
		/* TODO order by date, get comments ordered*/
		return find.setFirstRow((page * eventsPerPage))
				.setMaxRows(eventsPerPage).findList();
	}

	/* GETTERS AND SETTERS */
	
	public List<Comment> getEventComments() {
		return eventComments;
	}

	public List<Tag> getEventTags() {
		return eventTags;
	}

	public void setEventTags(List<Tag> tags) {
		this.eventTags = tags;
	}

	public void setEventComments(List<Comment> eventComment) {
		this.eventComments = eventComment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Organiser getOrganiser() {
		return organiser;
	}

	public void setOrganiser(Organiser organiser) {
		this.organiser = organiser;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	
	/*JSON Utility*/
	public JsonNode toJson() {
		ObjectNode node = (ObjectNode) Json.toJson(this);
		node.put("eventDate", df.format(eventDate));
		return node;
	}

}
