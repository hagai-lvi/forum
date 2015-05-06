package data_structures;

import main.exceptions.NodeNotFoundException;

/**
 * A basic tree implementation
 * @param <T>
 */
public class Tree<T> {
    // TODO handle the case in which the root is null (might happen after calling remove())
    private TreeNode<T> root;

    public Tree(T rootData) {
        root = new TreeNode<>(rootData, null);
    }

    public void add(T dataToAdd, T ancestor) throws NodeNotFoundException {
        TreeNode<T> ancestorNode = root.findChild(ancestor);
        if (ancestorNode == null){
            throw new NodeNotFoundException("Could not find ancestor " + ancestor.toString() + " in the tree");
        }
        TreeNode<T> nodeToAdd = new TreeNode<>(dataToAdd, ancestorNode);
        ancestorNode.addChild(dataToAdd);
    }

    public T getRoot() {
        return root.getData();
    }

    public TreeNode<T> getRootNode() {
        return root;
    }

    public T findNode(T data) {
        if (root == null){
            return null;
        }

        TreeNode<T> node = root.findChild(data);
        if (node == null){
            return null;
        }
        else{
            return node.getData();
        }
    }

    public void remove(T data) {
        TreeNode<T> child = root.findChild(data);
        if (child == root){
            root = null;
        }
        else {
            child.getParent().getChildren().remove(child);
        }
    }

}