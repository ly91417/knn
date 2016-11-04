package kNN_select;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;

import java.util.Arrays;
/*this class is used to classify the testing set as a classifier*/
public class kNN_Classifier extends classifier{
	private int k;
	private boolean regression;
	private Instances trainSet;
	private example[] pq;
	private String[] attributeValues;
	public kNN_Classifier(int k) {
		this.k = k;
		this.pq = new example[k];
	}
	/*
	 * Constructor with integer k specify the nearest k neighbors and the
	 * Instances train_set
	 * */
	public kNN_Classifier(int k, Instances Train_set) {
		this(k);
		assert(Train_set !=null) : "the train_set is null and there is something worng with the arff"
		+ "file parsing" ;
		this.trainSet = Train_set;
		if(Train_set.classAttribute().name().equals("response")) {
			this.regression = true;
		}else if (Train_set.classAttribute().name().equals("class")) {
			this.regression = false;
		}
		int numAttributes = trainSet.classAttribute().numValues();
		this.attributeValues = new String[numAttributes];

		for (int i = 0; i < numAttributes-1; i++) {
			attributeValues[i] = trainSet.attribute(i).name();
		}
	}
	/*getter of train_set*/
	public Instances getTrainSet () {
		return this.trainSet;
	}
	/*get the numerical distance by using "Euclidean distance"
	 *
	 ***/
	protected double get_numerical_distance(Instance i1, Instance i2) {
		double distanceSquare = Double.MAX_VALUE;
		for(int i = 0; i < i1.numAttributes() - 1; i++){
			if(distanceSquare == Double.MAX_VALUE) {
				distanceSquare = 0;
			}
			distanceSquare += Math.pow(
					Math.abs((Double.valueOf(i1.value(i)) - Double.valueOf(i2.value(i)))
							), 2);	
		}
		assert(distanceSquare != Double.MAX_VALUE) ;
		return Math.sqrt(distanceSquare);
	}
	/*	get distance of two nominal instances
	 * 
	 * */
	protected int get_nominal_distance(Instance i1, Instance i2) {
		int score = 0;
		for (int i = 0; i < i1.numAttributes() - 1; i++) {
			double differ = (double) i1.value(i) - (double) i2.value(i);
			//System.out.println(differ);
			if(differ != 0) {
				score++;
			}
		}
		return score;
	}
	/*	get distance of two instances
	 * */
	public double distance(Instance e1, Instance e2) {
		if(regression){
			return get_numerical_distance(e1, e2);
		}
		else{
			return (double) get_nominal_distance(e1,e2);
		}
	}
	public void getKNeighbors(Instance in) {
		if(this.isRegression()){
			pq = new example[k];
			int numExample = this.trainSet.numInstances();
			for (int i =0; i < k; i++){
				Instance temp = this.trainSet.get(i);
				double distance = this.get_numerical_distance(temp,in);
				example e = new example(temp, distance);
				pq[i] = e;
			}
			for (int i = k; i < numExample; i++) {
				Arrays.sort(pq);
				Instance temp = this.trainSet.get(i);
				double distance = this.get_numerical_distance(temp,in);
				example e = new example(temp, distance);
				if(pq[k-1].getDistance() > e.getDistance()){
					pq[k-1] = e;
				}
			}
		}else {
			int numExample = this.trainSet.numInstances();
			example[] pq1 = new example[numExample];
			for(int i = 0; i < numExample; i++) {
				Instance temp = this.trainSet.get(i);
				double distance = this.get_numerical_distance(temp,in);
				example e = new example(temp, distance);
				pq1[i] = e;
			} 
			Arrays.sort(pq1);
			pq = new example[k];
			for(int i =0 ; i < k; i++ ) {
				pq[i] = pq1[i];
			}		
//			pq = new example[k];
//			int numExample = this.trainSet.numInstances();
//			for (int i = 0; i < k; i++){
//				Instance temp = this.trainSet.get(i);
//				double distance = this.get_nominal_distance(temp,in);
//				example e = new example(temp, distance);
//				pq[i] = e;
//			}
//			for (int i = k; i < numExample; i++) {
//				Arrays.sort(pq);
//				//System.out.println(pq[0].getDistance() + " " + pq[1].getDistance() + " " + pq[2].getDistance()); //+ " " + pq[3].getDistance() + " " + pq[4].getDistance());
//				Instance temp = this.trainSet.get(i);
//				double distance = this.get_nominal_distance(temp,in);
//				example e = new example(temp, distance);
//				if(pq[k-1].getDistance() > e.getDistance()){
//					pq[k-1] = e;
//				}
//			}
		}

	}
	@Override
	public double classify(Instance e) {
		// TODO Auto-generated method stub
		getKNeighbors(e);
		if(regression ) {
			return this.classifyRegression(e);
		}else {
			 int classified = this.classifyNominal(e);
			 return (double) classified;
		}
	}
	/*when classify the regression value, return the numerical average
	 * */
	private double classifyRegression(Instance e) {
		// TODO Auto-generated method stub
		double value = Double.MAX_VALUE;
		double average = 0;
		for(example ex: pq) {
			if(value == Double.MAX_VALUE) {
				value =0;
			}
			value += ex.getInstance().classValue();
		}
		average = value / (double) k;
		return average;
	}
	public boolean isRegression() {
		return regression;
	}
	public void setRegression(boolean regression) {
		this.regression = regression;
	}
	/*when classify the nominal values, return the majority vote
	 * */
	private int classifyNominal(Instance e) {
		Attribute classLabel = this.trainSet.classAttribute();
		int[] stat = new int[classLabel.numValues()];
		int max = Integer.MIN_VALUE;
		int maxIndex = Integer.MIN_VALUE;
		for(int i =0; i < pq.length; i++) {
			example ex = pq[i];
			Instance in = ex.getInstance();
			int index = (int)in.classValue();
			//System.out.println(index);
			stat[index]++;
		}
		for(int i = 0; i <classLabel.numValues(); i++) {
			if(stat[i] > max) {
				max = stat[i];
				maxIndex = i;
			}
		}
		//System.out.println(stat[0] +"  "+ stat[1] +"  "+ stat[2] + "  " + stat[3] + "  " + stat[4]+ "  "+stat[5] + "  "+ stat[6] + "  " + stat[7] + "  " + stat[8]);
		assert(maxIndex != Integer.MIN_VALUE);
		return maxIndex;
	}
}
