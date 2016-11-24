package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;

import play.data.validation.Constraints.Required;
import play.libs.Json;

@Entity
public class Event extends BaseModel{
	public static Integer eventsPerPage = 10;
	
	@Required
	private String name;
	
	/*TODO fix the problem with dates when any find method triggers*/
	//private List<Date> dates;
	@Required
	private String description;
	
	@Required
	private String type;
	
	//@Required
	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	private Organiser organiser;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="event")
	//@JsonIgnore
	/* TODO ask for the Behaviour of @Valid when applied to items in a List */
	public List<Comment> eventComments;
	
	private static final Find<Long,Event> find =  new Find<Long,Event>(){};
	
	public static Event findById(Long id){
		return find.where().eq("id", id).findUnique();
	}
	
	public static List<Event> findPage(Integer page){
		/*First events will be shown in page 0*/
		/*TODO order by date*/
		return find.setFirstRow((page*eventsPerPage)).setMaxRows(eventsPerPage).findList();
	}

	/*GETTERS AND SETTERS*/
	public List<Comment> getEventComments() {
		return eventComments;
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
	
	/*
	public List<Date> getDates() {
		return this.dates;
	}

	public void setDates(List<Date> dates) {
		this.dates = dates;
	}
	*/
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
	public JsonNode toJson(){
		return Json.toJson(this);
	}

	
	
}
