package main_test;

import main.exceptions.*;
import main.interfaces.*;

import java.util.Collection;

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
    public void initilize() {
        if(this.real!=null)
            this.real.initilize();
    }

    @Override
    public Collection<ExForumI> getForumList() {
        if(this.real!=null)
            return this.real.getForumList();
        return null;
    }


    @Override
    public Collection<ExSubForumI> getSubForumList(int sessionId) {
        if(this.real!=null)
            return this.real.getSubForumList(sessionId);
        return null;    }

    @Override
    public void addForum(String username, String password, String forumName, String regex, int numberOfModerators) {
        if(this.real!=null)
            this.real.addForum(username, password, forumName, regex, numberOfModerators);
    }

    @Override
    public void createSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException {
        if(this.real!=null)
            this.real.createSubforum(sessionId, subforumName);
    }

    @Override
    public void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException {
        if(this.real!=null)
            this.real.register(forumName, userName, password, email);
    }

    @Override
    public int login(String forumName, String userName, String password) throws InvalidUserCredentialsException {
        if(this.real!=null)
            return this.real.login(forumName, userName, password);
        return 0;
    }

    @Override
    public void logout(int sessionId) {
        if(this.real!=null)
            this.real.logout(sessionId);
    }

    @Override
    public void addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException {
        if(this.real!=null)
            this.real.addReply(sessionId, srcMessageId, title, body);
    }

    @Override
    public void createNewThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        if(this.real!=null)
            this.real.createNewThread(sessionId, srcMessageTitle, srcMessageBody);
    }

    @Override
    public void reportModerator(int sessionId, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
        if(this.real!=null)
            this.real.reportModerator(sessionId, moderatorUserName, reportMessage);
    }

    @Override
    public String getUserAuthString(String forumName, String username, String password, String authenticationString) throws InvalidUserCredentialsException {
        if(this.real!=null)
            return this.real.getUserAuthString(forumName, username, password, authenticationString);
        return null;    }

    @Override
    public void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException {
        if(this.real!=null)
            this.real.deleteMessage(sessionId, messageId);
    }

    @Override
    public void setModerator(int sessionId, String moderatorName) throws PermissionDeniedException {
        if(this.real!=null)
            this.real.setModerator(sessionId, moderatorName);
    }

    @Override
    public int guestEntry(String forumName) {
        if(this.real!=null)
            return this.real.guestEntry(forumName);
        return 0;
    }

    @Override
    public void addUserType(int sessionId, String typeName, int seniority, int numOfMessages, int connectionTime) {
        if(this.real!=null)
            this.real.addUserType(sessionId, typeName, seniority, numOfMessages, connectionTime);
    }

    @Override
    public void removeForum(String username, String password, String forumName) {
        if(this.real!=null)
            this.real.getForumList();
    }

    @Override
    public void editMessage(int sessionId, int messageId, String title, String text) {
        if(this.real!=null)
            this.real.editMessage(sessionId, messageId, title, text);
    }

    @Override
    public void removeModerator(int sessionId, String moderatorName) {
        if(this.real!=null)
            this.real.removeModerator(sessionId, moderatorName);
    }

    @Override
    public String viewModeratorStatistics(int sessionsId) {
        if(this.real!=null)
            return this.real.viewModeratorStatistics(sessionsId);
        return null;
    }

    @Override
    public String viewSuperManagerStatistics(int sessionId) {
        if(this.real!=null)
            return this.real.viewSuperManagerStatistics(sessionId);
        return null;
    }

    @Override
    public String viewSessions(int sessionId) {
        if(this.real!=null)
            return this.real.viewSessions(sessionId);
        return null;
    }




}