package Helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

public class XmlHelper {
	public static Content getErrorJson(String errorType,int errorCode){
		Content error;
		error = views.xml.error.render(errorCode,Messages.get(errorType));
		
		return error;
	}
	
	public static Result getErrorResult(int errorCode){
		Content content;
		switch (errorCode){
		case 1:
			content = getErrorJson ("eventnotfound",errorCode);
			return Results.notFound(content);
			
		case 2:
			content = getErrorJson ("pagenotfound",errorCode);
			return Results.notFound(content);
		case 3:
			content = getErrorJson ("notacceptable",errorCode);
			return Results.status(406, content);
		case 4:
			content = getErrorJson ("internalservererror",errorCode);
			return Results.status(500, content);
		case 5:
			content = getErrorJson ("badrequest",errorCode);
			return Results.status(400, content);
		case 7:
			content = getErrorJson ("eventcontainstag",errorCode);
			return Results.status(409, content);
		}
		return null;
	}
}
