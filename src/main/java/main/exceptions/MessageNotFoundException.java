package main.exceptions;

/**
 * Point that the required message could not be found in the specified subforum
 *
 * Created by hagai on 07/04/15.
 */
public class MessageNotFoundException extends Exception {

    public MessageNotFoundException(int message) {
        super("Could not find message " + message);

    }
}
