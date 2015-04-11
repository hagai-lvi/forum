package main.forum_contents;
import main.interfaces.MessageI;
import main.interfaces.SubForumI;
import main.interfaces.SubForumPermissionI;
import main.interfaces.UserI;

/**
 * Created by xkcd2 on 4/11/2015.
 */
public class SubForumPermission implements SubForumPermissionI{
    SubForumI _theSubForum = null;
    UserI     _theUser = null;

    // SubForumPermission is a class connecting a subforum and a user, so it has to have instances of both.

    public SubForumPermission(SubForumI _subforum, UserI user){
        this._theSubForum = _subforum;
        this._theUser     = user;
    };

    @Override
    public void creatThread(MessageI message) {
        return; // TODO: implement
    }

    @Override
    public void replyToMessage(MessageI original, MessageI reply) {
        return; // TODO: implement
    }

    @Override
    public void reportModerator(String moderatorUsername, String reportMessage) {
        return; // TODO: impelment
    }

    @Override
    public void deleteMessage(MessageI message) {
        return; // TODO: implement
    }

}
