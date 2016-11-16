package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Event;
import play.mvc.Controller;
import play.mvc.Result;

public class EventController extends Controller {

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
	}

	public Result createEvent() {
		if (request().accepts("application/json")) {
			return ok("createEventJSON");
		} else if (request().accepts("application/xml")) {
			return ok("createEventXML");
		} else {
			return ok("createEventNotAcceptable");
		}
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
