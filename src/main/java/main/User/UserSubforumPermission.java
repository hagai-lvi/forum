package main.User;

import main.exceptions.*;
import main.forum_contents.SubForum;
import main.interfaces.*;
import org.apache.log4j.Logger;
import main.forum_contents.Forum;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by gabigiladov on 4/11/15.
 */
@Entity
    public class UserSubforumPermission implements SubForumPermissionI {

    public enum PERMISSIONS{
        PERMISSIONS_GUEST,
        PERMISSIONS_USER,
        PERMISSIONS_ADMIN,
        PERMISSIONS_SUPERADMIN;
    }

    private PERMISSIONS permission;
    @OneToOne(targetEntity = Forum.class)
    private ForumI forum;
    @OneToOne(targetEntity = SubForum.class)
    private SubForumI subforum;
    private static Logger logger = Logger.getLogger(UserSubforumPermission.class.getName());

    public UserSubforumPermission(PERMISSIONS permission, ForumI forum, SubForumI subforum){
        logger.info("Creating new permissions for - " + permission);
        this.forum = forum;
        this.subforum = subforum;
        this.permission = permission;
    }



    @Override
    public void createThread(MessageI message) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        if( ! permission.equals(PERMISSIONS.PERMISSIONS_GUEST)) {
            logger.info(permission + " has permission to create thread");
            subforum.createThread(message);
        } else {
            logger.error(permission + " has no permission to create thread");
            throw new PermissionDeniedException("User has no permission to create thread");
        }
    }

   @Override
    public void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException, DoesNotComplyWithPolicyException, PermissionDeniedException {
        if(!permission.equals(PERMISSIONS.PERMISSIONS_GUEST)) {
            logger.info(permission + " has permission to reply");
            subforum.replyToMessage(original, reply);
        } else {
            logger.error(permission + " has no permission to reply");
            throw new PermissionDeniedException("User has no permission to reply");
        }
    }

   @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws PermissionDeniedException, ModeratorDoesNotExistsException {
       if(!permission.equals(PERMISSIONS.PERMISSIONS_GUEST)) {
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
    public ThreadI[] getThreads() {
        return subforum.getThreads().toArray(new ThreadI[0]);
    }

    @Override
    public void setModerator(UserI moderator) throws PermissionDeniedException {
        if( permission.equals(PERMISSIONS.PERMISSIONS_ADMIN)) {
            logger.info(permission + " has permission to set moderator");
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
    public boolean findForum(String name) {
        //TODO what is this method for?
        return false;
    }

    private boolean canDeleteMessage(MessageI message, String deleter) {
        return message.getUser().equals(deleter);
    }

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
