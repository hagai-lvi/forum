package main.User;

import com.sun.xml.internal.bind.v2.TODO;
import main.exceptions.PermissionDeniedException;
import main.forum_contents.SubForum;
import main.interfaces.*;
import org.apache.log4j.Logger;

/**
 * Created by gabigiladov on 4/11/15.
 */
public class UserPermission implements ForumPermissionI, SubForumPermissionI {

    private String permission;
    private ForumI forum;
    private SubForumI subforum;
    private static Logger logger = Logger.getLogger(UserPermission.class.getName());

    public UserPermission(String permission, ForumI forum, SubForumI subforum){
        logger.info("Creating new permissions for - " + permission);
        this.forum = forum;
        this.subforum = subforum;
        this.permission = permission;
    }

    @Override
    public SubForumPermissionI[] viewSubForums() {
        return new SubForumPermissionI[0];
    }

    /**
     * Create a subforum in this forum
     */
    public SubForumI createSubForum(String name) throws PermissionDenied{
        if(canCreateSubForum()) {
            logger.info(permission + " has permission to create Sub-Forum");
            return new SubForum(name);
        }
<<<<<<< Updated upstream
        logger.error("The user - " + currentUser.getUsername() + " has no permission to create Sub-Forum!");
        throw new PermissionDenied("User has no permission to create sub forum", currentUser);
=======
        logger.error(permission + " has no permission to create Sub-Forum!");
        throw new PermissionDeniedException("User has no permission to create sub forum");
>>>>>>> Stashed changes
    }

    private boolean canCreateSubForum(){
        return permission.equals("Moderator") || permission.equals("Administrator");
    }
    /**
     * Delete a subForum from this forum
     */
    public void deleteSubForum(SubForumI toDelete) throws PermissionDenied {
        if(canDeleteSunForum()) {
            logger.info(permission + " has permission to delete Sub-Forum");
            // TODO
        }
<<<<<<< Updated upstream
        logger.error("The user - " + currentUser.getUsername() + " has no permission to delete Sub-Forum!");
        throw new PermissionDenied("User has no permission to delete sub forum", currentUser);
=======
        logger.error(permission + " has no permission to delete Sub-Forum!");
        throw new PermissionDeniedException("User has no permission to delete sub forum");
>>>>>>> Stashed changes
    }

    private boolean canDeleteSunForum() {
        return permission.equals("Moderator") || permission.equals("Administrator");
    }

    @Override
    public void setAdmin(UserI admin, ForumI forum) {

    }

    @Override
    public void setPolicy(ForumI forum, ForumPolicyI policy) {

    }

    @Override
    public String viewStatistics(ForumI forum) {
        return null;
    }

    @Override
    public void addForum(ForumI forum) {

    }

    @Override
    public void createThread(MessageI message) {

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

    @Override
    public ThreadI[] getThreads() {
        return new ThreadI[0];
    }

    @Override
    public void setModerator(SubForumI subForum, UserI moderator) {

    }

    @Override
    public void banModerator(UserI moderatorToBan, long time) {

    }

    @Override
    public void sendFriendRequest(UserI newFriend) {

    }

    @Override
    public SubForumI getSubForum() {
        return subforum;
    }
}
