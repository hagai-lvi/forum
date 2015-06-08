package main.forum_contents;

import com.fasterxml.jackson.annotation.JsonView;
import controller.NativeGuiController;
import data_structures.Tree;
import main.Persistancy.PersistantObject;
import main.exceptions.MessageNotFoundException;
import main.exceptions.NodeNotFoundException;
import main.interfaces.MessageI;
import main.interfaces.ThreadI;

import javax.persistence.*;
import javax.validation.constraints.Null;

/**
 * Created by hagai on 07/04/15.
 */
@Entity
public class ForumThread extends PersistantObject implements ThreadI{

    @OneToOne(targetEntity = Tree.class, cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    //@Transient
    @JsonView(NativeGuiController.class)
    private Tree messages;


    public ForumThread(MessageI initialMessage){
        messages = new Tree(initialMessage);
//        this.Save();
    }

    public ForumThread() {
    }

    @Override
    public String getTitle() {
        return getRootMessage().getMessageTitle();
    }

    public Tree getMessages(){
        return messages;
    }


    @Override
    public MessageI getRootMessage() {
        return messages.getRoot().data;
    }

    public void printThread(){
        throw new RuntimeException("Not yet implemented");
//        System.out.println(threadTree.printSubTree(0));
    }

    @Override
    public void addReply(MessageI reply, MessageI original) throws MessageNotFoundException {
        try {
            messages.add((ForumMessage)reply, (ForumMessage)original);
            original.addReply(reply);
        } catch (NodeNotFoundException | NullPointerException e) {
            throw new MessageNotFoundException(original, null);
        }
//        this.Save();
    }

    @Override
    public boolean contains(MessageI message){
        return messages.findNode((ForumMessage)message) != null;
    }

    @Override
    public void remove(MessageI message) {
        messages.remove((ForumMessage)message);
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
