package controller;

import main.forum_contents.Forum;
import main.interfaces.FacadeI;
import main.services_layer.Facade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by hagai_lvi on 4/18/15.
 */
@Controller
//@RequestMapping("/facade")
public class MyController {
	@RequestMapping(value = "/facade",method = RequestMethod.GET)
	public void printWelcome(ModelMap model, HttpSession session) {
		FacadeI f = Facade.getFacade();
		if (session.getAttribute("set") == null){
			model.addAttribute("message", "Facade controller, adding forum");
			model.addAttribute("forum", "empty");
			f.addForum(new Forum("Hi", null));
			f.addForum(new Forum("World", null));
			f.addForum(new Forum("Sweatshop", null));
			session.setAttribute("set", new Date(System.currentTimeMillis()).toString());
			session.setAttribute("Facade", f);
		}
		else{
			f = (FacadeI) session.getAttribute("Facade");
			model.addAttribute("message", session.getAttribute("set"));
			model.addAttribute("forum", f.getForumList().iterator().next().getName());
			model.addAttribute("size", f.getForumList().size());
			model.addAttribute("forumList", f.getForumList().iterator());
		}
	}


}
