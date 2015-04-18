package main.exceptions;

/**
 * Mark that a login has failed due to invalid credentials
 * Created by hagai on 07/04/15.
 */
//TODO add reason? like user not found, password doesn't match etc
public class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String _username) {
            super("This user already exists in the forum:  " + _username);
        }
}
