package main.exceptions;

import main.interfaces.MessageI;
import main.interfaces.SubForumI;

/**
 * Point that the required message could not be found in the specified subforum
 *
 * Created by hagai on 07/04/15.
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException(String username) {
        super("Could not find user " + username);

    }
}
