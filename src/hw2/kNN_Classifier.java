package hw2;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Attribute;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.PriorityQueue;
import java.math.*;
/*this class is used to classify the testing set as a classifier*/
public class kNN_Classifier extends classifier{
	private int k;
	private boolean regression;
	private Instances trainSet;
	private PriorityQueue<example> pq;
	private String[] attributeValues;
	public kNN_Classifier(int k) {
		this.k = k;
		this.pq = new PriorityQueue<example>(k,Collections.reverseOrder());
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
		for(int i = 0; i < i1.numAttributes()-1; i++){
			if(distanceSquare == Double.MAX_VALUE) {
				distanceSquare = 0;
			}
			distanceSquare += Math.pow(
								
										(Double.valueOf(i1.value(i)) - Double.valueOf(i2.value(i)))
										, 2);	
		}
		assert(distanceSquare != Double.MAX_VALUE) ;
		return Math.sqrt(distanceSquare);
	}
	/*	get distance of two nominal instances
	 * 
	 * */
	protected int get_nominal_distance(Instance i1, Instance i2) {
		int difference = Integer.MAX_VALUE;
		for (int i = 0; i < i1.numAttributes()-1; i++) {
			if(difference == Integer.MAX_VALUE) {
				difference = 0;
			}
			double differ = Double.valueOf(i1.value(i)) - Double.valueOf(i2.value(i));
			if(differ == 0) {
				continue;
			}
			difference++;
		}
		assert(difference >= 0);
		return difference;
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
		if(regression){
			int numExample = this.trainSet.numInstances();
			for (int i = 0; i < numExample; i++ ) {
				Instance temp = this.trainSet.get(i);
				double distance = this.get_numerical_distance(in, temp);
				example e = new example(temp, distance);
				if(pq.peek() == null){
					pq.offer(e);
				}else {
					example temp1 = pq.peek();
					if(distance <  temp1.getDistance()) {
						pq.offer(e);
					}
					if(pq.size() > k) {
						//TODO to verify the remove function work or not
						pq.poll();
					}
				}
				
			}
		}else {
			int numExample = this.trainSet.numInstances();
			for (int i = 0; i < numExample; i++ ) {
				Instance temp = this.trainSet.get(i);
				double distance = this.get_nominal_distance(in, temp);
				example e = new example(temp, distance);
				if(pq.peek() == null){
					pq.offer(e);
				}else {
					example temp1 = pq.peek();
					if(distance <  temp1.getDistance()) {
						pq.offer(e);
					}
					if(pq.size() > k) {
						//TODO to verify the remove function work or not
						pq.poll();
					}
				}
			}
		}
	}
	@Override
	public double classify(Instance e) {
		// TODO Auto-generated method stub
		if(regression ) {
			pq = new PriorityQueue<example>(k);
			getKNeighbors(e);
			return this.classifyRegression(e);
		}else {
			pq = new PriorityQueue<example>(k);
			getKNeighbors(e);
			return (double) this.classifyNominal(e);
		}
		
	}
	/*when classify the regression value, return the numerical average
	 * */
	private double classifyRegression(Instance e) {
		// TODO Auto-generated method stub
		double value = Double.MAX_VALUE;
		int numOfElements = pq.size();
		while(pq.peek() != null) {
			if(value == Double.MAX_VALUE) {
				value = 0;
			}
			example ex = pq.poll();
			value +=ex.getInstance().value(ex.getInstance().classAttribute()); 
		}
		assert(value != Double.MAX_VALUE);
		return value/(double)numOfElements;
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
		while(pq.peek() != null) {
			example in = pq.poll();
			int classValue = (int) in.getInstance().classValue();
			int vote = stat[classValue]++;
			stat[classValue] = vote;
			if (vote > max) {
				max = vote;
				maxIndex = classValue;
			}
		}
		assert(maxIndex != Integer.MIN_VALUE);
		return maxIndex;
	}
}
