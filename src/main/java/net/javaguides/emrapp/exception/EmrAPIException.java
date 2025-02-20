package net.javaguides.emrapp.exception;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmrAPIException extends RuntimeException{
	private HttpStatus status;
	private String message;

}
