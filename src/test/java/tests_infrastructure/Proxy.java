package tests_infrastructure;

import data_structures.Tree;
import main.exceptions.*;
import main.interfaces.*;
import main.services_layer.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by gabigiladov on 4/25/15.
 */

public class Proxy implements FacadeI {

    private FacadeI real;

    public Proxy() {
        this.real=null;
    }

    public void setRealBridge(FacadeI real) {
        this.real = real;
    }


    @Override
    public void initialize() {
        if(this.real!=null)
            this.real.initialize();
    }

    @Override
    public ArrayList<String> getForumList() {
        if(this.real!=null)
            return this.real.getForumList();
        return null;
    }


    @Override
    public Map<String, SubForumI> getSubForumList(int sessionId) throws SessionNotFoundException {
        if(this.real!=null)
            return this.real.getSubForumList(sessionId);
        return null;    }

    @Override
    public void addForum(String username, String password, String forumName, boolean isSEcured, String regex, int numberOfModerators, int passLife) throws PermissionDeniedException, ForumAlreadyExistException {
        if(this.real!=null)
            this.real.addForum(username, password, forumName, isSEcured, regex, numberOfModerators, passLife);
    }

    @Override
    public void addSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException, SessionNotFoundException, ForumNotFoundException, SubForumDoesNotExistException {
        if(this.real!=null)
            this.real.addSubforum(sessionId, subforumName);
    }

    @Override
    public void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, DoesNotComplyWithPolicyException {
        if(this.real!=null)
            this.real.register(forumName, userName, password, email);
    }

    @Override
    public int login(String forumName, String userName, String password) throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, ForumNotFoundException {
        if(this.real!=null)
            return this.real.login(forumName, userName, password);
        return 0;
    }

    @Override
    public Collection<Session> getSessions()  {
        return null;
    }

    @Override
    public void logout(int sessionId) throws SessionNotFoundException {
        if(this.real!=null)
            this.real.logout(sessionId);
    }

    @Override
    public void addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException {
        if(this.real!=null)
            this.real.addReply(sessionId, srcMessageId, title, body);
    }

    @Override
    public void addThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, SubForumDoesNotExistException {
        if(this.real!=null) {
            this.real.addThread(sessionId, srcMessageTitle, srcMessageBody);
        }
    }

    @Override
    public void reportModerator(int sessionId, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException, SessionNotFoundException {
        if(this.real!=null)
            this.real.reportModerator(sessionId, moderatorUserName, reportMessage);
    }

    @Override
    public String getUserAuthString(String forumName, String username, String password) throws InvalidUserCredentialsException, UserNotFoundException {
        if(this.real!=null)
            return this.real.getUserAuthString(forumName, username, password);
        return null;    }

    @Override
    public void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException, SessionNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException {
        if(this.real!=null)
            this.real.deleteMessage(sessionId, messageId);
    }

    @Override
    public void setModerator(int sessionId, String moderatorName) throws PermissionDeniedException, UserNotFoundException, SessionNotFoundException, SubForumNotFoundException {
        if(this.real!=null)
            this.real.setModerator(sessionId, moderatorName);
    }

    @Override
    public int guestEntry(String forumName) throws ForumNotFoundException {
        if(this.real!=null)
            return this.real.guestEntry(forumName);
        return 0;
    }

    @Override
    public void addUserType(int sessionId, String typeName, int seniority, int numOfMessages, int connectionTime) throws SessionNotFoundException {
        if(this.real!=null)
            this.real.addUserType(sessionId, typeName, seniority, numOfMessages, connectionTime);
    }

    @Override
    public void removeForum(String username, String password, String forumName) {
        if(this.real!=null)
            this.real.getForumList();
    }

    @Override
    public void setPolicies(int sessionId, boolean isSecure, String regex, int numOfModerators, int passLife) throws SessionNotFoundException, PermissionDeniedException, ForumNotFoundException {
        if(this.real!=null)
            this.real.setPolicies(sessionId, isSecure, regex, numOfModerators, passLife);
    }

    @Override
    public void editMessage(int sessionId, int messageId, String title, String text) throws SessionNotFoundException, MessageNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException {
        if(this.real!=null)
            this.real.editMessage(sessionId, messageId, title, text);
    }

    @Override
    public void removeModerator(int sessionId, String moderatorName) throws UserNotFoundException, SessionNotFoundException, SubForumDoesNotExistException {
        if(this.real!=null)
            this.real.removeModerator(sessionId, moderatorName);
    }

    @Override
    public String viewModeratorStatistics(int sessionsId) throws SessionNotFoundException {
        if(this.real!=null)
            return this.real.viewModeratorStatistics(sessionsId);
        return null;
    }

    @Override
    public String viewSuperManagerStatistics(int sessionId) throws SessionNotFoundException {
        if(this.real!=null)
            return this.real.viewSuperManagerStatistics(sessionId);
        return null;
    }

    @Override
    public String viewSessions(int sessionId) throws ThreadNotFoundException {
        if(this.real!=null)
            return this.real.viewSessions(sessionId);
        return null;
    }

    @Override
    public ExMessageI getMessage(int sessionId, int messageId) throws SessionNotFoundException, ThreadNotFoundException {
        if(this.real!=null)
            return this.real.getMessage(sessionId, messageId);
        return null;
    }

    @Override
    public Map<String, ThreadI> getThreadsList(int sessionId) throws SessionNotFoundException {
        if(this.real!=null)
            return this.real.getThreadsList(sessionId);
        return null;
    }

    @Override
    public Tree getMessageList(int sessionId) throws SessionNotFoundException, ThreadNotFoundException {
        if(this.real!=null)
            return this.real.getMessageList(sessionId);
        return null;
    }

    @Override
    public String getCurrentForumName(int sessionID) throws SessionNotFoundException {
        if(this.real!=null)
            return this.real.getCurrentForumName(sessionID);
        return null;
    }

    @Override
    public String getCurrentUserName(int sessionID) throws SessionNotFoundException {
        if(this.real!=null)
            return this.real.getCurrentForumName(sessionID);
        return null;
    }

    @Override
    public boolean isAdmin(int sessionID) throws SessionNotFoundException {
        if(this.real!=null)
            return this.real.isAdmin(sessionID);
        return false;
    }

    @Override
    public ExSubForumI viewSubforum(int sessionId, String subforum) throws SubForumAlreadyExistException, SessionNotFoundException, SubForumNotFoundException {
        if(this.real!=null)
            return this.real.viewSubforum(sessionId, subforum);
        return null;
    }

    @Override
    public ExSubForumI viewSubforum(int sessionId) throws SessionNotFoundException {
        if (this.real != null)
            return this.real.viewSubforum(sessionId);
        return null;
    }

    @Override
    public ExThreadI viewThread(int sessionId, String title) throws DoesNotComplyWithPolicyException, ThreadNotFoundException, SessionNotFoundException {
        if(this.real!=null)
            return this.real.viewThread(sessionId, title);
        return null;
    }

    @Override
    public ThreadI getCurrentThread(int sessionID) throws ThreadNotFoundException, SessionNotFoundException {
        if (this.real != null)
            return this.real.getCurrentThread(sessionID);
        return null;
    }

    @Override
    public void authenticateUser(String forum, String username, String userAuthString) throws EmailNotAuthanticatedException, UserNotFoundException {
        if (this.real != null)
            this.real.authenticateUser(forum, username, userAuthString);
    }

    @Override
    public boolean isMessageFromCurrentUser(int sessionId, int messageId) throws SessionNotFoundException {
        return false;
    }

    @Override
    public String getCurrentUserForumStatus(int sessionId) throws SessionNotFoundException {
        return null;
    }

    @Override
    public String getCurrentUserSubForumStatus(int sessionId) throws SessionNotFoundException, SubForumDoesNotExistException {
        return null;
    }

    @Override
    public void setAdmin(String username, String password, String newAdmin, String forumname) throws UserNotFoundException, PermissionDeniedException {

    }


}