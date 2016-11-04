package test;

import java.util.PriorityQueue;

public class runner {
	public static void main(String[] args) {
		PriorityQueue<example> pq = new PriorityQueue<example>(6);
		example e1 = new example(2,1);
		example e2 = new example(2,2);
		example e3 = new example(2,3);
		example e4 = new example(2,4);
		example e5 = new example(2,5);
		example e6 = new example(2,6);
		pq.offer(e1);
		pq.offer(e3);
		pq.offer(e2);
		pq.offer(e4);
		pq.offer(e5);
		pq.offer(e6);
		for(int i = 0; i <6; i++) {
			System.out.println(pq.poll());
		}	
	}
}
