package data_structures;

import main.forum_contents.ForumMessage;
import org.hibernate.annotations.Type;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xkcd on 6/2/2015.
 */
@Entity
public class Node{


    @OneToOne(targetEntity = ForumMessage.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    //@Transient
    public ForumMessage data;

    @ManyToOne(targetEntity = Node.class,  cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    //@Transient
    public Node parent;

    @OneToMany(targetEntity = Node.class,  cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    //@Transient
    public List<Node> children;

    public Node(ForumMessage data, Node parent) {
        this.data = data;
        this.parent = parent;
        this.children = new ArrayList<>();
    }

    public Node() {
    }

    public void addChild(ForumMessage child) {
        Node toAdd = new Node(child, this);
        children.add(toAdd);
    }

    /**
     * Find a node that contains the specified data. return null if non found
     */
    public Node findChild(ForumMessage data) {

        if (this.data.equals(data)) {
            return this;
        }

        Node res;
        for (Node node : children) {
            res = node.findChild(data);
            if (res != null) {
                return res;
            }
        }

        return null;
    }

    public ForumMessage getData() {
        return data;
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
