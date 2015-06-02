package controller;

import com.fasterxml.jackson.annotation.JsonView;
import gui_objects.ForumList;
import main.interfaces.FacadeI;
import main.services_layer.Facade;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hagai_lvi on 6/2/15.
 */
@RestController
@RequestMapping("/gui")
public class NativeGuiController {

	@RequestMapping(value = "/pojo", method = RequestMethod.GET)
	public @ResponseBody
	MyPojo getPojo(){
		MyPojo p = new MyPojo();
		p.setName("hagai");
		p.setId("10");
		return p;
	}


	@JsonView(NativeGuiController.class)
	@RequestMapping(value = "/facade", method = RequestMethod.GET)
	public @ResponseBody
	ForumList getFacade(){
		ForumList list = new ForumList();
		FacadeI facade = Facade.getFacade();
		list.addAll(facade.getForumList());
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
}
