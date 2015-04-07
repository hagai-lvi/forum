package main.forum_contents;

import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.SubForumAlreadyExistException;
import main.interfaces.ForumI;
import main.interfaces.SubForumI;
import main.interfaces.UserI;

import java.util.HashMap;

/**
 * Created by hagai on 07/04/15.
 */
public class Forum implements ForumI {

    private HashMap<String, SubForumI> _subForums = new HashMap<>();
    private HashMap<String, UserI> _users = new HashMap<>();
    private UserI guest = null; //TODO initialize

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
    public UserI register(String userName, String password, String eMail) {
        return null;
    }

    @Override
    public void sendAuthenticationEMail(UserI user) {
        //TODO create a mini mail client? generate an authentication link?

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
}