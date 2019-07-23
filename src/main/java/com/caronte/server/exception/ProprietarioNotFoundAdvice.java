package com.caronte.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProprietarioNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(ProprietarioNotFoundExceprion.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String proprietarioNotGoundHandler(ProprietarioNotFoundExceprion ex) {
		return ex.getMessage();
	}
}
