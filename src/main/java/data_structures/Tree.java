package data_structures;

import main.Persistancy.PersistantObject;
import main.exceptions.NodeNotFoundException;
import main.forum_contents.ForumMessage;
import main.interfaces.MessageI;

import javax.persistence.*;

/**
 * A basic tree implementation
 */
@Entity
public class Tree extends PersistantObject{
    // TODO handle the case in which the root is null (might happen after calling remove())

    @OneToOne(targetEntity = Node.class, cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Node root;


    public Tree(MessageI rootData) {
        root = new Node((ForumMessage)rootData, null);
//        this.Save();
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
//        this.Save();
    }

    public Node getRoot() {
        return root;
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

    public MessageI find(int id){
        return find(root, id);
    }
    private MessageI find(Node node, int id) {
        if (node.data.getId() == id) {
            return node.data;
        }
        else{
            for (Node child : root.children){
                MessageI msg = find(child, id);
                if (msg != null) {
                    return msg;
                }
            }
            return null;
        }
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