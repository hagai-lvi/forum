package controller;

import com.fasterxml.jackson.annotation.JsonView;
import gui_objects.ForumList;
import gui_objects.SubForumList;
import main.exceptions.EmailNotAuthanticatedException;
import main.exceptions.InvalidUserCredentialsException;
import main.exceptions.NeedMoreAuthParametersException;
import main.exceptions.PasswordNotInEffectException;
import main.interfaces.FacadeI;
import main.interfaces.SubForumI;
import main.services_layer.Facade;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Created by hagai_lvi on 6/2/15.
 * A rest controller to work with the native GUI
 */
@RestController
@RequestMapping("/gui")
public class NativeGuiController {
	private Logger logger = Logger.getLogger(this.getClass());


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

	@JsonView(NativeGuiController.class)
	@RequestMapping(value = "/forum/{forumID}", method = RequestMethod.GET)
	public @ResponseBody
	SubForumList getSubForums(@PathVariable String forumID) throws PasswordNotInEffectException, NeedMoreAuthParametersException, InvalidUserCredentialsException, EmailNotAuthanticatedException {
		logger.info("got request getSubForums");
		SubForumList list = new SubForumList();
		FacadeI facade = Facade.getFacade();
		int login = facade.login(forumID, "ADMIN", "ADMIN");//TODO get credentials from the user
		Collection<SubForumI> subForumList = facade.getSubForumList(login);

		list.addAll(subForumList);
		return list;
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
