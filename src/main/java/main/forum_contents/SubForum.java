package main.forum_contents;

import main.exceptions.MessageNotFoundException;
import main.interfaces.MessageI;
import main.interfaces.SubForumI;
import main.interfaces.ThreadI;
import main.interfaces.UserI;
import org.apache.log4j.Logger;

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
    private HashMap<String, UserI> _moderators = new HashMap<String, UserI>();
    private static Logger logger = Logger.getLogger(Forum.class.getName());



    public SubForum(String name){
        _name = name;
    }


    @Override
    public void creatThread(MessageI message) {
        ForumThread thread = new ForumThread(message);
        _threads.add(thread);
        _threadByMessage.put(message,thread);
    }

    @Override
    public void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException {
        ThreadI _thread = _threadByMessage.get(original);
        if (_thread == null){
            logger.warn("User tried to delete already deleted message");
            throw new MessageNotFoundException(original, this);
        }
        original.reply(reply);
        _threadByMessage.put(reply, _thread);
    }

    @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) {

    }

    @Override
    public void deleteMessage(MessageI message, UserI requesting_user) {
        if (message.getUser() == requesting_user){
            message.removeMessage();
            if (_threadByMessage.containsKey(message)){
                _threadByMessage.remove(message);
            }
        }
    }

    public String get_name(){
        return this._name;
    }
}
