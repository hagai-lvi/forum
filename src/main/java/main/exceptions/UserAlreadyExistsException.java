package main.exceptions;

/**
 * Mark that a user with the specifid username already exist in the forum
 */
public class UserAlreadyExistsException extends Exception {
        public UserAlreadyExistsException(String _username) {
            super("This user already exists in the forum:  " + _username);
        }
}
