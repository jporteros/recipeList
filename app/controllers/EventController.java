package controllers;

import java.util.List;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import Helpers.JsonHelper;
import Helpers.XmlHelper;
import models.Comment;
import models.Event;
import models.Tag;
import play.cache.CacheApi;
import play.cache.Cached;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.i18n.Lang;
import play.i18n.Messages;
import play.libs.Json;
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
		//Add cache
		Event event = cache.get("event-"+id);
		if(event == null){
			event = Event.findById(id);
			if (event == null){
				return sendError(1);
			}
			cache.set("event-"+id, event,60);
		}
		if (request().accepts("application/json")) {
			JsonNode node= cache.get("event-"+id+"-json");
			if(node==null){
				node= event.toJson();
				cache.set("event-"+id+"-json", node,60);
			}
			return ok(node);
		} else if (request().accepts("application/xml")) {
			Content content = cache.get("event-"+id+"-xml");
			if(content==null){
				content = views.xml.event.render(event);
				cache.set("event-"+id+"-xml", content,60);
			}
			return ok(content);
		} else {
			return JsonHelper.getErrorResult(3);//406
			
		}
	}
	
	public Result sendError(Integer error){
		
		if (request().accepts("application/json")) {
			return JsonHelper.getErrorResult(error);
			
		}else if (request().accepts("application/xml")) {
			return XmlHelper.getErrorResult(error);
		}else {
			return JsonHelper.getErrorResult(3);//406
		}
	}

	public Result listEvents(Integer page) {
		List<Event> events= cache.get("events+"+page);
		if(events == null){
			events = Event.findPage(page);
			if (events == null)
				return sendError(2);
			cache.set("events+"+page,events,60);//expira en 1 min
		}
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
			return JsonHelper.getErrorResult(3);
		}
	}

	public Result removeEvent(Long id) {
	
		Event event = cache.get("event-"+id);
		if(event==null){
			event = Event.findById(Long.valueOf(id));
			if (event == null) 
				return sendError(1);
		}
		
		if (event.delete()) {
			cache.remove("event-"+id);
			if (request().accepts("application/json")) {
				ObjectNode node =  play.libs.Json.newObject(); 
				node.put(Messages.get("errormessage"), Messages.get("eventremoved"));
				return ok(node);
			} else if (request().accepts("application/xml")) {
				Content content = views.xml.message.render(Messages.get("eventremoved"));
				return ok(content);
			} else {
				return JsonHelper.getErrorResult(3);
			}
		} else
			 return sendError(4);//500
	}

	public Result createEvent() {
		Form<Event> f = formFactory.form(Event.class).bindFromRequest();
		if (f.hasErrors()) {
			//TODO create different types of error for json and xml
			return sendError(5);//400
			
		}
		Event event = f.get();
		event.save();

		if (request().accepts("application/json")) {
			return Results.status(CREATED, event.toJson());
		} else if (request().accepts("application/xml")) {
			Content content = views.xml.event.render(event);
			return Results.status(CREATED, content);
		} else {
			return JsonHelper.getErrorResult(3);
		}
	}

	public Result updateEvent(Long id) {
		Form<Event> f = formFactory.form(Event.class).bindFromRequest();
		if(f.hasErrors()){
			//TODO create different types of error for json and xml
			return sendError(5);
		}
		Event event = cache.get("event-"+id);
		if(event==null){
			event = Event.findById(Long.valueOf(id));
			if (event == null) 
				return sendError(1);
		}
		event = f.get();
		event.setId(id);
		event.update();
		cache.set("event-"+id, event,60);
		if (request().accepts("application/json")) {
			JsonNode node= event.toJson();
			cache.set("event-"+id+"-json", node,60);
			
			return ok(node);
		} else if (request().accepts("application/xml")) {
			Content content = views.xml.event.render(event);
			cache.set("event-"+id+"-xml", content,60);
			return ok(content);
		} else {
			return JsonHelper.getErrorResult(3);
		}
	}
	
	
	public Result commentEvent(Long id) {
		Form<Comment> f = formFactory.form(Comment.class).bindFromRequest();
		if (f.hasErrors()) {
			//TODO create different types of error for Json and XML
			return sendError(5);
		}
		Event event = cache.get("event-"+id);
		if(event == null){
			event = Event.findById(id);
			if (event == null)
				return sendError(1);
			cache.set("event-"+id, event,60);
		}
		Comment comment=f.get();
		event.getEventComments().add(comment);
		comment.setEvent(event);
		event.save();
		cache.set("event-"+id, event,60);
		if (request().accepts("application/json")) {
			//JsonNode node= cache.get("event-"+id+"-json");
			//if(node==null){
			JsonNode node= event.toJson();
			//	cache.set("event-"+id+"-json", node,60);
			//}
			return ok(node);
		} else if (request().accepts("application/xml")) {
			//Content content = cache.get("event-"+id+"-xml");
			//if(content==null){
				Content content = views.xml.event.render(event);
				cache.set("event-"+id+"-xml", content,60);
			//}
			return ok(content);
		} else {
			return JsonHelper.getErrorResult(3);
		}
	}

	
	public Result addTagToEvent(Long id) {
		Form<Tag> f = formFactory.form(Tag.class).bindFromRequest();
		if (f.hasErrors()) {
			//TODO create different types of error for Json and XML
			return sendError(5);
		}
		Event event = cache.get("event-"+id);
		if(event == null){
			event = Event.findById(id);
			if (event == null)
				return sendError(1);
			
		}
		
		Tag tag=f.get();
		Tag aux = Tag.findByName(tag.getName());
		
		if( aux == null){
			//System.out.println("NO existe el tag en general el aux es:");
			event.getEventTags().add(tag);
			tag.getEvents().add(event);
		//	System.out.println("el tag que se mete es:"+tag);
		}
		else{
		//	System.out.println(aux);
			int variable=0;
		//	System.out.println(event.eventTags+"y la operacion"+!event.eventTags.contains(aux));
			for(Tag t:event.eventTags){
				if(t.getName()==aux.getName()){
					variable=1;
					break;
				}
			}
			if(variable!=1){
			//	System.out.println("NO ha encontrado el tag en el evento");
				event.getEventTags().add(aux);
				aux.getEvents().add(event);	
			//	System.out.println("el aux "+aux);
			}
			else{
			//	System.out.println("SI ha encontrado el tag en ele evento");
				//TODO AÑADIR FLAG
				return sendError(6);
			}
		}
		event.save();
		System.out.println(event.eventTags +"y la operacion");
		cache.set("event-"+id, event,60);
		if (request().accepts("application/json")) {
			//JsonNode node= cache.get("event-"+id+"-json");
			//if(node==null){
				JsonNode node= event.toJson();
				cache.set("event-"+id+"-json", node,60);
			//}
			return ok(node);
		} else if (request().accepts("application/xml")) {
		//	Content content = cache.get("event-"+id+"-xml");
			//if(content==null){
			Content	content = views.xml.event.render(event);
				cache.set("event-"+id+"-xml", content,60);
		//	}
			return ok(content);
		} else {
			return JsonHelper.getErrorResult(3);
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
