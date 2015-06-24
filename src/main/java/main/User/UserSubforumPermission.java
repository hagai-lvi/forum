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
    public void createThread(MessageI message) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        if( ! permission.equals(Permissions.PERMISSIONS_GUEST)) {
            logger.info(permission + " has permission to create thread");
            Forum f =  Forum.load(forum);
            f.getSubForums().get(subforum).addThread(message);
           // f.Update();
        } else {
            logger.error(permission + " has no permission to create thread");
            throw new PermissionDeniedException("User has no permission to create thread");
        }
    }

   @Override
    public void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException, DoesNotComplyWithPolicyException, PermissionDeniedException {
        if(!permission.equals(Permissions.PERMISSIONS_GUEST)) {
            logger.info(permission + " has permission to reply");
            Forum f =  Forum.load(forum);
            f.getSubForums().get(subforum).replyToMessage(original, reply);
            f.Update();
        } else {
            logger.error(permission + " has no permission to reply");
            throw new PermissionDeniedException("User has no permission to reply");
        }
    }

   @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws PermissionDeniedException, ModeratorDoesNotExistsException {
       if(!permission.equals(Permissions.PERMISSIONS_GUEST)) {
           logger.info(permission + " has permission to report moderator");
           Forum f =  Forum.load(forum);
           f.getSubForums().get(subforum).reportModerator(moderatorUsername, reportMessage, reporter);
           f.Update();
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
            Forum f =  Forum.load(forum);
            f.getSubForums().get(subforum).deleteMessage(message, deleter);
            f.Update();
        } else {
            logger.error(permission + " has no permission to delete message");
            throw new PermissionDeniedException("User has no permission delete message");
        }
    }

    @Override
    public void editMessage(ThreadI thread, int originalMessage, String title, String text) throws MessageNotFoundException {
        Forum f =  Forum.load(forum);
        f.getSubForums().get(subforum).editMessage(thread, originalMessage, title, text);
        f.Update();
    }

    @Override
    public ThreadI[] getThreads() {
        Forum f =  Forum.load(forum);
        return f.getSubForums().get(subforum).getThreads().values().toArray(new ThreadI[0]);
    }

    @Override
    public void setModerator(UserI moderator) throws PermissionDeniedException {
        if(isAdmin()) {
            SubForumPermissionI p = new UserSubforumPermission(Permissions.PERMISSIONS_MODERATOR, forum, subforum);
            moderator.addSubForumPermission(subforum, p);
            Forum f =  Forum.load(forum);
            f.getSubForums().get(subforum).setModerator(moderator);
            f.Update();
        } else {
            logger.error(permission + " has no permission to set moderator");
            throw new PermissionDeniedException("User can not set moderator");
        }
    }

    @Override
    public SubForumI getSubForum() {
        Forum f =  Forum.load(forum);
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
        Forum f = Forum.load(forum);
        f.getSubForums().get(subforum).removeModerator(moderatorName);
        f.Update();
    }
    @Override
    public boolean isAdmin() {
        return this.permission.compareTo(Permissions.PERMISSIONS_ADMIN) >= 0;
    }
}
