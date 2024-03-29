package main.services_layer;

import data_structures.Tree;
import main.User.User;
import main.exceptions.*;
import main.forum_contents.Forum;
import main.forum_contents.ForumPolicy;
import main.interfaces.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;

/**
 * Created by hagai_lvi on 4/11/15.
 */
     public class Facade implements FacadeI {
	private static final String SUPER_ADMIN_USERNAME = "ADMIN";
	private static final String SUPER_ADMIN_PASSWORD = "ADMIN";
	private static FacadeI theFacade;
	private Collection<Session> openSessions;
	private int sessionCounter;
	private Vector<String> messagesToSuperAdmin = new Vector<>();

	private Facade(){
		initialize();
	}


	@Override
	public void initialize() {
		openSessions = new ArrayList<>();
		sessionCounter = 0;
	}

	@Override
	public ArrayList<String> getForumList() {
		return
				Forum.getForumList();
	}

	@Override
	public Map<String, SubForumI> getSubForumList(int sessionId) throws SessionNotFoundException {
		return findSession(sessionId).getForum().getSubForums();
	}

	public Collection<Session> getSessions() {
		return this.openSessions;
	}
	@Override
	public void addForum(String username, String password, String forumName, boolean isSecured, String regex, int numberOfModerators, int passLife) throws PermissionDeniedException, ForumAlreadyExistException {
		if (username.equals(SUPER_ADMIN_USERNAME) && password.equals(SUPER_ADMIN_PASSWORD)){
			ForumI forum = findForum(forumName);
			if(forum != null) throw new ForumAlreadyExistException("Forum already exist");
			ForumPolicyI policy = new ForumPolicy(isSecured, numberOfModerators, regex, passLife);
			forum = new Forum(forumName, policy);
		}
		else {
			throw new PermissionDeniedException(MessageFormat.format("user {0} is not authorized to add a forum.", username));
		}

	}

	@Override
	public void addSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException, SessionNotFoundException, ForumNotFoundException, SubForumDoesNotExistException {
		Session currentSession = findSession(sessionId);
		SubForumI subforum = currentSession.getUser().createSubForum(subforumName);

		currentSession.setSubForum(subforum.getTitle());
	}

	@Override
	public void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException, DoesNotComplyWithPolicyException {
		ForumI currentForum = findForum(forumName);
		if(currentForum == null) throw new ForumNotFoundException("Forum not found");
		currentForum.register(userName, password, email);
	}


	@Override
	public int login(String forumName, String userName, String password) throws InvalidUserCredentialsException, EmailNotAuthanticatedException, PasswordNotInEffectException, NeedMoreAuthParametersException, ForumNotFoundException {
		ForumI currentForum = findForum(forumName);
		if(currentForum == null) throw new ForumNotFoundException("Forum not found");
		//if(currentForum.isSecured()) currentForum.securedLogin(userName, password, ans);
		UserI currentUser = currentForum.login(userName, password);
		Session currentSession = new Session(sessionCounter, userName); //create a new session
		currentSession.addCommand(userName + " has logged in to " + forumName);
		currentSession.setForum(currentForum.getName());
		openSessions.add(currentSession);
		sessionCounter++;
		return sessionCounter - 1;
	}

	@Override
	public void logout(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		openSessions.remove(current);
	}

	@Override
	public int addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException {
		Session currentSession = findSession(sessionId);
		currentSession.addCommand("Added reply " + title + ", " + body + " to message " + srcMessageId);
		return currentSession.getUser().replyToMessage(currentSession.getSubForum().getTitle(), currentSession.getThread().getMessages().find(srcMessageId), title, body);

	}

	@Override
	public int addThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException, SessionNotFoundException, SubForumDoesNotExistException {
		Session currentSession = findSession(sessionId);
		ThreadI th = currentSession.getUser().createThread(srcMessageTitle, srcMessageBody, currentSession.getSubForum().getTitle());
		currentSession.addCommand("Added thread " + srcMessageTitle);
		currentSession.setThread(srcMessageTitle);
		return th.getRootMessage().getId();
	}

	@Override
	public void reportModerator(int sessionId, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException, SessionNotFoundException {
		Session current = findSession(sessionId);
		current.addCommand(" reporting moderator " + moderatorUserName);
		addMessageToSuperAdmin("A report for moderator " + moderatorUserName + " was recieved from " +
				current.getUser().getUsername() + ":\n" + reportMessage);
//		current.getSubForum().reportModerator(moderatorUserName, reportMessage, current.getUser());
	}

	@Override
	public String getUserAuthString(String forumName, String username, String password) throws InvalidUserCredentialsException, UserNotFoundException {
		UserI current = findUser(username, forumName);
		return current.getUserAuthString();
	}

	private UserI findUser(String username, String forumName) {
		return User.getUserFromDB(username, forumName);
	}


	@Override
	public void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException, SessionNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException {
		Session current = findSession(sessionId);
		current.addCommand("Deleting message " + messageId );
		current.getUser().deleteMessage(current.getSubForum().getTitle(), current.getThread().getTitle(), current.getThread().getMessages().find(messageId));
	}

	@Override
	public void setModerator(int sessionId, String moderatorName) throws PermissionDeniedException, UserNotFoundException, SessionNotFoundException, SubForumNotFoundException, ForumNotFoundException, CloneNotSupportedException, TooManyModeratorsException {
		Session current = findSession(sessionId);
		current.getUser().setModerator(current.getSubForum().getTitle(), findUser(moderatorName, current.getForum().getName()));
		current.addCommand("Adding moderator " + moderatorName);
	}

	@Override
	public int guestEntry(String forumName) throws ForumNotFoundException {
		ForumI current = findForum(forumName);
		if(current == null) throw new ForumNotFoundException("Forum not found");
		Session currentSession = new Session(sessionCounter, current.getGuest().getUsername());
		currentSession.setForum(current.getName());
		openSessions.add(currentSession);
		sessionCounter++;
		return sessionCounter - 1;
	}

	@Override
	public void addUserType(int sessionId, String typeName, int seniority, int numOfMessages, int connectionTime) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		current.getForum().addUserType(typeName, seniority, numOfMessages, connectionTime);
		current.addCommand("Adding user type " + typeName);
	}

	@Override
	public void removeForum(String username, String password, String forumName) throws ForumNotFoundException, PermissionDeniedException{
		if (username.equals(SUPER_ADMIN_USERNAME) && password.equals(SUPER_ADMIN_PASSWORD)) {
			Forum.delete(forumName);
		}else{
			throw new PermissionDeniedException("Unauthorized removal of a forum");
		}
	}

	@Override
	public void setPolicies(int sessionId, boolean isSecured, String regex, int numOfModerators, int passLife) throws SessionNotFoundException, PermissionDeniedException, ForumNotFoundException {
		Session current = findSession(sessionId);
		current.getUser().setPolicy(new ForumPolicy(isSecured, numOfModerators, regex, passLife));
		current.addCommand("setting policy");
	}

	@Override
	public void editMessage(int sessionId, int messageId, String title, String text) throws SessionNotFoundException, MessageNotFoundException, SubForumDoesNotExistException, ThreadNotFoundException, PermissionDeniedException {
		Session current = findSession(sessionId);
		current.getUser().editMessage(current.getSubForum().getTitle(), current.getThread(), messageId, title, text);
		current.addCommand("Editting message " + messageId + ".\n new message:" + title + ", " + text);
	}

	@Override
	public void removeModerator(int sessionId, String moderatorName) throws UserNotFoundException, SessionNotFoundException, SubForumDoesNotExistException, PermissionDeniedException {
		Session current = findSession(sessionId);
		current.getUser().removeModerator(current.getSubForum().getTitle(), moderatorName);
		current.addCommand("removing moderator " + moderatorName);
	}

	@Override
	public String viewModeratorStatistics(int sessionsId) throws SessionNotFoundException, PermissionDeniedException, SubForumDoesNotExistException {
		Session current = findSession(sessionsId);
		current.addCommand("viewing statistics");
		return current.getUser().viewStatistics();
	}

	@Override
	public String viewSuperManagerStatistics(String username, String password) throws PermissionDeniedException, SubForumDoesNotExistException {
		if (username.equals(SUPER_ADMIN_USERNAME) && password.equals(SUPER_ADMIN_PASSWORD)){
			StringBuilder result = new StringBuilder();
			ArrayList<String> forums = getForumList();
			for(String s:forums){
				result.append(Forum.load(s).viewStatistics()+"\n\n");
			}
			return result.toString();
		}
		else {
			throw new PermissionDeniedException(MessageFormat.format("user {0} is not authorized to view system statistics.", username));
		}
	}


	@Override
	public ExMessageI getMessage(int sessionId, int messageId) throws SessionNotFoundException, ThreadNotFoundException {
		Session current = findSession(sessionId);
		ThreadI thread = current.getThread();
		Tree messages = thread.getMessages();
		current.addCommand("viewing message " + messageId);
		return messages.find(messageId);
	}

	@Override
	public Map<String, ThreadI> getThreadsList(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		current.addCommand(" viewing threads list");
		return current.getSubForum().getThreads();

	}

	@Override
	public Tree getMessageList(int sessionId) throws SessionNotFoundException, ThreadNotFoundException {
		Session current = findSession(sessionId);
		current.addCommand("viewing messages for thread " + current.getThread().getTitle());
		return current.getThread().getMessages();
	}

	@Override
	public String getCurrentForumName(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		return current.getForum().getName();
	}

	@Override
	public String getCurrentUserName(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		return current.getUser().getUsername();
	}

	@Override
	public boolean isAdmin(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		return current.getUser().isAdmin();
	}

	@Override
	public boolean isGuest(int sessionID) throws SessionNotFoundException {
		Session current = findSession(sessionID);
		return current.getUser().isGuest();
	}

	@Override
	public ExSubForumI viewSubforum(int sessionId, String subforum) throws SubForumNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
		Map<String, SubForumI> subForums = current.getForum().getSubForums();
		if(!subForums.containsKey(subforum))
			throw new SubForumNotFoundException();
		current.setSubForum(subforum);
		current.addCommand("viewing subforum " + subforum);
		return subForums.get(subforum);
	}

	@Override
	public ExSubForumI viewSubforum(int sessionId) throws SessionNotFoundException {
		Session current = findSession(sessionId);
		current.addCommand("viewing subforum " + current.getSubForum());
		return current.getSubForum();
	}

	@Override
	public ExThreadI viewThread(int sessionId, String title) throws DoesNotComplyWithPolicyException, ThreadNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
		Map<String, ThreadI> threads = current.getSubForum().getThreads();
		if(!threads.containsKey(title))
			throw new ThreadNotFoundException();
		current.setThread(title);
		current.addCommand("viewing thread " + title);
		return current.getThread();
	}

	@Override
	public ExThreadI viewThread(int sessionId) throws DoesNotComplyWithPolicyException, ThreadNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionId);
		current.addCommand("viewing subforum " + current.getThread().getTitle());
		return current.getThread();
	}

	@Override
	public ThreadI getCurrentThread(int sessionID) throws ThreadNotFoundException, SessionNotFoundException {
		Session current = findSession(sessionID);
		ThreadI thread = current.getThread();
		if (thread == null){
			throw new ThreadNotFoundException();
		}
		return thread;
	}

	@Override
	public void authenticateUser(String forum, String username, String userAuthString) throws EmailNotAuthanticatedException, UserNotFoundException {
		UserI user = findUser(username, forum);
		if(user == null) throw new UserNotFoundException("User not found");
		if (!user.getUserAuthString().equals(userAuthString)){
			throw new EmailNotAuthanticatedException();
		}
		user.setAuthenticated();
	}

	@Override
	public boolean isMessageFromCurrentUser(int sessionId, int messageId) throws SessionNotFoundException, ThreadNotFoundException {
		Session currentSession = findSession(sessionId);
		UserI user = currentSession.getUser();
		MessageI message = currentSession.getThread().getMessages().find(messageId);
		return user.isOwnerOfMessage(message);
	}

	@Override
	public String getCurrentUserForumStatus(int sessionId) throws SessionNotFoundException, SubForumDoesNotExistException {
		Session current = findSession(sessionId);
		return current.getUser().getForumStatus();
	}

	@Override
	public String getCurrentUserSubForumStatus(int sessionId) throws SessionNotFoundException, SubForumDoesNotExistException {
		Session current = findSession(sessionId);
		return current.getUser().getSubForumStatus(current.getSubForum().getTitle());
	}

	@Override
	public void setAdmin(String username, String password, String newAdmin, String forumname) throws UserNotFoundException, PermissionDeniedException, ForumNotFoundException, CloneNotSupportedException {
		User currentAdmin = User.getUserFromDB(username, forumname);
		if(currentAdmin == null) throw new UserNotFoundException("User not found");
		User user = User.getUserFromDB(newAdmin, forumname);
		if(user == null) throw new UserNotFoundException("User not found");
		currentAdmin.setAdmin(user);
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

	private Session findSession(int sessionId) throws SessionNotFoundException {
		for (Session s: openSessions){
			if (s.getId() == sessionId){
				return s;
			}
		}
		throw new SessionNotFoundException();
	}

	private ForumI findForum(String forumName) {
		return Forum.load(forumName);
	}

	@Override
	public Vector<String> getMessagesToSuperAdmin() {
		return messagesToSuperAdmin;
	}

	@Override
	public void addMessageToSuperAdmin(String message){
		messagesToSuperAdmin.add(message);
	}
}
