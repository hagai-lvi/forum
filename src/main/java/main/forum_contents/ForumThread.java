package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import data_structures.Tree;
import main.Persistancy.PersistantObject;
import main.exceptions.MessageNotFoundException;
import main.exceptions.NodeNotFoundException;
import main.exceptions.ThreadFinalMessageDeletedException;
import main.interfaces.MessageI;
import main.interfaces.ThreadI;

import javax.persistence.*;

/**
 * Created by hagai on 07/04/15.
 */
@Entity
public class ForumThread extends PersistantObject implements ThreadI{

    @OneToOne(targetEntity = Tree.class, cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    //@Transient
    @JsonView(NativeGuiController.class)
    private Tree messages;

    public static int GENERATED_ID;

    public ForumThread(String user, String title, String text){
        messages = new Tree(new ForumMessage(user, title, text));
//        this.Save();
    }

    public ForumThread() {
    }

    public int getMessagesCount(){
        return messages.getMessagesCount();
    }

    @Override
    public String getTitle() {
        return getRootMessage().getMessageTitle();
    }

    @Override
    public void editMessage(MessageI originalMessage, String title, String text) throws MessageNotFoundException {
        if (text == null || title == null || text == "" || title == ""){
            throw new MessageNotFoundException("invalid message data provided");
        }
        MessageI ans = messages.editNodeData(originalMessage, title, text);
        if (ans == null){
            throw new MessageNotFoundException("invalid message data provided");
        }
    }

    public Tree getMessages(){
        return messages;
    }


    @Override
    public MessageI getRootMessage() {
        return messages.getRoot().data;
    }

    @Override
    public MessageI addReply(MessageI original, String title, String text, String user) throws MessageNotFoundException {
        try {
            ForumMessage reply = new ForumMessage(user, title, text);
            messages.add(original, reply);
            original.addReply(reply);
            return reply;
        } catch (NodeNotFoundException | NullPointerException e) {
            throw new MessageNotFoundException(original.getMessageTitle());
        }
//        this.Save();
    }

    @Override
    public boolean contains(MessageI message){
        return messages.findNode(message) != null;
    }

    @Override
    public void remove(MessageI message) throws MessageNotFoundException, ThreadFinalMessageDeletedException {
        messages.remove(message);
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
