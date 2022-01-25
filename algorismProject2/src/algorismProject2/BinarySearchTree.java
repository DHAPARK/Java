package algorismProject2;

public class BinarySearchTree {
	public static int cnt;
	public static int c_cnt;
	class Node {
		int data;
		String pdName;
		Node left;
		Node right;
		Node(int data,String pdName) {
			this.data = data;
			if(pdName==null) {
				pdName = "";
			}
			this.pdName = pdName;
		}
	}

	public Node root;

	public Node Search(Node node, int key) {
			
		if (node == null && c_cnt == 0) {
			System.out.println("등록된 상품이 없습니다.");
			c_cnt+=1;
			return null;
		}
		
		if (node ==null && c_cnt >0) {
			return null;
		}

		if (key == node.data) {
			return node;
		} else if (key < node.data) {
			return Search(node.left, key);
		} else {
			return Search(node.right, key);
		}
	}

	public Node rSearch(Node node, int key) {
		while (node != null) {
			if (key == node.data) {
				return node;
			} else if (key < node.data) {
				node = node.left;
			} else {
				node = node.right;
			}
		}

		return null; 
	}

	public Node insert(Node node, int key, String pdName) {
		if (node == null) {
			return new Node(key,pdName); 
		}
		if (key < node.data) {
			node.left = insert(node.left, key,pdName);
		} else if (key > node.data) {
			node.right = insert(node.right, key,pdName);
		}
		this.cnt++;
		return node;
	}

	public Node searchminv(Node node) {
		Node currentNode = node;

		while (currentNode.left != null) {
			currentNode = currentNode.left; 
		}
		return currentNode;
	}

	
	// 노드 삭제
	public Node delete(Node root, int key) {
		if (root == null) {
			return root;
		}

		if (key < root.data) { 
			root.left = delete(root.left, key);
		} else if (key > root.data) { 
			root.right = delete(root.right, key);
		} else {

			if (root.left == null) {
				return root.right;
			} else if (root.right == null) {
				return root.left;
			}

			Node temp = searchminv(root.right); 
			root.data = temp.data;
			root.right = delete(root.right, temp.data); 
		}
		return root;
	}
	public void range(Node node) {
		if (node != null) {
			if (node.left != null)
				range(node.left);
			System.out.print(node.data + " ");
			if (node.right != null)
				range(node.right);
		}
	}

	

}
