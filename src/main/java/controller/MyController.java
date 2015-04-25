package controller;

import java.util.Collection;
import main.forum_contents.Example;
import main.forum_contents.Forum;
import main.interfaces.FacadeI;
import main.interfaces.ForumI;
import main.services_layer.Facade;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Created by hagai_lvi on 4/18/15.
 */
@Controller
public class MyController {
	@RequestMapping(value = "/facade",method = RequestMethod.GET)
//	public void printWelcome(ModelMap model, HttpSession session) {
//	//public void printWelcome(Map<String,Object> model,  @ModelAttribute("forum") Forum forum) {
//		//FacadeI f = Facade.getFacade();
//		ArrayList<Example> ex = new ArrayList<>();
//
//		model.put("message", "Facade controller");
//		model.put("size", 1);//f.getForumList().size());
//
//		Example ex1 = new Example("1","1");
//		Example ex2 = new Example("2","2");
//		ex.add(ex1);
//		ex.add(ex2);
//		model.put("forumList", ex);
//		//model.put("forumList", f.getForumList());
//	}
	//public void printWelcome(ModelMap model, HttpSession session) {
	public void printWelcome(Map<String,Object> model, HttpSession session) {
		FacadeI f = Facade.getFacade();
//			model.addAttribute("message", "Facade controller");
//			model.addAttribute("size", f.getForumList().size());
//			model.addAttribute("forumList", f.getForumList());
			model.put("message", "Facade controller");
			model.put("size", f.getForumList().size());
			model.put("forumList", f.getForumList());

		for (ForumI forum : f.getForumList()){
			model.put(forum.getName(),forum);
		}
	}

	@RequestMapping(value = "/addForum",method = RequestMethod.POST)
	public void add(ModelMap model, String forumName) {
		FacadeI f = Facade.getFacade();
		f.addForum(new Forum(forumName, null));
		model.addAttribute("forumName", forumName);
	}


	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public void reallogin(Map<String,Object> model, @ModelAttribute("command") Forum forum)
	{
		model.put("forum",forum);
		//Example ex = new Example(forum.getForum_name(),forum.getForum_name());
	}

	@RequestMapping(value = "/ex1", method = RequestMethod.GET)
	public void bla(Map<String,Object> model, @ModelAttribute("Example") Example exx) {

		model.put("forum", exx);
	}

	@RequestMapping(value = "/ex2", method = RequestMethod.POST)
	public void login(Map<String,Object> model, @ModelAttribute("Example") Example exx) {

		model.put("forum",exx);
	}
}
