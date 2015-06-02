package data_structures;

import main.exceptions.NodeNotFoundException;
import main.forum_contents.ForumMessage;
import main.interfaces.MessageI;

import javax.persistence.*;

/**
 * A basic tree implementation
 * @param <T>
 */
@Entity
public class Tree{
    // TODO handle the case in which the root is null (might happen after calling remove())

    @OneToOne(targetEntity = Node.class, cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Node root;


    public Tree(MessageI rootData) {
        root = new Node((ForumMessage)rootData, null);
    }

    public Tree() {
    }

    public void add(ForumMessage dataToAdd, ForumMessage ancestor) throws NodeNotFoundException {
        Node ancestorNode = root.findChild(ancestor);
        if (ancestorNode == null){
            throw new NodeNotFoundException("Could not find ancestor " + ancestor.toString() + " in the tree");
        }
        Node nodeToAdd = new Node(dataToAdd, ancestorNode);
        ancestorNode.addChild(dataToAdd);
    }

    public ForumMessage getRoot() {
        return root.getData();
    }

    public ForumMessage findNode(ForumMessage data) {
        if (root == null){
            return null;
        }

        Node node = root.findChild(data);
        if (node == null){
            return null;
        }
        else{
            return node.data;
        }
    }

    public void remove(ForumMessage data) {
        Node child = root.findChild(data);
        if (child == root){
            root = null;
        }
        else {
            child.parent.children.remove(child);
        }
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