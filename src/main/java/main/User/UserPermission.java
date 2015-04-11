package main.User;

import main.exceptions.PermissionDenied;
import main.forum_contents.SubForum;
import main.interfaces.*;

import org.apache.log4j.Logger;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class UserPermission implements ForumPermissionI, SubForumPermissionI {

    private UserI currentUser;
    private static Logger logger = Logger.getLogger(UserPermission.class.getName());

    public UserPermission(UserI currentUser){
        logger.info("Creating new permissions for - " + currentUser.getUsername());
        this.currentUser = currentUser;
    }

    /**
     * Create a subforum in this forum
     */
    public SubForumI createSubForum(String name) throws PermissionDenied{
        if(canCreateSubForum()) {
            logger.info("The user - " + currentUser.getUsername() + " has permission to create Sub-Forum");
            return new SubForum(name);
        }
        logger.error("The user - " + currentUser.getUsername() + " has no permission to create Sub-Forum!");
        throw new PermissionDenied("User has no permission to create sub forum", currentUser);
    }

    private boolean canCreateSubForum(){
        return false;
    }
    /**
     * Delete a subForum from this forum
     */
    public void deleteSubForum(SubForumI toDelete){

    }

    /**
     * create a thread in the subforum
     */
    public void creatThread(MessageI message){

    }

    /**
     * reply to a specific message
     */
    public void replyToMessage(MessageI original, MessageI reply){

    }

    /**
     * Allows a user to report a moderator
     */
    public void reportModerator(String moderatorUsername, String reportMessage){

    }

    /**
     * Delete a specific message if the message was create by the user that sent this request
     */
    public void deleteMessage(MessageI message){

    }
}
