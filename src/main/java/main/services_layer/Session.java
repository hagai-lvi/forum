package main.services_layer;

import main.forum_contents.Forum;
import main.forum_contents.SubForum;
import main.interfaces.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by gabigiladov on 5/31/15.
 */
public class Session {
    private int sessionId;
    private UserI user;
    private ForumI forum;
    private SubForumI subForum;
    private ThreadI thread;


    public Session(int sessionId, UserI user) {
        this.sessionId = sessionId;
        this.user = user;
    }

    public int getId(){
        return sessionId;
    }

    public void setThread(ThreadI thread) {
        this.thread = thread;
    }

    public ThreadI getThread() { return thread; }

    public void setForum(ForumI forum) {
        this.forum = forum;
    }

    public ForumI getForum() {
        return forum;
    }

    public void setSubForum(SubForumI subForum) {
        this.subForum = subForum;
    }

    public SubForumI getSubForum() {
        return subForum;
    }

    public UserI getUser() {
        return user;
    }
}
