package main.User;

import main.exceptions.DoesNotComplyWithPolicyException;
import main.exceptions.MessageNotFoundException;
import main.exceptions.ModeratorDoesNotExistsException;
import main.exceptions.PermissionDeniedException;
import main.forum_contents.Forum;
import main.interfaces.*;
import org.apache.log4j.Logger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by gabigiladov on 4/11/15.
 */
@Entity
    public class UserSubforumPermission implements SubForumPermissionI {

    public UserSubforumPermission() {
    }

    @Override
    public Permissions getPermission() {
        return permission;
    }

    private Permissions permission;
   // @OneToOne(targetEntity = String.class)
    private String forum;
   // @OneToOne(targetEntity = SubForum.class, cascade = CascadeType.ALL)
    private String subforum;
    private static Logger logger = Logger.getLogger(UserSubforumPermission.class.getName());

    public UserSubforumPermission(Permissions permission, String forum, String subforum){
        logger.info("Creating new permissions for - " + permission);
        this.forum = forum;
        this.subforum = subforum;
        this.permission = permission;
    }



    @Override
    public ThreadI createThread(String user, String title, String text) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        if( ! permission.equals(Permissions.PERMISSIONS_GUEST)) {
            logger.info(permission + " has permission to create thread");
            ForumI f =  Forum.load(forum);
                ThreadI thread = f.getSubForums().get(subforum).addThread(user, title, text);
                return thread;

        } else {
            logger.error(permission + " has no permission to create thread");
            throw new PermissionDeniedException("User has no permission to create thread");
        }
    }

   @Override
    public void replyToMessage(MessageI original, String user, String title, String text) throws MessageNotFoundException, DoesNotComplyWithPolicyException, PermissionDeniedException {
        if(!permission.equals(Permissions.PERMISSIONS_GUEST)) {
            logger.info(permission + " has permission to reply");
            ForumI f =  Forum.load(forum);
            f.getSubForums().get(subforum).replyToMessage(original, user, title, text);
            //f.Update();
        } else {
            logger.error(permission + " has no permission to reply");
            throw new PermissionDeniedException("User has no permission to reply");
        }
    }

   @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws PermissionDeniedException, ModeratorDoesNotExistsException {
       if(!permission.equals(Permissions.PERMISSIONS_GUEST)) {
           logger.info(permission + " has permission to report moderator");
           ForumI f =  Forum.load(forum);
           f.getSubForums().get(subforum).reportModerator(moderatorUsername, reportMessage, reporter);
          // f.Update();
       } else {
           logger.error(permission + " has no permission to reply");
           throw new PermissionDeniedException("User has no permission to reply");
       }
    }

    /**
     * Delete a specific message if the message was create by the user that sent this request
     */
    @Override
    public void deleteMessage(MessageI message, String deleter) throws PermissionDeniedException, MessageNotFoundException {
        if(canDeleteMessage(message, deleter)) {
            logger.info(permission + " has permission to delete message");
            ForumI f =  Forum.load(forum);
            f.getSubForums().get(subforum).deleteMessage(message, deleter);
           // f.Update();
        } else {
            logger.error(permission + " has no permission to delete message");
            throw new PermissionDeniedException("User has no permission delete message");
        }
    }

    @Override
    public void editMessage(ThreadI thread, int originalMessage, String title, String text) throws MessageNotFoundException {
        ForumI f =  Forum.load(forum);
        f.getSubForums().get(subforum).editMessage(originalMessage, title, text);
       // f.Update();
    }

    @Override
    public ThreadI[] getThreads() {
        ForumI f =  Forum.load(forum);
        return f.getSubForums().get(subforum).getThreads().values().toArray(new ThreadI[0]);
    }

    @Override
    public void setModerator(UserI moderator) throws PermissionDeniedException {
            permission = Permissions.PERMISSIONS_MODERATOR;
            SubForumPermissionI p = new UserSubforumPermission(permission, forum, subforum);
            moderator.addSubForumPermission(subforum, p);
            ForumI f =  Forum.load(forum);
            SubForumI sf = f.getSubForums().get(subforum);
            sf.setModerator(moderator);
            f.getSubForums().replace(sf.getTitle(), sf);
            //f.Update();
    }

    @Override
    public SubForumI getSubForum() {
        ForumI f =  Forum.load(forum);
        return f.getSubForums().get(subforum);
    }

    @Override
    public boolean subForumExists(String name) {
        //TODO what is this method for?
        return false;
    }

    @Override
    public void setPermission(Permissions permission) {
        this.permission = permission;
    }

    private boolean canDeleteMessage(MessageI message, String deleter) {
        return message.getUser().equals(deleter);
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isModerator() {
        return permission.equals(Permissions.PERMISSIONS_MODERATOR);
    }

    @Override
    public boolean canReply() throws PermissionDeniedException {
        if (!permission.equals(Permissions.PERMISSIONS_GUEST)){
            return true;
        }
        else {
            throw new PermissionDeniedException("user cannot reply to a message.");
        }
    }

    @Override
    public boolean canAddThread() throws PermissionDeniedException {
        if(!permission.equals(Permissions.PERMISSIONS_GUEST)){
            return true;
        }
        else {
            throw new PermissionDeniedException("user cannot start a thread.");
        }
    }

    @Override
    public boolean canDeleteMessage() throws PermissionDeniedException {
        if (!permission.equals(Permissions.PERMISSIONS_GUEST) && !permission.equals(Permissions.PERMISSIONS_USER)){
            return true;
        } else {
            throw new PermissionDeniedException("user cannot delete this message.");
        }
    }

    @Override
    public void removeModerator(String moderatorName) {
        ForumI f = Forum.load(forum);
        f.getSubForums().get(subforum).removeModerator(moderatorName);
        //f.Update();
    }
}
