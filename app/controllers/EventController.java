package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import models.Event;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

public class EventController extends Controller {
	@Inject
	FormFactory formFactory;
	private List<Event> events = new ArrayList<>();

	public Result getEvent(Long id) {
		if (request().accepts("application/json")) {
			return ok("getEventJSON");
		} else if (request().accepts("application/xml")) {
			return ok("getEventXML");
		} else {
			return ok("getEventNotAcceptable");
		}
	}

	public Result listEvents() {
		if (request().accepts("application/json")) {
			return ok("listEventsJSON");
		} else if (request().accepts("application/xml")) {
			return ok("listEventsXML");
		} else {
			return ok("listEventsNotAcceptable");
		}
	}

	public Result removeEvent(Long id) {
		if (request().accepts("application/json")) {
			return ok("removeEventJSON");
		} else if (request().accepts("application/xml")) {
			return ok("removeEventXML");
		} else {
			return ok("removeEventNotAcceptable");
		}
		
		 
	/*	if(id>= events.size()){
			 System.out.println("id incorrecto");
			 return Results.notFound();
		 }
		 
		 Event event= event.findById(id);
		 if(event== null){
			 return notFound();//retorna 404
		 }
		 if(event.delete()){
			 cache.remove("event-"+id);
			 return ok();
		 }
		 else{
			 return Results.internalServerError();
		 }
	*/
	}

	public Result createEvent() {
		/*if (request().accepts("application/json")) {
			return ok("createEventJSON");
		} else if (request().accepts("application/xml")) {
			return ok("createEventXML");
		} else {
			return ok("createEventNotAcceptable");
		}*/
		 Form<Event> f = formFactory.form(Event.class).bindFromRequest();
		 
		 if(f.hasErrors()){
			 //f.errorsAsJson
			 return Results.badRequest(f.errorsAsJson());
		 }
		 Event event= f.get();
		 event.save();
		
		 return Results.status(CREATED, event.toJson());
	}

	public Result updateEvent(Long id) {
		if (request().accepts("application/json")) {
			return ok("updateEventJSON");
		} else if (request().accepts("application/xml")) {
			return ok("updateEventXML");
		} else {
			return ok("updateEventNotAcceptable");
		}
	}
	
	public Result commentEvent(Long id) {
		if (request().accepts("application/json")) {
			return ok("commentEventJSON");
		} else if (request().accepts("application/xml")) {
			return ok("commentEventXML");
		} else {
			return ok("commentEventNotAcceptable");
		}
	}

}
