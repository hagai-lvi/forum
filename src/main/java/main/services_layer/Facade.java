package main.services_layer;

import main.exceptions.*;
import main.forum_contents.ForumMessage;
import main.interfaces.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public class Facade implements FacadeI {
	private static Facade theFacade = new Facade();

	private Facade(){}

	private Collection<ForumI> forums = new ArrayList<>();

	@Override
	public void initilize() {
		//TODO
	}

	@Override
	public Collection<ExForumI> getForumList() {
		return null; //TODO
	}

	@Override
	public Collection<ExSubForumI> getSubForumList(int sessionId) {
		return null; //TODO
	}

	@Override
	public void addForum(String username, String password, String forumName, String regex, int numberOfModerators) {
		//TODO
	}

	@Override
	public void createSubforum(int sessionId, String subforumName) throws PermissionDeniedException, SubForumAlreadyExistException {
		//TODO
	}

	@Override
	public void register(String forumName, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException {
		//TODO
	}

	@Override
	public int login(String forumName, String userName, String password) throws InvalidUserCredentialsException {
		return 0;		//TODO
	}

	@Override
	public void logout(int sessionId) {
		//TODO
	}

	@Override
	public void addReply(int sessionId, int srcMessageId, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException {
		//TODO
	}

	@Override
	public void createNewThread(int sessionId, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
		//TODO
	}

	@Override
	public void reportModerator(int sessionId, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
		//TODO
	}

	@Override
	public String getUserAuthString(String forumName, String username, String password, String authenticationString) throws InvalidUserCredentialsException {
		return null;		//TODO
	}

	@Override
	public void deleteMessage(int sessionId, int messageId) throws PermissionDeniedException, MessageNotFoundException {
		//TODO
	}

	@Override
	public void setModerator(int sessionId, String moderatorName) throws PermissionDeniedException {
		//TODO
	}

	@Override
	public int guestEntry(String forumName) {
		return 0;		//TODO
	}

	@Override
	public void addUserType(int sessionId, String typeName, int seniority, int numOfMessages, int connectionTime) {
		//TODO
	}

	@Override
	public void removeForum(String username, String password, String forumName) throws ForumNotFoundException, PermissionDeniedException{
		//TODO
	}

	@Override
	public void editMessage(int sessionId, int messageId, String title, String text) {
		//TODO
	}

	@Override
	public void removeModerator(int sessionId, String moderatorName) {
		//TODO
	}

	@Override
	public String viewModeratorStatistics(int sessionsId) {
		return null;		//TODO
	}

	@Override
	public String viewSuperManagerStatistics(int sessionId) {
		return null;		//TODO
	}

	@Override
	public String viewSessions(int sessionId) {
		return null;		//TODO
	}

	@Override
	public ExMessageI getMessage(int sessionId, int messageId) {
		return null; 		//TODO
	}

	/*@Override
	public Collection<SubForumPermissionI> getSubForumList(UserI user) {
		return user.getSubForumsPermissions();
	}

	@Override
	public void addForum(ForumI toAdd) {
		//TODO shouldn't be a part of the facade, super-admin only
		forums.add(toAdd);
	}

	@Override
	public void createSubforum(String subforumName, UserI user) throws PermissionDeniedException, SubForumAlreadyExistException {
		user.createSubForum(subforumName);
	}

	@Override
	public void register(ForumI forum, String userName, String password, String email) throws UserAlreadyExistsException, InvalidUserCredentialsException {
		forum.register(userName, password, email);
	}

	@Override
	public UserI login(ForumI forum, String userName, String password) throws InvalidUserCredentialsException {
		return forum.login(userName, password);
	}

	@Override
	public void logout(ForumI forum, UserI user) {
		forum.logout(user);
	}

	@Override
	public void addReply(UserI user, SubForumPermissionI subForumPermission, MessageI src, String title, String body) throws MessageNotFoundException, PermissionDeniedException, DoesNotComplyWithPolicyException {
		user.replyToMessage(subForumPermission, src, title, body);

	}

	@Override
	public void createNewThread(UserI user, SubForumPermissionI subForumPermission, String srcMessageTitle, String srcMessageBody) throws PermissionDeniedException, DoesNotComplyWithPolicyException {
		//TODO
		user.createThread(new ForumMessage(null, user, srcMessageBody, srcMessageTitle), subForumPermission);//move the message creation to the user
	}

	@Override
	public void reportModerator(UserI user, SubForumI subforum, String moderatorUserName, String reportMessage) throws PermissionDeniedException, ModeratorDoesNotExistsException {
		user.reportModerator(subforum, moderatorUserName, reportMessage);
	}

	//TODO
	//public void

*/
	public static FacadeI getFacade(){
		return theFacade;
	}

	public static FacadeI dropAllData(){
		theFacade = new Facade();
		return theFacade;
	}

}
