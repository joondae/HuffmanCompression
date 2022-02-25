//NOTE: can be a HuffmanNode, "summed node", or "zero node"
	//HuffmanNode: a character w/ # of frequencies and lt and rt
	//"summed node": only frequencies and lt and rt
	//"zero node": only a character and lt and rt
public class HuffmanNode implements Comparable<HuffmanNode> {
	int character;
	int data;
	HuffmanNode left;
	HuffmanNode right;
	
	public HuffmanNode(int ch, int d, HuffmanNode lt, HuffmanNode rt) {
		this.character = ch;
		this.data = d;
		this.left = lt;
		this.right = rt;
	}
	
	public int compareTo(HuffmanNode other) {
		//return other.data - this.data;
		return this.data - other.data;
	}
	
	public String toString() {
		return "c: " + (char) this.character + "\td: " + this.data + "\tlt: " + this.left + "\trt: " + this.right + "\n";
	}
}