package test;

public class example implements Comparable<example>{
	private int num;
	private double distance;
	public example (double d, int num){
		this.distance =d;
		this.num = num;
	}
	public int compareTo(example o) {
		// TODO Auto-generated method stub
		if(this.distance < o.distance) return 1;
		else if(this.distance == o.distance) { return 0;
		}
		else {
			return -1;
		}
	}
	public String toString() {
		return "num " + num + " distance:  "+ (int) distance;
	}
}