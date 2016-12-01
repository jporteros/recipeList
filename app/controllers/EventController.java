package controllers;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Comment;
import models.Event;
import models.Tag;
import play.cache.CacheApi;
import play.cache.Cached;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

public class EventController extends Controller {
	@Inject
	FormFactory formFactory;
	@Inject
	private CacheApi cache;
	
	public Result getEvent(Long id) {
		
		//añadimos la cache
		Event event = cache.get("event-"+id);
		if(event == null){
			event = Event.findById(id);
			if (event == null)
				return notFound();
			cache.set("event-"+id, event);
		}
		if (request().accepts("application/json")) {
			JsonNode node= cache.get("event-"+id+"-json");
			if(node==null){
				node= event.toJson();
				cache.set("event-"+id+"-json", node);
			}
			return ok(node);
		} else if (request().accepts("application/xml")) {
			Content content = cache.get("event-"+id+"-xml");
			if(content==null){
				content = views.xml.event.render(event);
				cache.set("event-"+id+"-xml", content);
			}
			return ok(content);
		} else {
			return ok("getEventNotAcceptable");
		}
	}

	public Result listEvents(Integer page) {
		List<Event> events= cache.get("events+"+page);
		if(events == null){
			events = Event.findPage(page);
			if (events == null)
				return notFound();
			cache.set("events+"+page,events,60);//expira en 1 min
		}
		if (events == null)
			return notFound();
		if (request().accepts("application/json")) {
			ObjectNode node = cache.get("events-"+page+"-json");
			if(node==null){
				node= EventController.createEventListNode(events);
				cache.set("events-"+page+"-json",node,60);
			}
			return ok(node);
		} else if (request().accepts("application/xml")) {
			Content content = cache.get("events-"+page+"-xml");
			if(content==null){
				content = views.xml.events.render(events);
				cache.set("events-"+page+"-xml", content,60);
			}
			return ok(content);
		} else {
			return ok("listEventsNotAcceptable");
		}
	}

	public Result removeEvent(Long id) {
	
		Event event = cache.get("event-"+id);
		if(event==null){
			event = Event.findById(Long.valueOf(id));
			if (event == null) 
				return Results.notFound();
		}
		
		if (event.delete()) {
			cache.remove("event-"+id);
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
			Content content = views.xml.event.render(event);
			return ok(content);
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
		Event event = cache.get("event-"+id);
		if(event==null){
			event = Event.findById(Long.valueOf(id));
			if (event == null) 
				return Results.notFound();
		}
		event = f.get();
		event.setId(id);
		event.update();
		cache.set("event-"+id, event);
		if (request().accepts("application/json")) {
			JsonNode node= event.toJson();
			cache.set("event-"+id+"-json", node);
			
			return ok(node);
		} else if (request().accepts("application/xml")) {
			Content content = views.xml.event.render(event);
			cache.set("event-"+id+"-xml", content);
			return ok(content);
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
		Event event = cache.get("event-"+id);
		if(event == null){
			event = Event.findById(id);
			if (event == null)
				return notFound();
			cache.set("event-"+id, event);
		}
		Comment comment=f.get();
		event.getEventComments().add(comment);
		comment.setEvent(event);
		event.save();
		cache.set("event-"+id, event);
		if (request().accepts("application/json")) {
			JsonNode node= cache.get("event-"+id+"-json");
			if(node==null){
				node= event.toJson();
				cache.set("event-"+id+"-json", node);
			}
			return ok(node);
		} else if (request().accepts("application/xml")) {
			Content content = cache.get("event-"+id+"-xml");
			if(content==null){
				content = views.xml.event.render(event);
				cache.set("event-"+id+"-xml", content);
			}
			return ok(content);
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
		Event event = cache.get("event-"+id);
		if(event == null){
			event = Event.findById(id);
			if (event == null)
				return notFound();
			
		}
		Tag tag=f.get();
		Tag aux = Tag.findByName(tag.getName());
		if( aux == null){
			event.getEventTags().add(tag);
			tag.getEvents().add(event);
		}
		else{
			if(!event.eventTags.contains(aux)){
				event.getEventTags().add(aux);
				aux.getEvents().add(event);	
			}
			else{
				//TODO AÑADIR FLAG
				return ok("ya contiene el tag");
			}
		}
		event.save();
		cache.set("event-"+id, event);
		if (request().accepts("application/json")) {
			JsonNode node= cache.get("event-"+id+"-json");
			if(node==null){
				node= event.toJson();
				cache.set("event-"+id+"-json", node);
			}
			return ok(node);
		} else if (request().accepts("application/xml")) {
			Content content = cache.get("event-"+id+"-xml");
			if(content==null){
				content = views.xml.event.render(event);
				cache.set("event-"+id+"-xml", content);
			}
			return ok(content);
		} else {
			return ok("addTagNotAcceptable");
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
