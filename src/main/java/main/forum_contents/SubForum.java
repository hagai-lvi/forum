package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.Persistancy.PersistantObject;
import main.User.User;
import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ModeratorDoesNotExistsException;
import main.interfaces.*;
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
    @OneToMany(targetEntity = ForumThread.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<String, ThreadI> _threads = new HashMap<>();

    @Override
    public Map<String, UserI> getModerators() {
        return _moderators;
    }

    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<String, UserI> _moderators = new HashMap<>();
    private static Logger logger = Logger.getLogger(Forum.class.getName());
    @ManyToOne(targetEntity = ForumPolicy.class, cascade = CascadeType.ALL)
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
    public ThreadI addThread(String user, String title, String text) throws DoesNotComplyWithPolicyException {
        if (!subforumPolicy.isValidMessage(title, text)) {
            throw new DoesNotComplyWithPolicyException("message does not comply with forum policy.");
        }
        ThreadI thread = new ForumThread(user, title, text);
        _threads.put(title, thread);
        Update();
        return thread;
    }

    @Override
    public void replyToMessage(MessageI original, String user, String title, String text) throws MessageNotFoundException, DoesNotComplyWithPolicyException {
        if (!subforumPolicy.isValidMessage(title, text)){
            throw new DoesNotComplyWithPolicyException("message does not comply with forum policy.");
        }
        ThreadI thread = _threads.get(original.getMessageTitle());
        if (thread == null){
            logger.warn("User tried to reply to already deleted thread");
            throw new MessageNotFoundException(title);
        }
        thread.addReply(original, title, text, user);
//        this.Update();
    }

    @Override
    public void setModerator(UserI mod){
        _moderators.put(mod.getUsername(), mod);

    }


    @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws ModeratorDoesNotExistsException {
        if (!_moderators.containsKey(moderatorUsername)){
            throw new ModeratorDoesNotExistsException();
        }
    }

    @Override
    public void deleteMessage(MessageI message, String requestingUser) throws MessageNotFoundException {
        ThreadI thread = _threads.get(message.getMessageTitle());
        if (thread != null){
            if (message.equals(thread.getRootMessage())){
                //need to remove this thread from the subforum
                _threads.remove(thread.getTitle());
            }
            thread.remove(message);
        }
        else {
            throw new MessageNotFoundException(message.getMessageTitle());
        }
        //this.Update();
    }


    @Override
    public void removeModerator(String mod) {
        _moderators.remove(mod);
        //this.Update();
    }

    @Override
    public void editMessage(ThreadI thread, int originalMessage, String title, String text) throws MessageNotFoundException {
        MessageI ms =  thread.getMessages().find(originalMessage);
        ms.editTitle(title);
        ms.editText(text);
    }


    // ============================================ GETTERS ================================================

    @Override
    public String getTitle() {
        return _name;
    }

    @Override
    public Map<String, ThreadI> getThreads(){
        return _threads;
    }


    public int getMessagesCount(){
        int sum = 0;
        for (ThreadI t: _threads.values()){
            sum+= t.getMessagesCount();
        }
        return sum;
    }


    public static SubForum load(String sub_forum_name){
        return (SubForum)pers.load(SubForum.class, sub_forum_name);
    }


}
