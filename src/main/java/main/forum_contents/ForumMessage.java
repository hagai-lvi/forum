package main.forum_contents;

import main.exceptions.PermissionDeniedException;
import main.interfaces.MessageI;
import main.interfaces.UserI;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by hagai_lvi on 4/11/15.
 */
public class ForumMessage implements MessageI {

	private MessageI reply_message;
	private final UserI writingUser;
	private String messageText;
	private final String messageTitle;
	private final Date writingTime;
	private ArrayList<MessageI> replays;
	private boolean isDeleted = false;


	public ForumMessage(MessageI reply_to, UserI user, String messageText, String messageTitle){
		this.writingUser = user;
		this.messageText = messageText;
		this.reply_message = reply_to;
		this.messageTitle = messageTitle;
		writingTime = new Date();
		replays = new ArrayList<>();
	}

	public void editText(UserI user, String newText) throws PermissionDeniedException {
		if (user != this.writingUser){
			throw new PermissionDeniedException("User can't edit message");
		}
		this.messageText = newText;
	}


	@Override
	public UserI getUser() {
		return writingUser;
	}
	public Date getDate()  { return writingTime; }

	public MessageI get_replayed_message(){
		return reply_message;
	}

	@Override
	public String printSubTree(int depth){
		if (isDeleted){
			return "";
		}
		String ans = "";
		for (int i = 0; i < depth ; i++){
			ans += "	";
		}
		ans = "--> " + this.messageText;
		for (MessageI m: replays){
			ans += "\n " + m.printSubTree(depth+1);
		}
		return ans;
	}

	@Override
	public void removeMessage(){
		this.isDeleted = true;
	}

	@Override
	public String getMessageText(){

		return messageText;
	}

	@Override
	public String getMessageTitle(){
		return messageTitle;
	}


}
