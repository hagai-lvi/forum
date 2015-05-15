package main.exceptions;

import main.forum_contents.Forum;

import java.text.MessageFormat;

/**
 * Mark that a subforum with the specified name could not be created in the specified forum because there already
 * is a subforum with the specified name
 * Created by hagai on 07/04/15.
 */
public class ForumAlreadyExistException extends Exception {
    public ForumAlreadyExistException(String forumName) {
        super(MessageFormat.format("A forum with the name {0} already exist.",forumName));
    }
}
