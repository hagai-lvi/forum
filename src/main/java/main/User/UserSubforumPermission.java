package main.User;

import main.exceptions.*;
import main.interfaces.*;
import org.apache.log4j.Logger;
import javax.persistence.*;

/**
 * Created by gabigiladov on 4/11/15.
 */
    public class UserSubforumPermission implements SubForumPermissionI {

    public static final String PERMISSION_GUEST = "GUEST";
    private String permission;
    private ForumI forum;
    private SubForumI subforum;
    private static Logger logger = Logger.getLogger(UserSubforumPermission.class.getName());

    public UserSubforumPermission(String permission, ForumI forum, SubForumI subforum){
        logger.info("Creating new permissions for - " + permission);
        this.forum = forum;
        this.subforum = subforum;
        this.permission = permission;
    }

    /**
     * Create a subforum in this forum.
     */
    public void createSubForum(String name) throws PermissionDeniedException, SubForumAlreadyExistException {
        if(canCreateSubForum()) {
            logger.info(permission + " has permission to create Sub-Forum");
            forum.createSubForum(name);
        } else {
            logger.error(permission + " has no permission to create Sub-Forum!");
            throw new PermissionDeniedException("User has no permission to create sub forum");
        }
    }

    private boolean canCreateSubForum(){
        return permission.equals("Moderator") || permission.equals("Administrator");
    }
    /**
     * Delete a subForum from this forum
     */
    public void deleteSubForum(SubForumI toDelete) throws PermissionDeniedException {
        if(canDeleteSunForum()) {
            logger.info(permission + " has permission to delete Sub-Forum");
            deleteSubForum(toDelete);
        }
        else {
            logger.error(permission + " has no permission to delete Sub-Forum!");
            throw new PermissionDeniedException("User has no permission to delete sub forum");
        }
    }

    private boolean canDeleteSunForum() {
        return permission.equals("Moderator") || permission.equals("Administrator");
    }

    @Override
    public void createThread(MessageI message) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        if( ! permission.equals(PERMISSION_GUEST)) {
            logger.info(permission + " has permission to create thread");
            subforum.createThread(message);
        } else {
            logger.error(permission + " has no permission to create thread");
            throw new PermissionDeniedException("User has no permission to create thread");
        }
    }

   @Override
    public void replyToMessage(MessageI original, MessageI reply) throws MessageNotFoundException, DoesNotComplyWithPolicyException, PermissionDeniedException {
        if(!permission.equals(PERMISSION_GUEST)) {
            logger.info(permission + " has permission to reply");
            subforum.replyToMessage(original, reply);
        } else {
            logger.error(permission + " has no permission to reply");
            throw new PermissionDeniedException("User has no permission to reply");
        }
    }

   @Override
    public void reportModerator(String moderatorUsername, String reportMessage, UserI reporter) throws PermissionDeniedException, ModeratorDoesNotExistsException {
       if(!permission.equals(PERMISSION_GUEST)) {
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
    public void deleteMessage(MessageI message, UserI deleter) throws PermissionDeniedException, MessageNotFoundException {
        if(canDeleteMessage(message, deleter)) {
            logger.info(permission + " has permission to delete message");
            subforum.deleteMessage(message, deleter);
        } else {
            logger.error(permission + " has no permission to delete message");
            throw new PermissionDeniedException("User has no delete message");
        }
    }

    @Override
    public ThreadI[] getThreads() {
        return subforum.getThreads().toArray(new ThreadI[0]);
    }

    @Override
    public void setModerator(UserI moderator) throws PermissionDeniedException {
        if( permission.equals("Administrator")) {
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

    private boolean canDeleteMessage(MessageI message, UserI deleter) {
        return message.getUser().equals(deleter);
    }
}
