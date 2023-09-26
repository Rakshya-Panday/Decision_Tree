package logic;

public class FeatureValue {
	private String name;
	private int occurances;
	public FeatureValue(String name) {this.name = name;}
	public String getName() {return name;}
	public int getOccurances() {return occurances;}
	public void setOccurances(int occurances) {this.occurances = occurances;}
	public int hashCode() {return name.hashCode();}
	public boolean equals(Object object) {
		boolean returnValue = true;
		if(object == null || (getClass()!=object.getClass())) returnValue = false;
		if(name == null)if(((FeatureValue) object).name != null)returnValue = false;
		else if(!name.equals(((FeatureValue)object).name))returnValue = false;
		return returnValue;
	}
	public String toString() {return name;}


}
