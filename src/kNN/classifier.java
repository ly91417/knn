package kNN;

import weka.core.Instance;
public abstract class classifier {
	//if it is moninal return the index of the class
	//if it is regression return the average value of the regression
	public abstract double classify(Instance e);
	protected abstract int get_nominal_distance(Instance i1, Instance i2);
	protected abstract double get_numerical_distance(Instance i1, Instance i2);
}