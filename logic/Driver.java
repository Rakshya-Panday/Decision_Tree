package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Queue;
import java.io.StringReader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import rules.AbstractNode;
import rules.CompositeNode;
import rules.ResultNode;
import rules.Rule;

public class Driver {
	static String datakey = Data.datas.keySet().iterator().next();
	static ArrayList<Rule> rules;
	static boolean verboseFlag = false;

	public static void main(String[] args) throws IOException, TransformerException {
		DataSet dataSet = new DataSet(datakey, Data.datas.get(datakey));
		System.out.println("[" + dataSet.getName() + " DATASET]\n" + dataSet);
		Driver driver = new Driver();
		//String windy = "true";
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		boolean flag = true;
		while (flag) {
			System.out.println("> What do u want to do(g:enerate rules,c:hoose dataset,d:efault mode,v:erbose mode,e:xit)");
			switch (bufferedReader.readLine()) {
			case "g":
				if (verboseFlag)
				System.out.println("information gain ==>" + dataSet.getInfoGains() + "\n");
				driver.handleGenerateRules(dataSet, bufferedReader);
				break;

			case "c":
				dataSet = driver.handleChooseDataset(dataSet, bufferedReader);
				break;
			case "d":
				verboseFlag = false;
				break;

			case "v":
				verboseFlag = true;
				break;

			case "e":
				flag = false;
				break;
			}

		}
		System.exit(0);

	}

	DataSet handleChooseDataset(DataSet dataSet, BufferedReader bufferedReader) throws IOException {
		System.out.println("> Choose DataSet" + Data.datas.keySet());
		String value = bufferedReader.readLine();
		if (Data.datas.keySet().contains(value)) {
			datakey = value;
			dataSet = new DataSet(datakey,Data.datas.get(datakey));
			System.out.println(dataSet);
		} else
			System.out.println("please enter the valid dataset name");
		return dataSet;

	}

	void handleGenerateRules(DataSet dataSet, BufferedReader bufferedReader) throws IOException, TransformerException {
		rules = new ArrayList<Rule>();
		generateRules(dataSet, null, null, new CompositeNode("dataset", datakey, null));
		handleGenerateXMLDecisionTree(dataSet, bufferedReader);
		rules.forEach(x -> System.out
				.println(Arrays.toString(x.getbranchNode().toArray()) + " ==> " + x.getResultNode().toString()));
		while (true) {
			System.out.println("\n> To decide enter key = value pairs separated w /commas,e:lse");
			ArrayList<String> keyValuePairs = new ArrayList<String>();
			String[] splitEntry = bufferedReader.readLine().split(",");
			if (!splitEntry[0].equals("e"))
				for (int i = 0; i < splitEntry.length; i++)
					keyValuePairs.add(splitEntry[i].trim());
			else
				break;
			System.out.println("decision ==>" + decide(keyValuePairs));
		}
	}

	String decide(ArrayList<String> entry) {
		String decision = "N/A";
		for (int i = 0; i < rules.size(); i++) {
			if (rules.get(i).getNameValuePairs().containsAll(entry)
					&& rules.get(i).getNameValuePairs().size() == entry.size())
				decision = rules.get(i).getResultNode().getName() + "=" + rules.get(i).getResultNode().getValue();
			else if (entry.containsAll(rules.get(i).getNameValuePairs()))
				decision = rules.get(i).getResultNode().getName() + "=" + rules.get(i).getResultNode().getValue();
		}
		return decision;
	}

	void generateRules(DataSet dataSet, String featureName, String featureValueName, CompositeNode compositeNode) {
		if (dataSet.getEntropy() != 0) {
			HashMap<String, DataSet> dataSets = new HashMap<String, DataSet>();
			dataSet.getSplitOnFeature().getFeatureValues().forEach(featureValue -> dataSets.put(featureValue.getName(),
					dataSet.createDataSet(dataSet.getSplitOnFeature(), featureValue)));
			dataSets.keySet().forEach(ds -> {
				if (dataSets.get(ds).getEntropy() != 0) {

					if (verboseFlag) {
						System.out.print("[" + dataSets.get(ds).getName() + "]:\n" + dataSets.get(ds));
						System.out.println("information gains ==>" + dataSets.get(ds).getInfoGains() + "\n");
					}
					CompositeNode childCompositeNode = new CompositeNode(dataSet.getSplitOnFeature().getName(), ds,
							compositeNode);
					compositeNode.getChilds().add(childCompositeNode);
					generateRules(dataSets.get(ds), dataSet.getSplitOnFeature().getName(), ds, childCompositeNode);

				} else {
					if (verboseFlag)
						System.out.print("[" + dataSets.get(ds).getName() + "]:\n" + dataSets.get(ds) + "\n");
					generateRules(dataSets.get(ds), dataSet.getSplitOnFeature().getName(), ds, compositeNode);

				}
			});
		} else if (featureName != null && featureValueName != null) {
			CompositeNode childCompositeNode = new CompositeNode(featureName, featureValueName, compositeNode);
			compositeNode.getChilds().add(childCompositeNode);
			ResultNode resultNode = new ResultNode(dataSet.getData()[0][dataSet.getData()[0].length - 1],
					dataSet.getData()[1][dataSet.getData()[0].length - 1], childCompositeNode);
			childCompositeNode.getChilds().add(resultNode);
			rules.add(generateRule(resultNode));

		}
	}

	Rule generateRule(ResultNode resultNode) {
		Queue<AbstractNode> branchNodes = Collections.asLifoQueue(new ArrayDeque<AbstractNode>());
		AbstractNode parent = resultNode.getParent();
		while (!parent.getName().equals("dataset")) {
			branchNodes.add(parent);
			parent = parent.getParent();
		}
		return new Rule(branchNodes, resultNode);
	}

	void handleGenerateXMLDecisionTree(DataSet dataSet, BufferedReader bufferefReader) throws TransformerException {
		StringBuffer decisionTreeXML = new StringBuffer();
		decisionTreeXML.append("<"+ datakey + ">");
		generateXMLDT(dataSet, null, null, decisionTreeXML);
		decisionTreeXML.append("</" + datakey + ">");
		System.out.println(transformXML(4, decisionTreeXML.toString()));
	}

	public StringBuffer generateXMLDT(DataSet dataSet, String featureName, String featureValueName,StringBuffer dtXML) {
		if (dataSet.getEntropy() != 0) {
			HashMap<String, DataSet> featureDataSets = new HashMap<String, DataSet>();
			dataSet.getSplitOnFeature().getFeatureValues().forEach(featureValue -> featureDataSets.put(featureValue.getName(), dataSet.createDataSet(dataSet.getSplitOnFeature(), featureValue)));
			featureDataSets.keySet().forEach(featureDataSet -> {
				if (featureDataSets.get(featureDataSet).getEntropy() != 0) {
					dtXML.append("<" + dataSet.getSplitOnFeature() + " value = \"" + featureDataSet + "\">");
					generateXMLDT(featureDataSets.get(featureDataSet), dataSet.getSplitOnFeature().getName(),featureDataSet, dtXML);
					dtXML.append("</" + dataSet.getSplitOnFeature() + ">");

				} else generateXMLDT(featureDataSets.get(featureDataSet), dataSet.getSplitOnFeature().getName(),featureDataSet, dtXML);

			});
		} else {
			String decision = dataSet.getData()[0][dataSet.getData()[0].length - 1] + "="
					+ dataSet.getData()[1][dataSet.getData()[0].length - 1];
			if (featureName != null)
				dtXML.append("<" + featureName + " value=\"" + featureValueName + "\">");
			dtXML.append(decision.trim());
			if (featureName != null)
				dtXML.append("</" + featureName + ">");

		}
		return dtXML;
	}

	public static String transformXML(int indentation, String rawXML) throws TransformerException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", indentation);
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StreamResult streamResult = new StreamResult(new StringWriter());
		transformer.transform(new StreamSource(new StringReader(rawXML)), streamResult);
		return streamResult.getWriter().toString();

	}
}
