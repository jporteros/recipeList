package controllers;

import java.util.ArrayList;
import java.util.List;

import play.mvc.Controller;
import play.mvc.Result;

public class EventController extends Controller{
	
	private List<String> events= new ArrayList<>();

	public Result getEvent (Integer id){
		
		return ok("getEvent");
	}
	public Result listEvents (){
			
		return ok("listEvents");
	}
	public Result removeEvent (Integer id){
		
		return ok("removeEvent");
	}
	public Result createEvent (){
			
			return ok("createEvent");
	}
	public Result updateEvent (Integer id){
		
		return ok("updateEvent");
	}
	
	
	
}
