package controller;

import com.fasterxml.jackson.annotation.JsonView;
import data_structures.Tree;
import gui_objects.*;
import main.exceptions.*;
import main.interfaces.ExSubForumI;
import main.interfaces.ExThreadI;
import main.interfaces.FacadeI;
import main.interfaces.SubForumI;
import main.services_layer.Facade;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;


//TODO implement view thread
//TODO implement add message
/**
 * Created by hagai_lvi on 6/2/15.
 * A rest controller to work with the native GUI
 */
@RestController
@RequestMapping("/gui")
public class NativeGuiController {
	public static final String SESSION_ID_ATTR = "session_id";
	private static final String ADMIN_PASS = "ADMIN";
	private static final String ADMIN_USER = "ADMIN";
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return A list of json objects that represents all the forums in the system
	 */
	@JsonView(NativeGuiController.class)
	@RequestMapping(value = "/facade", method = RequestMethod.GET)
	public @ResponseBody
	ForumList showFacade(){
		logger.info("got request getFacade");
		ForumList list = new ForumList();
		FacadeI facade = getFacade();
		list.addAll(facade.getForumList());
		return list;
	}

	/**
	 * Register a user to the specified forum
	 * Example usage:  send post request to {@code http://localhost:8080/forum-system/gui/register/A}
	 *  with the following json{@code {"username":"a", "password":"a", "email":"a@a.a"}}
	 */
	@JsonView(NativeGuiController.class)
	@RequestMapping(value = "/register/{forumID}", method = RequestMethod.POST)
	public @ResponseBody
	void register(@PathVariable String forumID, @RequestBody LoginDetailesG loginDetailes) throws DoesNotComplyWithPolicyException, UserAlreadyExistsException, InvalidUserCredentialsException, ForumNotFoundException {
		FacadeI facade = getFacade();
		facade.register(forumID, loginDetailes.getUsername(), loginDetailes.getPassword(), loginDetailes.getEmail());
	}

	/**
	 * example: {@code {"name":"FORUM","regex":".*","numOfModerators":10,"user":{"username":"ADMIN","password":"ADMIN"}}}
	 * Create a new forum
	 */
	@JsonView(NativeGuiController.class)
	@RequestMapping(value = "/addForum", method = RequestMethod.POST)
	public @ResponseBody
	void addForum(@RequestBody ForumG forum) throws PermissionDeniedException, ForumAlreadyExistException, ForumNotFoundException {
		FacadeI facade = getFacade();
		UserG user = forum.getUser();
		facade.addForum(user.getUsername(), user.getPassword(),
				forum.getName(), true, //TODO what should be here?
				forum.getRegex(), forum.getNumOfModerators(),
				Integer.MAX_VALUE //TODO what should be here?
		);
	}

	/**
	 *
	 * An example request: send post request to<br/> {@code http://localhost:8080/forum-system/gui/forum/FORUM-NAME} <br/>
	 * with the json <code>{"username":"ADMIN","password":"ADMIN"}</code> <br/>
	 * <b>Don't forget the <code>Content-type:application/json</code> http header</b><br/>
	 * <b>NOTE that the forum name is part of the URL, i.e. {@code .../forum/FORUM-NAME}</b>
	 *
	 * @param forumID given as a rest path property
	 * @param user given as a json in the http request body
	 * @return subforums in the specified forum
	 *
	 */
	@JsonView(NativeGuiController.class)
	@RequestMapping(value = "/forum/{forumID}", method = RequestMethod.POST)
	public @ResponseBody
	SubForumList getForum(HttpSession session, @PathVariable String forumID, @RequestBody UserG user) throws PasswordNotInEffectException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException, ForumNotFoundException, SessionNotFoundException {
		logger.info("got request getForum");
		SubForumList list = new SubForumList();
		FacadeI facade = getFacade();
		Integer sessionID = facade.login(forumID, user.getUsername(), user.getPassword());
		session.setAttribute(SESSION_ID_ATTR, sessionID);
		Collection<SubForumI> subForumList = facade.getSubForumList(sessionID).values();

		list.addAll(subForumList);
		return list;
	}

	/**
	 * @return A list of json objects that represents all the forums in the system
	 */
	@JsonView(NativeGuiController.class)
	@RequestMapping(value = "/subforum/{subforumID}", method = RequestMethod.GET)
	public @ResponseBody
	ThreadList getSubforum(HttpSession session, @PathVariable String subforumID ) throws SubForumAlreadyExistException, SessionNotFoundException, SubForumNotFoundException {
		logger.info("got request getSubforum");
		ThreadList list = new ThreadList();
		FacadeI facade = getFacade();
		Integer sessionID = getSessionID(session);
		if (sessionID == null){
			logger.warn("Got request with sessionID=null");
		}
		ExSubForumI exSubForumI = facade.viewSubforum(sessionID, subforumID);
		Collection<? extends ExThreadI> threadsList = exSubForumI.getThreads().values();
		for (ExThreadI t: threadsList){
			list.add(t.getTitle());
		}
		return list;
	}

	@JsonView(NativeGuiController.class)
	public @RequestMapping(value = "/thread/{threadID}")
	Tree getThread(HttpSession session, @PathVariable String threadID) throws DoesNotComplyWithPolicyException, SessionNotFoundException, ThreadNotFoundException {
		FacadeI facade = getFacade();
		ExThreadI exThreadI = facade.viewThread(getSessionID(session), threadID);
		return exThreadI.getMessages();
	}

	@RequestMapping(value = "logout")
	public void logout(HttpSession session) throws SessionNotFoundException {
		Integer sessionID = getSessionID(session);
		removeSessionID(session);
		FacadeI facade = getFacade();
		facade.logout(sessionID);
	}

	private void removeSessionID(HttpSession session) {
		session.removeAttribute(SESSION_ID_ATTR);
	}

	private FacadeI getFacade() {
		return Facade.getFacade();
	}

	private Integer getSessionID(HttpSession session) {
		Object attribute = session.getAttribute(SESSION_ID_ATTR);
		if (attribute == null){
			return null;
		}
		else{
			return (int) attribute;
		}
	}

}
