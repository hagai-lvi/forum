package main.forum_contents;

import data_structures.Tree;
import main.exceptions.MessageNotFoundException;
import main.exceptions.NodeNotFoundException;
import main.interfaces.MessageI;
import main.interfaces.ThreadI;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hagai on 07/04/15.
 */
@Entity
public class ForumThread implements ThreadI{
    private final static AtomicInteger ID_GENERATOR = new AtomicInteger();

    @Override
    public Tree<MessageI> getMessages() {
        return messages;
    }

    @Transient //TODO : hagai, It's a mess to persist this kind of tree, we could use default implementation which is supported by hibernate
    private Tree<MessageI> messages;

    @Id
    private long id;


    public ForumThread(MessageI initialMessage){
        messages = new Tree<>(initialMessage);
		id = ID_GENERATOR.incrementAndGet();

    }

    public ForumThread() {
    }


    @Override
    public MessageI getRootMessage() {
        return messages.getRoot();
    }

    public void printThread(){
        throw new RuntimeException("Not yet implemented");
//        System.out.println(threadTree.printSubTree(0));
    }

    @Override
    public void addReply(MessageI reply, MessageI original) throws MessageNotFoundException {
        try {
            messages.add(reply, original);
            original.addReply(reply);
        } catch (NodeNotFoundException e) {
            //TODO fix the exception
            throw new MessageNotFoundException(original, null);
        }
    }

    @Override
    public boolean contains(MessageI message){
        return messages.findNode(message) != null;
    }

    @Override
    public void remove(MessageI message) {
        messages.remove(message);
    }

    @Override
    public long getID() {
        return this.id;
    }



    public void setId(long id) {
        this.id = id;
    }
}
