package main.exceptions;

import main.interfaces.UserI;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class PermissionDenied extends Exception {

    public PermissionDenied(String message, UserI currentUser) {
        super(currentUser.getUsername() + " " + message);
    }
}