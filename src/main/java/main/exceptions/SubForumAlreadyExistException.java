package main.exceptions;

import main.forum_contents.Forum;

import java.text.MessageFormat;

/**
 * Mark that a subforum with the specified name could not be created in the specified forum because there already
 * is a subforum with the specified name
 * Created by hagai on 07/04/15.
 */
public class SubForumAlreadyExistException extends Exception {
    public SubForumAlreadyExistException(String subForumName, Forum forum) {
        super(MessageFormat.format("A subforum with the name {0} already exist in Forum {1}.",subForumName,forum));
    }
}
