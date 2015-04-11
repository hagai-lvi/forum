package main.exceptions;

import main.interfaces.MessageI;
import main.interfaces.SubForumI;
import main.interfaces.UserI;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class PermissionDenied extends Exception {

    public PermissionDenied(MessageI message, UserI currentUser) {
        super(currentUser.getUsername() + " " + message);
    }
}