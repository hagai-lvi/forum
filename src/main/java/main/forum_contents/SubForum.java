package main.forum_contents;

import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.interfaces.*;
import org.apache.log4j.Logger;

import java.security.Policy;
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
    private SubForumPolicyI subforum_policy;


    public SubForum(String name, SubForumPolicyI subforum_policy){
        _name = name;
        this.subforum_policy = subforum_policy;
    }


    @Override
    public void creatThread(MessageI message) throws DoesNotComplyWithPolicyException {
        if (!subforum_policy.isValidMessage(message)) {
            throw new DoesNotComplyWithPolicyException();
        }
        ForumThread thread = new ForumThread(message);
        _threads.add(thread);
        _threadByMessage.put(message,thread);
    }

    @Override
    public void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException, DoesNotComplyWithPolicyException {
        if (!subforum_policy.isValidMessage(reply)){
            throw new DoesNotComplyWithPolicyException();
        }
        ThreadI _thread = _threadByMessage.get(original);
        if (_thread == null){
            logger.warn("User tried to reply to already deleted thread");
            throw new MessageNotFoundException(original, this);
        }
        original.reply(reply);
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
