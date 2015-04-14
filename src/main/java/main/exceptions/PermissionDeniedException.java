package main.exceptions;

import main.interfaces.UserI;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class PermissionDeniedException extends Exception {

    public PermissionDeniedException(String message, UserI currentUser) {
        super(currentUser.getUsername() + " " + message);
    }
}