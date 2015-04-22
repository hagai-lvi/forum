package data_structures;

import main.exceptions.NodeNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic tree implementation
 * @param <T>
 */
public class Tree<T> {
    // TODO handle the case in which the root is null (might happen after calling remove())
    private Node<T> root;

    public Tree(T rootData) {
        root = new Node<>(rootData, null);
    }

    public void add(T dataToAdd, T ancestor) throws NodeNotFoundException {
        Node<T> ancestorNode = root.findChild(ancestor);
        if (ancestorNode == null){
            throw new NodeNotFoundException("Could not find ancestor " + ancestor.toString() + " in the tree");
        }
        Node<T> nodeToAdd = new Node<>(dataToAdd, ancestorNode);
        ancestorNode.addChild(dataToAdd);
    }

    public T getRoot() {
        return root.getData();
    }

    public T findNode(T data) {
        if (root == null){
            return null;
        }

        Node<T> node = root.findChild(data);
        if (node == null){
            return null;
        }
        else{
            return node.data;
        }
    }

    public void remove(T data) {
        Node<T> child = root.findChild(data);
        if (child == root){
            root = null;
        }
        else {
            child.parent.children.remove(child);
        }
    }


    private static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;

        public Node(T data, Node<T> parent){
            this.data = data;
            this.parent = parent;
            this.children = new ArrayList<>();
        }

        public void addChild(T child){
            Node<T> toAdd = new Node<>(child, this);
            children.add(toAdd);
        }

        /**
         * Find a node that contains the specified data. return null if non found
         */
        public Node<T> findChild(T data){

            if (this.data.equals(data)){
                return this;
            }

            Node<T> res;
            for (Node<T> node: children){
                res = node.findChild(data);
                if (res != null){
                    return res;
                }
            }

            return null;
        }

        public T getData() {
            return data;
        }
    }
}