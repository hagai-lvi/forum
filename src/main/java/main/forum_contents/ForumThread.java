package main.forum_contents;

import data_structures.Tree;
import main.interfaces.MessageI;
import main.interfaces.ThreadI;

/**
 * Created by hagai on 07/04/15.
 */
public class ForumThread implements ThreadI{
    //TODO should a thread have a subject?
    Tree<MessageI> threadTree;


    public ForumThread(MessageI initialMessage){
        threadTree = new Tree<>(initialMessage);
    }

    @Override
    public MessageI getRootMessage() {
        return threadTree.getRoot();
    }
}
