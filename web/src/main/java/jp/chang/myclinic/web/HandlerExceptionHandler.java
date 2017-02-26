package jp.chang.myclinic.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.util.HashMap;

@ControllerAdvice
public class HandlerExceptionHandler {

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HandlerException.class)
	@ResponseBody
	public Map<String, String> handle(HandlerException ex){
		Map<String, String> err = new HashMap<String, String>();
		err.put("message", ex.getMessage());
		return err;
	}

}