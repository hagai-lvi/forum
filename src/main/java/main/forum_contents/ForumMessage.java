package main.forum_contents;

import main.exceptions.PermissionDeniedException;
import main.interfaces.MessageI;
import main.interfaces.UserI;
import main.User.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by hagai_lvi on 4/11/15.
 */
@Entity
public class ForumMessage implements MessageI {

	@OneToOne(targetEntity = ForumMessage.class)
	private MessageI reply_message;
	@OneToOne(targetEntity = User.class)
	private UserI writingUser;
	private String messageText;
	private String messageTitle;
	private Date writingTime;
	@OneToMany(targetEntity = ForumMessage.class, cascade = CascadeType.ALL)
	private List<MessageI> replays;
	private boolean isDeleted = false;


	public ForumMessage(MessageI reply_to, UserI user, String messageText, String messageTitle){
		this.writingUser = user;
		this.messageText = messageText;
		this.reply_message = reply_to;
		this.messageTitle = messageTitle;
		writingTime = new Date();
		replays = new ArrayList<>();
	}

	public ForumMessage() {
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


	@Id
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
