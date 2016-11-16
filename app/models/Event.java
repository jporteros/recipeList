package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import play.data.validation.Constraints.Required;

@Entity
public class Event extends BaseModel{
	@Required
	private String name;
	
	private List<Date> dates;
	
	@Required
	private String description;
	
	@Required 
	private String type;
	
	@Required
	@OneToOne(cascade = CascadeType.ALL)
	@Valid
	private Organiser Organiser;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="event")
	/* TO DO ask for the Behaviour of @Valid when applied to items in a List */
	public List<Comment> eventComment;
	
	private static final Find<Long,Event> find =  new Find<Long,Event>(){};
	
	public static Event findById(Long id){
		return find.where().eq("id", id).findUnique();
	}
	
	public static List<Event> findPage(Integer page, Integer count){
		return find.setFirstRow(page*count).setMaxRows(count).findList();
	}

	/*GETTERS AND SETTERS*/
	public List<Comment> getEventComment() {
		return eventComment;
	}

	public void setEventComment(List<Comment> eventComment) {
		this.eventComment = eventComment;
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

	public Organiser getOrganiser() {
		return Organiser;
	}

	public void setOrganiser(Organiser organiser) {
		Organiser = organiser;
	}
	
}
