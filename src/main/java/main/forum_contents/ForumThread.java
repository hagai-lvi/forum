package main.forum_contents;

import main.interfaces.MessageI;
import main.interfaces.ThreadI;

/**
 * Created by hagai on 07/04/15.
 */
public class ForumThread implements ThreadI{
    //TODO should a thread have a subject?
    MessageI _rootMessage;

    public ForumThread(MessageI root){
        _rootMessage = root;
    }

    @Override
    public MessageI getRootMessage() {
        return _rootMessage;
    }
}
