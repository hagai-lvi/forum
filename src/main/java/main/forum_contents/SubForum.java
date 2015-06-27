package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import main.Persistancy.PersistantObject;
import main.User.User;
import main.exceptions.*;
import main.interfaces.*;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hagai on 07/04/15.
 */
@Entity
public class SubForum extends PersistantObject implements SubForumI {


    //  ============================================== Properties ====================================

    @Id@GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonView(NativeGuiController.class)
    private String _name;

    /**
     * a list of all of the threads in this subforum
     */
    @OneToMany(targetEntity = ForumThread.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<String, ThreadI> _threads;

    @Override
    public Map<String, UserI> getModerators() {
        return _moderators;
    }

    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<String, UserI> _moderators;
    private static Logger logger = Logger.getLogger(Forum.class.getName());
    @ManyToOne(targetEntity = ForumPolicy.class, cascade = CascadeType.ALL)
    private SubForumPolicyI subforumPolicy;


    // ================================================ Constructors ====================================

    public SubForum(String name, SubForumPolicyI subforumPolicy){
        _moderators = new HashMap<>();
        _threads = new HashMap<>();
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
    public int replyToMessage(MessageI original, String user, String title, String text) throws MessageNotFoundException, DoesNotComplyWithPolicyException {
        if (!subforumPolicy.isValidMessage(title, text)){
            throw new DoesNotComplyWithPolicyException("message does not comply with forum policy.");
        }
        ThreadI thread = null;
        for (ThreadI t : _threads.values()){
            if (t.contains(original)){
                thread = t;
                break;
            }
        }
        if (thread == null){
            logger.warn("User tried to reply to already deleted thread");
            throw new MessageNotFoundException(title);
        }
        MessageI m = thread.addReply(original, title, text, user);
        _threads.replace(thread.getTitle(), thread);
        Update();
        return m.getId();
    }

    @Override
    public void setModerator(UserI mod){
        _moderators.put(mod.getUsername(), mod);
        Update();

    }


    @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws ModeratorDoesNotExistsException {
        if (!_moderators.containsKey(moderatorUsername)){
            throw new ModeratorDoesNotExistsException();
        }
        //TODO - not yet implemented.
    }

    @Override
    public void deleteMessage(String th, MessageI message) throws MessageNotFoundException {
        ThreadI thread = _threads.get(th);
        if (thread == null){
            throw new MessageNotFoundException("could not find message");
        }
        String title = thread.getTitle();
        try {
            thread.remove(message);
          //  _threads.replace(thread.getTitle(), thread);
        } catch (ThreadFinalMessageDeletedException e) {
            _threads.remove(title);
        }
        Update();
    }


    @Override
    public void removeModerator(String mod) {
        _moderators.remove(mod);
        Update();
    }

    @Override
    public void editMessage(ThreadI thread, int originalMessage, String text, String title, String user) throws MessageNotFoundException, PermissionDeniedException {
        if (thread == null){
            logger.warn("User tried to reply to already deleted thread");
            throw new MessageNotFoundException(title);
        }
        MessageI original = thread.getMessages().find(originalMessage);
        if(original.getUser().equals(user)) {
            thread.editMessage(original, title, text);
            _threads.replace(thread.getTitle(), thread);
            Update();
        }
        else throw new PermissionDeniedException("Can not edit message");
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
