package data_structures;

import main.forum_contents.ForumMessage;
import main.interfaces.MessageI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hagai_lvi on 5/6/15.
 */
public class TreeNode<T> {
	private T data;
	private TreeNode<T> parent;

	public void setData(T data) {
		this.data = data;
	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	private List<TreeNode<T>> children;

	public TreeNode(T data, TreeNode<T> parent){
		this.data = data;
		this.parent = parent;
		this.children = new ArrayList<>();
	}

	public void addChild(T child){
		TreeNode<T> toAdd = new TreeNode<>(child, this);
		children.add(toAdd);
	}

	/**
	 * Find a node that contains the specified data. return null if non found
	 */
	public TreeNode<T> findChild(T data){

		if (this.data.equals(data)){
			return this;
		}

		TreeNode<T> res;
		for (TreeNode<T> node: children){
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

	public TreeNode<T> getParent() {
		return parent;
	}

	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	public static TreeNode getNodeTree() {
		TreeNode<MessageI> root = new TreeNode(new ForumMessage(null, null, "Root_text", "Root_title"), null);
		root.children = new ArrayList<>();
		for (int i=0 ; i<5 ; i++){
			String s = Integer.toString(i);
			TreeNode<MessageI> a = new TreeNode(new ForumMessage(null, null, s, s), root);
			a.children = new ArrayList<>();
			for (int j=0 ; j<5 ; j++){
				String s1 = s + "_" + Integer.toString(j);
				TreeNode<MessageI> b = new TreeNode(new ForumMessage(null, null, s1, s1 ), null);
				b.children = new ArrayList<>();
				for (int k=0 ; k<5 ; k++){
					String s2 = s1 + "_" + Integer.toString(k);
					TreeNode<MessageI> c = new TreeNode(new ForumMessage(null, null, s2, s2), null);
					b.children.add(c);
				}
				a.children.add(b);
			}
			root.children.add(a);
		}
		return root;
	}

}
