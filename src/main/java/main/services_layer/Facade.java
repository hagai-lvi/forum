package main.services_layer;

import data_structures.Tree;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumMessage;
import main.forum_contents.ForumPolicy;
import main.interfaces.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by hagai_lvi on 4/11/15.
 */
     public class Facade implements FacadeI {
	private static FacadeI theFacade;
	private ArrayList<UserI> users;
	private Collection<Session> openSessions;
	private ArrayList<ForumI> forums;
	private int sessionCounter;
	private static final int GUEST_SESSION_ID = -2;
	private Facade(){
		initilize();
	}


	@Override
	public void initilize() {
		//TODO
		openSessions =  new ArrayList<Session>();
		forums = new ArrayList<ForumI>();
		users = new ArrayList<UserI>();
		sessionCounter = 0;
	}

	@Override
	public ArrayList<ForumI> getForumList() {
		return forums;
	}

	@Override
	public Collection<SubForumI> getSubForumList(int sessionId) {
		return findSession(sessionId).getForum().getSubForums();
	}

	@Override
	public void addForum(String username, String password, String forumName, String regex, int numberOfModerators) throws PermissionDeniedException, ForumAlreadyExistException {
		//TODO
		forums.add(new Forum(forumName, new ForumPolicy(numberOfModerators, regex)));
	}

	@Override
	public void createSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException {
		Session current = findSession(sessionId);
		current.setSubForum(current.getForum().createSubForum(subforumName));
	}

	@Override
	public void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException {
		ForumI current = findForum(forumName);
		if (current == null) return;
		UserI currentUser = current.register(userName, password, email);
		users.add(currentUser);
	}


	@Override
	public int login(String forumName, String userName, String password) throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException {
		ForumI current = findForum(forumName);
		UserI currentUser = current.login(userName, password);
		Session currentSession = new Session(sessionCounter, currentUser);
		currentSession.setForum(current);
		openSessions.add(currentSession);
		sessionCounter++;
		return sessionCounter-1;
	}

	@Override
	public void logout(int sessionId) {
		Session current = findSession(sessionId);
		openSessions.remove(current);
	}

	@Override
	public void addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException {
		//TODO srcMessageId??
	}

	@Override
	public int createNewThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
		Session current = findSession(sessionId);
		ForumMessage msg = new ForumMessage(null, current.getUser(), srcMessageBody, srcMessageTitle);
		current.getSubForum().createThread(msg);
		return msg.getId();
	}

	@Override
	public void reportModerator(int sessionId, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
		Session current = findSession(sessionId);
		current.getSubForum().reportModerator(moderatorUserName, reportMessage, current.getUser());
	}

	@Override
	public String getUserAuthString(String forumName, String username, String password, String authenticationString) throws InvalidUserCredentialsException {	boolean flag = false;
		UserI current = findUser(username);
		return current.getUserAuthString();
	}

	@Override
	public void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException {
		Session current = findSession(sessionId);
			// TODO message id????
	}

	@Override
	public void setModerator(int sessionId, String moderatorName) throws PermissionDeniedException {
		Session current = findSession(sessionId);
		current.getSubForum().setModerator(findUser(moderatorName));
	}

	@Override
	public int guestEntry(String forumName) {
		ForumI current = findForum(forumName);
		Session currentSession = new Session(sessionCounter, null);
		currentSession.setForum(current);
		openSessions.add(currentSession);
		return GUEST_SESSION_ID;
	}

	@Override
	public void addUserType(int sessionId, String typeName, int seniority, int numOfMessages, int connectionTime) {
		Session current = findSession(sessionId);
		current.getForum().addUserType(typeName, seniority, numOfMessages, connectionTime);
	}

	@Override
	public void removeForum(String username, String password, String forumName) throws ForumNotFoundException, PermissionDeniedException{
		// TODO update database
		forums.remove(findForum(forumName));
	}

	@Override
	public void setPolicies(int sessionId, String regex, int numOfModerators) {
		Session current = findSession(sessionId);
		current.getForum().setPolicy(new ForumPolicy(numOfModerators, regex));
	}

	@Override
	public void editMessage(int sessionId, int messageId, String title, String text) {
		Session current = findSession(sessionId);
		// TODO messageId????
	}

	@Override
	public void removeModerator(int sessionId, String moderatorName) {
		Session current = findSession(sessionId);
		current.getSubForum().removeModerator(findUser(moderatorName));
	}

	@Override
	public String viewModeratorStatistics(int sessionsId) {
		Session current = findSession(sessionsId);
		return current.getForum().viewStatistics();
	}

	@Override
	public String viewSuperManagerStatistics(int sessionId) {
		Session current = findSession(sessionId);
		return  null;
		//TODO
	}

	@Override
	public String viewSessions(int sessionId) {
		return null;
		//TODO
	}

	@Override
	public ExMessageI getMessage(int sessionId, int messageId) {
		return null; 		//TODO messageId??
	}

	@Override
	public Collection<ThreadI> getThreadsList(int sessionId) {
		Session current = findSession(sessionId);
		return current.getSubForum().getThreads();

	}

	@Override
	public Tree getMessageList(int sessionId) {
		Session current = findSession(sessionId);
		return current.getThread().getMessages();
		//TODO
	}

	@Override
	public String getCurrentForumName(int sessionID) {
		Session current = findSession(sessionID);
		return current.getForum().getName();
	}

	@Override
	public String getCurrentUserName(int sessionID) {
		Session current = findSession(sessionID);
		return current.getUser().getUsername();
	}

	@Override
	public boolean isAdmin(int sessionID) {
		Session current = findSession(sessionID);
		return current.getUser().isAdmin();
	}

	@Override
	public void viewSubforum(int sessionId, String subforum) throws SubForumAlreadyExistException {
		Session current = findSession(sessionId);
		SubForumI sub = current.getForum().createSubForum(subforum);
		current.setSubForum(sub);
	}

	@Override
	public void viewThread(int sessionId, String title) throws DoesNotComplyWithPolicyException {
		Session current = findSession(sessionId);
		ThreadI thread = current.getSubForum().createThread(new ForumMessage(null, current.getUser(), null, title));
		current.setThread(thread);
	}


	public static FacadeI getFacade(){
		if (theFacade == null) {
			theFacade = new Facade();
		}
		return theFacade;
	}

	public static FacadeI dropAllData(){
		theFacade = new Facade();
		return theFacade;
	}

	private Session findSession(int sessionId) {
		boolean flag = false;
		Session current = openSessions.iterator().next();
		while(openSessions.iterator().hasNext()) {
			if(current.getId() == sessionId){
				flag =true;
				break;
			}
			current = openSessions.iterator().next();
		}
		if(flag){
			return current;
		}
		else {
			return null;
			//TODO add exception session does not exist
		}
	}

	private ForumI findForum(String forumName) {
		boolean flag = false;
		Iterator<ForumI> iter = forums.iterator();
		for (ForumI f: forums){
			if (f.getName().equals(forumName)){
				return f;
			}
		}
		return null;//TODO throw exception
	}

	private UserI findUser(String name) {
		boolean flag = false;
		UserI current = users.iterator().next();
		while(users.iterator().hasNext()) {
			if(current.getUsername().equals(name)) {
				flag = true;
				break;
			}
			current = users.iterator().next();
		}
		if(flag) {
			return current;
		}
		else {
			//TODO user does not exist
			return null;
		}
	}

}
