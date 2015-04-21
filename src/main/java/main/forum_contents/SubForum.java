package main.forum_contents;

import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ModeratorDoesNotExistsException;
import main.interfaces.*;
import org.apache.log4j.Logger;
import javax.persistence.*;
import java.util.Collection;
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
    private SubForumPolicyI subforumPolicy;


    public SubForum(String name, SubForumPolicyI subforumPolicy){
        _name = name;
        this.subforumPolicy = subforumPolicy;
    }


    @Override
    public void createThread(MessageI message) throws DoesNotComplyWithPolicyException {
        if (!subforumPolicy.isValidMessage(message)) {
            throw new DoesNotComplyWithPolicyException();
        }
        ForumThread thread = new ForumThread(message);
        _threads.add(thread);
        _threadByMessage.put(message,thread);
    }

    @Override
    public void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException, DoesNotComplyWithPolicyException {
        if (!subforumPolicy.isValidMessage(reply)){
            throw new DoesNotComplyWithPolicyException();
        }
        ThreadI _thread = _threadByMessage.get(original);
        if (_thread == null){
            logger.warn("User tried to reply to already deleted thread");
            throw new MessageNotFoundException(original, this);
        }
        original.reply(reply);
    }

    public void setModerator(UserI mod){
        _moderators.put(mod.getUsername(), mod);
    }


    @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws ModeratorDoesNotExistsException {
        if (!_moderators.containsKey(moderatorUsername)){
            throw new ModeratorDoesNotExistsException();
        }
        // TODO: send to in-charge forum admin
    }

    @Override
    public void deleteMessage(MessageI message, UserI requestingUser) {
        if (message.getUser() == requestingUser){
            message.removeMessage();
            if (_threadByMessage.containsKey(message)){
                _threadByMessage.remove(message);
            }
        }
    }

    @Override
    public String getName(){
        return this._name;
    }

    @Override
    public Collection<ThreadI> getThreads(){
        return _threads;
    }
}
