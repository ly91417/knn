package kNN;
import java.util.Comparator;
public class ExampleComparator implements Comparator<example>{

	public int compare(example o1, example o2) {
		// TODO Auto-generated method stub
		return (int)o2.getDistance() - (int)o1.getDistance() ;
	}
}
