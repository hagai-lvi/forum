package main.User;

import main.exceptions.*;
import main.forum_contents.SubForum;
import main.interfaces.*;
import org.apache.log4j.Logger;
import main.forum_contents.Forum;

import javax.persistence.*;
import java.util.Collection;

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
    @OneToOne(targetEntity = Forum.class)
    private ForumI forum;
    @OneToOne(targetEntity = SubForum.class)
    private SubForumI subforum;
    private static Logger logger = Logger.getLogger(UserSubforumPermission.class.getName());

    public UserSubforumPermission(Permissions permission, ForumI forum, SubForumI subforum){
        logger.info("Creating new permissions for - " + permission);
        this.forum = forum;
        this.subforum = subforum;
        this.permission = permission;
    }



    @Override
    public void createThread(MessageI message) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        if( ! permission.equals(Permissions.PERMISSIONS_GUEST)) {
            logger.info(permission + " has permission to create thread");
            subforum.createThread(message);
        } else {
            logger.error(permission + " has no permission to create thread");
            throw new PermissionDeniedException("User has no permission to create thread");
        }
    }

   @Override
    public void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException, DoesNotComplyWithPolicyException, PermissionDeniedException {
        if(!permission.equals(Permissions.PERMISSIONS_GUEST)) {
            logger.info(permission + " has permission to reply");
            subforum.replyToMessage(original, reply);
        } else {
            logger.error(permission + " has no permission to reply");
            throw new PermissionDeniedException("User has no permission to reply");
        }
    }

   @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws PermissionDeniedException, ModeratorDoesNotExistsException {
       if(!permission.equals(Permissions.PERMISSIONS_GUEST)) {
           logger.info(permission + " has permission to report moderator");
           subforum.reportModerator(moderatorUsername, reportMessage, reporter);
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
            subforum.deleteMessage(message, deleter);
        } else {
            logger.error(permission + " has no permission to delete message");
            throw new PermissionDeniedException("User has no permission delete message");
        }
    }

    @Override
    public void editMessage(MessageI originalMessage, MessageI newMessage) {
        originalMessage.editText(newMessage.getMessageText());
    }

    @Override
    public ThreadI[] getThreads() {
        return subforum.getThreads().toArray(new ThreadI[0]);
    }

    @Override
    public void setModerator(UserI moderator) throws PermissionDeniedException {
        if( permission.equals(Permissions.PERMISSIONS_ADMIN)) {
            SubForumPermissionI p = new UserSubforumPermission(Permissions.PERMISSIONS_MODERATOR, forum, subforum);
            moderator.addSubForumPermission(p);
            subforum.setModerator(moderator);
        } else {
            logger.error(permission + " has no permission to set moderator");
            throw new PermissionDeniedException("User can not set moderator");
        }
    }

    @Override
    public SubForumI getSubForum() {
        return subforum;
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

}
