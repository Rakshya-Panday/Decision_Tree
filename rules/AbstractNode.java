package rules;

public abstract class AbstractNode {
	protected AbstractNode parent;
	protected String name,value;
	public AbstractNode getParent() {return parent;}
	public String getName() { return name;}
	public String getValue() { return value;}
	public String getNameValuePair() { return name + "="+value;}
	
	

}
