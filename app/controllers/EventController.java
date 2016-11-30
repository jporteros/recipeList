package controllers;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Comment;
import models.Event;
import models.Tag;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class EventController extends Controller {
	@Inject
	FormFactory formFactory;

	public Result getEvent(Long id) {
		Event event = Event.findById(id);
		if (event == null)
			return notFound();

		if (request().accepts("application/json")) {
			return ok(event.toJson());
		} else if (request().accepts("application/xml")) {
			return ok("getEventXML");
		} else {
			return ok("getEventNotAcceptable");
		}
	}

	public Result listEvents(Integer page) {
		List<Event> events = Event.findPage(page);
		if (events == null)
			return notFound();
		if (request().accepts("application/json")) {
			return ok(EventController.createEventListNode(events));
		} else if (request().accepts("application/xml")) {
			return ok("listEventsXML");
		} else {
			return ok("listEventsNotAcceptable");
		}
	}

	public Result removeEvent(Long id) {
		Event event = Event.findById(Long.valueOf(id));
		if (event == null) {
			return Results.notFound();
		}
		if (event.delete()) {
			if (request().accepts("application/json")) {
				return ok("removeEventJSON");
			} else if (request().accepts("application/xml")) {
				return ok("removeEventXML");
			} else {
				return ok("removeEventNotAcceptable");
			}
		} else
			return Results.internalServerError();
	}

	public Result createEvent() {
		Form<Event> f = formFactory.form(Event.class).bindFromRequest();
		if (f.hasErrors()) {
			//TODO create different types of error for json and xml
			return Results.badRequest(f.errorsAsJson());
		}
		Event event = f.get();
		event.save();

		if (request().accepts("application/json")) {
			return Results.status(CREATED, event.toJson());
		} else if (request().accepts("application/xml")) {
			return ok("createEventXML");
		} else {
			return ok("createEventNotAcceptable");
		}
	}

	public Result updateEvent(Long id) {
		Form<Event> f = formFactory.form(Event.class).bindFromRequest();
		if(f.hasErrors()){
			//TODO create different types of error for json and xml
			return Results.badRequest(f.errorsAsJson());
		}
		Event event = Event.findById(Long.valueOf(id));
		if (event == null) {
			return Results.notFound();
		}
		event = f.get();
		event.setId(id);
		event.update();
		if (request().accepts("application/json")) {
			return ok(event.toJson());
		} else if (request().accepts("application/xml")) {
			return ok("updateEventXML");
		} else {
			return ok("updateEventNotAcceptable");
		}
	}
	
	@Transactional
	public Result commentEvent(Long id) {
		Form<Comment> f = formFactory.form(Comment.class).bindFromRequest();
		if (f.hasErrors()) {
			//TODO create different types of error for Json and XML
			return Results.badRequest(f.errorsAsJson());
		}
		Event event = Event.findById(id);
		if(event == null)
			return notFound();
		Comment comment=f.get();
		event.getEventComments().add(comment);
		comment.setEvent(event);
		event.save();
		
		if (request().accepts("application/json")) {
			return ok(event.toJson());
		} else if (request().accepts("application/xml")) {
			return ok("commentEventXML");
		} else {
			return ok("commentEventNotAcceptable");
		}
	}

	@Transactional
	public Result addTagToEvent(Long id) {
		Form<Tag> f = formFactory.form(Tag.class).bindFromRequest();
		if (f.hasErrors()) {
			//TODO create different types of error for Json and XML
			return Results.badRequest(f.errorsAsJson());
		}
		Event event = Event.findById(id);
		if(event == null)
			return notFound();
		Tag tag=f.get();
		Tag aux = Tag.findByName(tag.getName());
		if( aux == null){
			event.getEventTags().add(tag);
			tag.getEvents().add(event);
		}
		else{
			System.out.println("Antes contains");
			if(!event.eventTags.contains(aux)){
				event.getEventTags().add(aux);
				aux.getEvents().add(event);	
			}
			else{
				//TODO AÑADIR FLAG
				return ok("ya contiene el tag");
			}
			System.out.println("Despues contains");
		}
		event.save();
		if (request().accepts("application/json")) {
			return ok(event.toJson());
		} else if (request().accepts("application/xml")) {
			return ok("commentEventXML");
		} else {
			return ok("commentEventNotAcceptable");
		}
	}
	
	public static ObjectNode createEventListNode(List<Event> events) {
		ArrayNode array = play.libs.Json.newArray();
		ObjectNode node = play.libs.Json.newObject();
		for (Event e : events) {
			array.add(e.toJson());
		}
		node.set("events", array);
		return node;
	}

}
