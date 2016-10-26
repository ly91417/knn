package hw2;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import java.util.ArrayList;
public class HW2 {
	public static void main(String[] args) throws Exception {
		if (args.length	!= 3)
		{
			System.out.println("usage: kNN <train-set-file> <test-set-file> k");
			pukeAndDie();
		}
		DataSource train_set_file = new DataSource(args[0]);
		DataSource test_set_file = new DataSource(args[1]);
		Instances train_set = train_set_file.getDataSet();
		Instances test_set = test_set_file.getDataSet();
		if (train_set.classIndex() == -1) {
			train_set.setClassIndex(train_set.numAttributes() - 1);	
		}
		if (test_set.classIndex() == -1) {
			test_set.setClassIndex(test_set.numAttributes() - 1);	
		}
		Integer k = Integer.valueOf(args[2]);
		kNN_Classifier kc = new kNN_Classifier(k,train_set);
		int numOfExample = test_set.numInstances();
		ArrayList<Double> regResult = new ArrayList<Double>(numOfExample);
		ArrayList<Integer> nomResultList = new ArrayList<Integer>(numOfExample);
		//ArrayList<String> nonResult = new ArrayList<String>(numOfExample);
		if (kc.isRegression()){
			for(int i =0; i < numOfExample; i++ ) {
				Instance in = test_set.instance(i);
				double classifiedReg = kc.classify(in);
				regResult.add(classifiedReg);

			} 
			System.out.println("k value : " + k);
			for(int i =0; i < numOfExample; i++) {
//				System.out.print("Predicted value : ");
//				System.out.printf("%.6f",regResult.get(i)); 
//				System.out.print("	Actual value : ");
//				System.out.printf("%.6f",(double) test_set.instance(i).classValue());
//				System.out.println();
			}
			double absErrorSum = 0;
			for(int i =0; i < numOfExample; i++) {
				double error = regResult.get(i) - test_set.instance(i).classValue();
				absErrorSum += Math.abs(error);
			}
			double mean = absErrorSum/numOfExample;
			System.out.println("Mean absolute error : " + mean);
			System.out.println("Total number of instances : " + numOfExample);
		}else {
			Attribute classAttribute = test_set.classAttribute();
			for(int i =0; i < numOfExample; i++ ) {
				Instance in = test_set.instance(i);
				double classifiedNom = kc.classify(in);
				nomResultList.add((int) classifiedNom);
			}
			System.out.println("k value : " + k);
			int numOfCorrect = 0;
			for(int i =0; i < numOfExample; i++ ) {
				String pridicted = classAttribute.value(nomResultList.get(i));
				String actual = classAttribute.value((int)test_set.instance(i).classValue());
//				System.out.print("Predicted class : ");
//				System.out.print(pridicted); 
//				System.out.print("	Actual class : ");
//				System.out.print(actual);
//				System.out.println();
				if(nomResultList.get(i) == (int)test_set.instance(i).classValue()) {
					numOfCorrect++;
				}
			}
			confusion_matrix(test_set, numOfExample, nomResultList,
					classAttribute);
			
			
			System.out.println("Number of correctly classified instances : " + numOfCorrect);
			System.out.println("Total number of instances : " + numOfExample);
//			System.out.println("Accuracy : " + (double) numOfCorrect/(double) numOfExample );
		}

	}
	private static void confusion_matrix(Instances test_set, int numOfExample,
			ArrayList<Integer> nomResultList, Attribute classAttribute) {
		int numClass = classAttribute.numValues();
		double[][] stats = new double[numClass][numClass];
		for(int i= 0 ; i < numOfExample;  i++) {
			Instance in = test_set.instance(i);
			int actual = (int) in.classValue();
			int predicted = nomResultList.get(i);
			stats[actual][predicted]++;
		}
		for(int i = 0; i < numClass; i++) {
			for(int j =0; j < numClass; j++) {
				System.out.print((int)stats[i][j] + "	");
			}
			System.out.println();
		}
	}
	private static void pukeAndDie() {
		System.out.println("puke and die");
		System.exit(-1);
	}
}
