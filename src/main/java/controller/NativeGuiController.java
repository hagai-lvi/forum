package controller;

import com.fasterxml.jackson.annotation.JsonView;
import gui_objects.ForumList;
import gui_objects.SubForumList;
import gui_objects.UserG;
import main.exceptions.EmailNotAuthanticatedException;
import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.NeedMoreAuthParametersException;
import main.exceptions.PasswordNotInEffectException;
import main.interfaces.FacadeI;
import main.interfaces.SubForumI;
import main.services_layer.Facade;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * Created by hagai_lvi on 6/2/15.
 * A rest controller to work with the native GUI
 */
@RestController
@RequestMapping("/gui")
public class NativeGuiController {
	public static final String SESSION_ID_ATTR = "session_id";
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @return A list of json objects that represents all the forums in the system
	 */
	@JsonView(NativeGuiController.class)
	@RequestMapping(value = "/facade", method = RequestMethod.GET)
	public @ResponseBody
	ForumList getFacade(){
		logger.info("got request getFacade");
		ForumList list = new ForumList();
		FacadeI facade = Facade.getFacade();
		list.addAll(facade.getForumList());
		return list;
	}

	/**
	 *
	 * An example request: send post request to<br/> <code>http://localhost:8080/forum-system/gui/forum/A</code> <br/>
	 * with the json <code>{"username":"ADMIN","password":"ADMIN"}</code> <br/>
	 * <b>Don't forget the <code>Content-type:application/json</code> http header</b>
	 *
	 * @param forumID given as a rest path property
	 * @param user given as a json in the http request body
	 * @return subforums in the specified forum
	 *
	 */
	@JsonView(NativeGuiController.class)
	@RequestMapping(value = "/forum/{forumID}", method = RequestMethod.POST)
	public @ResponseBody
	SubForumList getForum(HttpSession session, @PathVariable String forumID, @RequestBody UserG user) throws PasswordNotInEffectException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException {
		logger.info("got request getSubforum");
		SubForumList list = new SubForumList();
		FacadeI facade = Facade.getFacade();
		Integer sessionID = facade.login(forumID, user.getUsername(), user.getPassword());
		session.setAttribute(SESSION_ID_ATTR, sessionID);
		Collection<SubForumI> subForumList = facade.getSubForumList(sessionID);

		list.addAll(subForumList);
		return list;
	}










	@RequestMapping(value = "/postExp", method = RequestMethod.POST)
	public @ResponseBody
	MyPojoList postExp(@RequestBody UserG user){
		MyPojoList l = new MyPojoList();
		MyPojo p1 = new MyPojo();
		p1.setName(user.getUsername());
		p1.setId(Integer.toString(1));

		MyPojo p2 = new MyPojo();
		p2.setName(user.getPassword());
		p2.setId(Integer.toString(1));
		l.add(p1);
		l.add(p2);
		return l;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody
	MyPojoList getPojoList(){
		MyPojoList l = new MyPojoList();
		MyPojo p1 = new MyPojo();
		p1.setName("hagai");
		p1.setId("10");

		MyPojo p2 = new MyPojo();
		p2.setName("gil");
		p2.setId("20");
		l.add(p1);
		l.add(p2);
		return l;
	}


	@RequestMapping(value = "/pojo", method = RequestMethod.GET)
	public @ResponseBody
	MyPojo getPojo(){
		MyPojo p = new MyPojo();
		p.setName("hagai");
		p.setId("10");
		return p;
	}
}
