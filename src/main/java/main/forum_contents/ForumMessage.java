package main.forum_contents;

import main.interfaces.MessageI;
import main.interfaces.UserI;

/**
 * Created by hagai_lvi on 4/11/15.
 */
public class ForumMessage implements MessageI {
	@Override
	public UserI getUser() {
		return null;
	}

	@Override
	public void reply(MessageI reply) {

	}
}
