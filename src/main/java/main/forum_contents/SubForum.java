package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.Persistancy.PersistantObject;
import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ModeratorDoesNotExistsException;
import main.interfaces.*;
import main.User.User;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.*;

/**
 * Created by hagai on 07/04/15.
 */
@Entity
public class SubForum extends PersistantObject implements SubForumI {


    //  ============================================== Properties ====================================

    @JsonView(NativeGuiController.class)
    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String _name;

    /**
     * a list of all of the threads in this subforum
     */
    @OneToMany(targetEntity = ForumThread.class, cascade = CascadeType.ALL)
    private List<ThreadI> _threads = new LinkedList<>();

    @Override
    public Map<String, UserI> getModerators() {
        return _moderators;
    }

    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL)
    private Map<String, UserI> _moderators = new HashMap<>();
    private static Logger logger = Logger.getLogger(Forum.class.getName());
    @OneToOne(targetEntity = ForumPolicy.class, cascade = CascadeType.ALL)
    private SubForumPolicyI subforumPolicy;


    // ================================================ Constructors ====================================

    public SubForum(String name, SubForumPolicyI subforumPolicy){
        _name = name;
        this.subforumPolicy = subforumPolicy;
    }

    public SubForum() {   // this is needed for hibernate

    }


    // ================================================ Methods   =========================================

    @Override
    public ThreadI addThread(MessageI message) throws DoesNotComplyWithPolicyException {
        if (!subforumPolicy.isValidMessage(message)) {
            throw new DoesNotComplyWithPolicyException("message does not comply with forum policy.");
        }
        ThreadI thread = new ForumThread(message);
        _threads.add(thread);
//        this.Update(); //TODO - need to handle those update methods.
        return thread;
    }

    @Override
    public void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException, DoesNotComplyWithPolicyException {
        if (!subforumPolicy.isValidMessage(reply)){
            throw new DoesNotComplyWithPolicyException("message does not comply with forum policy.");
        }
        ThreadI thread = findThread(original);
        if (thread == null){
            logger.warn("User tried to reply to already deleted thread");
            throw new MessageNotFoundException(original.getId());
        }
        thread.addReply(reply, original);
//        this.Update();
    }

    @Override
    public void setModerator(UserI mod){
        _moderators.put(mod.getUsername(), mod);
//        this.Update();
    }


    @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws ModeratorDoesNotExistsException {
        if (!_moderators.containsKey(moderatorUsername)){
            throw new ModeratorDoesNotExistsException();
        }
    }

    @Override
    public void deleteMessage(MessageI message, String requestingUser) throws MessageNotFoundException {
        ThreadI thread = findThread(message);
        if (thread != null){
            if (message.equals(thread.getRootMessage())){
                //need to remove this thread from the subforum
                _threads.remove(thread);
            }
            thread.remove(message);
        }
        else {
            throw new MessageNotFoundException(message.getId());
        }
        //this.Update();
    }


    @Override
    public void removeModerator(String mod) {
        // TODO - Tom update database
        _moderators.remove(mod);
        //this.Update();
    }

    @Override
    public void editMessage(MessageI originalMessage, MessageI newMessage) throws MessageNotFoundException {
        for (ThreadI thread : _threads){
            if (thread.contains(originalMessage)) {
                thread.editMessage(originalMessage, newMessage);
                break;
            }
        }
        throw new MessageNotFoundException(originalMessage.getId());
    }


    // ============================================ GETTERS ================================================

    @Override
    public String getTitle() {
        return _name;
    }

    @Override
    public Collection<ThreadI> getThreads(){
        return _threads;
    }

    /**
     * Find the thread that contains the specified message
     */
    private ThreadI findThread(MessageI message){
        for (ThreadI t: _threads){
            if (t.contains(message)){
                return t;
            }
        }
        return null;
    }


    public static SubForum load(String sub_forum_name){
        return (SubForum)pers.load(SubForum.class, sub_forum_name);
    }


}
