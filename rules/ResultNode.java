package rules;

public class ResultNode extends AbstractNode {
	public ResultNode(String name,String value ,AbstractNode parent) {
		this.name = name;
		this.value = value;
		this.parent = parent;
	}
	public String toString() {return name + "="+value; }

}
