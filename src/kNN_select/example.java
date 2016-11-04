package kNN_select;
import weka.core.Instance;
public class example implements Comparable<example>{
	private Instance instance;
	private double distance;
	public example(Instance in){
		this.instance = in;
	}
	public example(Instance in, double distance) {
		this.instance = in;
		this.distance = distance;
	}
	public Instance getInstance() {
		return this.instance;
	}
	public double getDistance() {
		return this.distance;
	}
	public void setInstance(Instance in) {
		this.instance = in;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public int compareTo(example o) {
		// TODO Auto-generated method stub
		if(this.distance > o.distance) return 1;
		else if(this.distance == o.distance) { return 0;
		}
		else {
			return -1;
		}
	}
}