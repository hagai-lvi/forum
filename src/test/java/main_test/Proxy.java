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
    public Collection<ForumI> getForumList() {
        if(this.real!=null)
            return this.real.getForumList();
        return null;
    }

    @Override
    public Collection<SubForumPermissionI> getSubForumList(UserI user) {
        if(this.real!=null)
            this.real.getSubForumList(user);
        return null;
    }

    @Override
    public void addForum(ForumI toAdd) {
        if(this.real!=null)
            this.real.addForum(toAdd);
    }

    @Override
    public void createSubforum(String subforumName, UserI user) throws PermissionDeniedException, SubForumAlreadyExistException {
        if(this.real!=null)
            this.real.createSubforum(subforumName, user);
    }

    @Override
    public void register(ForumI forum, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException {
        if(this.real!=null)
            this.real.register(forum, userName, password, email);
    }

    @Override
    public UserI login(ForumI forum, String userName, String password) throws InvalidUserCredentialsException {
        if(this.real!=null)
           return this.real.login(forum, userName, password);
        return null;
    }

    @Override
    public void logout(ForumI forum, UserI user) {
        if(this.real!=null)
            this.real.logout(forum, user);
    }

    @Override
    public void addReply(UserI user, SubForumPermissionI subforumPermissions, MessageI src, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException {
        if(this.real!=null)
            this.real.addReply(user, subforumPermissions, src, title, body);
    }

    @Override
    public void createNewThread(UserI user, SubForumPermissionI subforumPermission, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
        if(this.real!=null)
            this.real.createNewThread(user, subforumPermission, srcMessageTitle, srcMessageBody);
    }

    @Override
    public void reportModerator(UserI user, SubForumI subforum, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
        if(this.real!=null)
            this.real.reportModerator(user, subforum, moderatorUserName, reportMessage);
    }
}