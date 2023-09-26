package rules;

import java.util.ArrayList;
import java.util.Queue;

public class Rule { 
     private ResultNode  resultNode;
     private Queue<AbstractNode> branchNodes;
     public Rule(Queue<AbstractNode> branchNodes,ResultNode resultNode) {
    	 this.branchNodes = branchNodes;
    	 this.resultNode = resultNode;
     }
     
     public ResultNode getResultNode() {return resultNode;}
     public Queue<AbstractNode> getbranchNode(){return branchNodes;}
     
     public ArrayList<String> getNameValuePairs(){
    	 ArrayList<String> nameValuePairs = new ArrayList<String>();
    	 branchNodes.forEach(x->nameValuePairs.add(x.getNameValuePair()));
    	 return nameValuePairs;
     }
     
}
