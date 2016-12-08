package Helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Results;

public class JsonHelper {
	
	public static JsonNode getErrorJson(String errorType,int errorCode){
		
		ObjectNode error = play.libs.Json.newObject(); 
		error.put(Messages.get("errortype"), errorCode);
		error.put(Messages.get("errormessage"), Messages.get(errorType));
		return error;
	}

	public static Result getErrorResult(int errorCode){
		JsonNode node;
		switch (errorCode){
			case 1:
				node = getErrorJson ("eventnotfound",errorCode);
				return Results.notFound(node);
			case 2:
				node = getErrorJson ("pagenotfound",errorCode);
				return Results.notFound( node);
			case 3:
				node = getErrorJson ("notacceptable",errorCode);
				return Results.status(406, node);
			case 4:
				node = getErrorJson ("internalservererror",errorCode);
				return Results.status(500, node);
			case 5:
				node = getErrorJson ("badrequest",errorCode);
				return Results.status(400, node);
			case 6:
				node = getErrorJson ("eventcontainstag",errorCode);
				return Results.status(409, node);
		
		}
		return null;
	}
}
