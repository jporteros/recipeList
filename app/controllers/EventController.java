package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Event;
import play.mvc.Controller;
import play.mvc.Result;

public class EventController extends Controller{
	
	private List<Event> events= new ArrayList<>();

	public Result getEvent (Long id){
		
		return ok("getEvent");
	}
	public Result listEvents (){
			
		return ok("listEvents");
	}
	public Result removeEvent (Long id){
		
		return ok("removeEvent");
	}
	public Result createEvent (){
			
		return ok("createEvent");
	}
	public Result updateEvent (Long id){
		
		return ok("updateEvent");
	}
	
	
	
}
