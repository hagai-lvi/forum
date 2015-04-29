package main.exceptions;

/**
 * Mark that a login has failed due to invalid credentials
 * Created by hagai on 07/04/15.
 */
public class InvalidUserCredentialsException extends Exception {
	public InvalidUserCredentialsException(String message){
		super(message);
	}
	public InvalidUserCredentialsException(String message, Throwable cause){
		super(message, cause);
	}
}
