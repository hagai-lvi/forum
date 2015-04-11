package main.forum_contents;

import main.User.User;
import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.SubForumAlreadyExistException;
import main.exceptions.UserAlreadyExistsException;
import main.interfaces.ForumI;
import main.interfaces.ForumPolicyI;
import main.interfaces.SubForumI;
import main.interfaces.UserI;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by hagai on 07/04/15.
 */
public class Forum implements ForumI {

    private ForumPolicyI policy;
    private HashMap<String, SubForumI> _subForums = new HashMap<>();
    private HashMap<String, UserI> _users = new HashMap<>();
    private UserI guest = null; //TODO initialize

    public Forum(ForumPolicyI policy){
        this.policy = policy;
    }

    @Override
    public SubForumI createSubForum(String name) throws SubForumAlreadyExistException {
        if (_subForums.containsKey(name)){
            throw new SubForumAlreadyExistException(name,this);
        }

        SubForumI subForum = new SubForum(name);
        _subForums.put(name,subForum);
        return subForum;
    }

    @Override
    public UserI register(String userName, String password, String eMail) throws UserAlreadyExistsException, InvalidUserCredentialsException {
        // Protective Programing
        if (userName.equals("") || userName == null || password.equals("") || password == null || eMail.equals("") || eMail == null)
            throw new InvalidUserCredentialsException();
        if (_users.containsKey(userName)){
            throw new UserAlreadyExistsException(userName);
        }
        // we are done with protective programing, time to do work.
        User new_user = new User(userName, password, eMail);
        new_user.addForum(this);  // Gabi said this will be the logic
        return new_user;
    }

    @Override
    public void sendAuthenticationEMail(UserI user) {
        return;

    }

    @Override
    public UserI login(String username, String password) throws InvalidUserCredentialsException {
        if (_users.containsKey(username) &&
                _users.get(username).getPassword().equals(password)){
            return _users.get(username);
        }
        else {
            throw new InvalidUserCredentialsException();
        }
    }

    @Override
    public UserI guestLogin() {
        // TODO initialize guest
        return guest;
    }

    @Override
    public void logout(UserI user) {
        //TODO what should happen?

    }

    @Override
    public void setPolicy(ForumPolicyI policy) {
        this.policy = policy;
    }

    @Override
    public Collection<UserI> getUserList() {
        return _users.values();
    }
}
