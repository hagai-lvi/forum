package main.forum_contents;

import main.exceptions.MessageNotFoundException;
import main.interfaces.MessageI;
import main.interfaces.SubForumI;
import main.interfaces.ThreadI;
import main.interfaces.UserI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by hagai on 07/04/15.
 */
public class SubForum implements SubForumI {


    private String _name;
    private List<ThreadI> _threads = new LinkedList<>();
    private HashMap<MessageI, ThreadI> _threadByMessage = new HashMap<>();

    public SubForum(String name){
        _name = name;
    }

    @Override
    public void creatThread(MessageI message) {
        ForumThread thread = new ForumThread(message);
        _threads.add(0,thread);
        _threadByMessage.put(message,thread);
    }

    @Override
    public void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException {
        if (_threadByMessage.get(original) == null){
            throw new MessageNotFoundException(original, this);
        }
        original.reply(reply);
    }

    @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) {

    }

    @Override
    public void deleteMessage(MessageI message) {

    }
}
