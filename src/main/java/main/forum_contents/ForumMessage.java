package main.forum_contents;

import main.interfaces.MessageI;
import main.interfaces.UserI;
import sun.plugin2.message.Message;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public class ForumMessage implements MessageI {

	UserI writing_user;
	String message_text;
	Date writing_time;
	ArrayList<MessageI> replays;
	boolean isDeleted = false;


	public ForumMessage(UserI user, String message_text){
		this.writing_user = user;
		this.message_text = message_text;
		writing_time = new Date();
		replays = new ArrayList<MessageI>();
	}

	@Override
	public UserI getUser() {
		return writing_user;
	}
	public Date getDate()  { return writing_time; }

	@Override
	public void reply(MessageI reply) {
		this.replays.add(reply);
	}

	public String printSubTree(int depth){
		if (isDeleted){
			return "";
		}
		String ans = "";
		for (int i = 0; i < depth ; i++){
			ans += "	";
		}
		ans = "--> " + this.message_text;
		for (MessageI m: replays){
			ans += "\n " + m.printSubTree(depth+1);
		}
		return ans;
	}

	public void removeMessage(){
		this.isDeleted = true;
	}
}
