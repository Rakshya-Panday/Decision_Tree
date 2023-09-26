package rules;

import java.util.ArrayList;

public class CompositeNode extends AbstractNode {
	private ArrayList<AbstractNode> childs = new ArrayList<AbstractNode>();
	public CompositeNode(String name,String value ,AbstractNode parent) {
		this.name = name;
		this.value = value;
		this.parent = parent;
	}
	public String toString() {return name + "="+value; }
	public ArrayList<AbstractNode> getChilds(){return childs;}

}
