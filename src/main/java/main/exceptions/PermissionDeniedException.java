package main.exceptions;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class PermissionDeniedException extends Exception {

    public PermissionDeniedException(String message) {
        super(message);
    }
}