package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.IntStream;

public class DataSet {
	
	private String name;
	private String [][]data = null;
	private double entropy = 0;
	private HashMap<Feature,Double> infoGains = new  HashMap<Feature,Double>();
	private Feature splitOnFeature = null;
	public DataSet(String name , String[][] data) {
		this.name = name;
		this.data = data;
		calculateEntropy().calculateInfoGains().findSplitOnFeature();
	}
	
	private DataSet calculateEntropy() {
		new Feature(data,data[0].length-1).getFeatureValues().stream().forEach(featureValue ->
		entropy += minusPlog2((double)featureValue.getOccurances()/(data.length-1)));
		return this;
	}
	
	private DataSet calculateInfoGains() {
		IntStream.range(0,data[0].length-1).forEach(column ->{
			Feature feature = new Feature(data,column);
			ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
			feature.getFeatureValues().stream().forEach(featureValue ->
			dataSets.add(createDataSet(feature,featureValue)));
			double summation =0;
			for(int i =0;i<dataSets.size();i++)
				summation +=((double)(dataSets.get(i).getData().length-1)/(data.length-1))*dataSets.get(i).getEntropy();
			infoGains.put(feature , entropy - summation);
		});
		return this;
	}
	
	private DataSet findSplitOnFeature() {
		Iterator<Feature> iterator = infoGains.keySet().iterator();
		while(iterator.hasNext()) {
			Feature feature = iterator.next();
			if(splitOnFeature == null || infoGains.get(splitOnFeature)<infoGains.get(feature)) splitOnFeature = feature;
			}
		return this;
	}
	
	public DataSet createDataSet(Feature feature,FeatureValue featureValue) {
		int column = getColNumb(feature.getName());
		String[][] returnData = new String[featureValue.getOccurances()+1][data[0].length];
		returnData[0] = data[0];
		int counter  = 1;
		for(int row =1;row<data.length;row++ )
			if(data[row][column].equals(featureValue.getName())) returnData[counter++]= data[row];
		return new DataSet(feature.getName()+ "-"+featureValue.getName(),deleteColumns(returnData,column));
	}
	
	private double minusPlog2(double p) {
		double returnValue = 0;
		if(p != 0) returnValue = (-1)*p*Math.log(p)/Math.log(2);
		return returnValue;
	}
	
	private String [][] deleteColumns(String [][] data,int toDeleteColumn){
		String returnData[][] = new String[data.length][data[0].length-1];
		IntStream.range(0, data.length).forEach(row->{
			int columnCounter = 0;
			for(int column =0; column< data[0].length;column++)
			if( column != toDeleteColumn) returnData[row][columnCounter++] = data[row][column];
		});
					return returnData;
	}
	
	public int getColNumb(String colName) {
		int returnValue = -1;
		for(int column = 0; column < data[0].length-1;column++)
			if(data[0][column] ==  colName) {returnValue = column; break;}
		    return returnValue;
	}
	
	
	public String getName() {return name;}
	public String[][]getData() {return data;}
	public double getEntropy() {return entropy;}
	public HashMap<Feature,Double> getInfoGains(){return infoGains;} 
	public Feature getSplitOnFeature() {return  splitOnFeature;}
	public String toString() {
		StringBuffer  stringBuffer = new StringBuffer();
		for(int row = 0;row<data.length;row++) {
			for (int column =0; column < data[row].length; column++) {
				stringBuffer.append(data[row][column]);
				IntStream.range(0, 24-data[row][column].length()).forEach(i->stringBuffer.append(""));
			}
			stringBuffer.append("\n");
			if(row == 0) {
				IntStream.range(0,108).forEach(i->stringBuffer.append("-"));
				stringBuffer.append("\n");
				
			}
		}
		return stringBuffer.toString();
	}

}
