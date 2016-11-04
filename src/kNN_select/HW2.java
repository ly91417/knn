package kNN_select;
import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;
import java.util.ArrayList;
public class HW2 {
	public static void main(String[] args) throws Exception {
		if (args.length	!= 5)
		{
			System.out.println("usage: kNN <train-set-file> <test-set-file> k1 k2 k3");
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
		Integer k1 = Integer.valueOf(args[2]);
		Integer k2 = Integer.valueOf(args[3]);
		Integer k3 = Integer.valueOf(args[4]);
		if(train_set.classAttribute().name().equals("class")) {
			classificationProblem(train_set, test_set, k1, k2, k3);
		}else if (train_set.classAttribute().name().equals("response")){
			regressionProblem(train_set, test_set, k1, k2, k3);
		}
		
	}

	private static void regressionProblem(Instances train_set,
			Instances test_set, Integer k1, Integer k2, Integer k3) {
		double count1 = regressionCrossValidate(train_set, k1);
		System.out.println("Mean absolute error for k = " + k1 + " : " + count1);
		double count2 = regressionCrossValidate(train_set, k2);
		System.out.println("Mean absolute error for k = " + k2 + " : " + count2);
		double count3 = regressionCrossValidate(train_set, k3);
		System.out.println("Mean absolute error for k = " + k3 + " : " + count3);
		int[] kValues = {k1,k2,k3};
		double[] count = {count1, count2, count3};
		double min = Integer.MAX_VALUE;
		int minIndex = Integer.MAX_VALUE;
		for(int i = 0; i < 3; i++ ) {
			if(count[i] < min) {
				min = count[i];
				minIndex = i;
			}
		}
		assert(minIndex != Integer.MAX_VALUE) : "there is something wrong with your k values";
		System.out.println("Best k value : " + kValues[minIndex]);
		int numOfExample = test_set.numInstances();
		kNN_Classifier kcn = new kNN_Classifier(kValues[minIndex],train_set);
		print(test_set, k1, kcn, numOfExample);
	}

	private static void classificationProblem(Instances train_set,
			Instances test_set, int k1, int k2, int k3) {
		int count1 = classificationCrossValidate(train_set, k1);
		System.out.println("Number of incorrectly classified instances for k = " + k1 + " : " + count1);
		int count2 = classificationCrossValidate(train_set, k2);
		System.out.println("Number of incorrectly classified instances for k = " + k2 + " : " + count2);
		int count3 = classificationCrossValidate(train_set, k3);
		System.out.println("Number of incorrectly classified instances for k = " + k3 + " : " + count3);
		int[] kValues = {k1,k2,k3};
		int[] count = {count1, count2, count3};
		int min = Integer.MAX_VALUE;
		int minIndex = Integer.MAX_VALUE;
		for(int i = 0; i < 3; i++ ) {
			if(count[i] < min) {
				min = count[i];
				minIndex = i;
			}
		}
		assert(minIndex != Integer.MAX_VALUE) : "there is something wrong with your k values";
		System.out.println("Best k value : " + kValues[minIndex]);
		int numOfExample = test_set.numInstances();
		kNN_Classifier kcn = new kNN_Classifier(kValues[minIndex],train_set);
		print(test_set, k1, kcn, numOfExample);
	}
	private static double regressionCrossValidate(Instances train_set, Integer k){
		double MAE = 0;
		int trainSetSize = train_set.numInstances();
		for(int i = 0; i < trainSetSize; i++) {
			Instance testInstance = train_set.instance(i);
			Instances set = new Instances(train_set);
			set.delete(i);
			kNN_Classifier kcCross = new kNN_Classifier(k,set);
			MAE += Math.abs(testInstance.classValue() - kcCross.classify(testInstance));
		}
		return MAE/(train_set.numInstances());
	}
	private static int classificationCrossValidate(Instances train_set, Integer k) {
		int numIncorrect = 0;
		int trainSetSize = train_set.numInstances();
		for(int i = 0; i < trainSetSize; i++) {
			Instance testInstance = train_set.instance(i);
			Instances set = new Instances(train_set);
			set.delete(i);
			kNN_Classifier kcCross = new kNN_Classifier(k,set);
			if(testInstance.classValue() == kcCross.classify(testInstance)){
				continue;
			}
			numIncorrect++;
		}
		return numIncorrect;
	}
	
	private static int print(Instances test_set, Integer k, kNN_Classifier kc,
			int numOfExample) {
		ArrayList<Double> regResult = new ArrayList<Double>(numOfExample);
		ArrayList<Integer> nomResultList = new ArrayList<Integer>(numOfExample);
		int numOfCorrect = 0;
		if (kc.isRegression()){
			for(int i =0; i < numOfExample; i++ ) {
				Instance in = test_set.instance(i);
				double classifiedReg = kc.classify(in);
				regResult.add(classifiedReg);

			} 
//			System.out.println("k value : " + k);
			for(int i =0; i < numOfExample; i++) {
				System.out.print("Predicted value : ");
				System.out.printf("%.6f",regResult.get(i)); 
				System.out.print("	Actual value : ");
				System.out.printf("%.6f",(double) test_set.instance(i).classValue());
				System.out.println();
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
//			System.out.println("k value : " + k);
			numOfCorrect = 0;
			for(int i =0; i < numOfExample; i++ ) {
				String pridicted = classAttribute.value(nomResultList.get(i));
				String actual = classAttribute.value((int)test_set.instance(i).classValue());
				System.out.print("Predicted class : ");
				System.out.print(pridicted); 
				System.out.print("	Actual class : ");
				System.out.print(actual);
				System.out.println();
				if(nomResultList.get(i) == (int)test_set.instance(i).classValue()) {
					numOfCorrect++;
				}
			}
			System.out.println("Number of correctly classified instances : " + numOfCorrect);
			System.out.println("Total number of instances : " + numOfExample);
			System.out.println("Accuracy : " + (double) numOfCorrect/(double) numOfExample );
		}
		return numOfExample - numOfCorrect;
	}
	private static void pukeAndDie() {
		System.out.println("puke and die");
		System.exit(-1);
	}
}
