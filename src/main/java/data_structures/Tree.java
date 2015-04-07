package data_structures;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic tree implementation
 * @param <T>
 */
public class Tree<T> {
    private Node<T> root;

    public Tree(T rootData) {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
    }

    public void add(T dataToAdd, T ancestor){
        //TODO
        throw new RuntimeException("not yet implemented");
    }


    public static class Node<T> {
        private T data;
        private Node<T> parent;
        private List<Node<T>> children;
    }
}