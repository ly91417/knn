###
#
#
#
#
###

##test
test:
	java -jar kNN.jar diabetes_train.arff diabetes_test.arff 3 >diabetes_3.out1
	java -jar kNN-select.jar diabetes_train.arff diabetes_test.arff 3 5 7 >diabetes357.out1	
	java -jar kNN.jar power_plant_train.arff power_plant_test.arff 3 >pp_3.out1
	java -jar kNN-select.jar power_plant_train.arff power_plant_test.arff 3 5 7 >pp357.out1	
#	java -jar kNN.jar wine_train.arff wine_test.arff 3 >wine3.out1
#	java -jar kNN.jar wine_train.arff wine_test.arff 5 >wine5.out1
#	java -jar kNN.jar wine_train.arff wine_test.arff 7 >wine7.out1
#	java -jar kNN.jar yeast_train.arff yeast_test.arff 3 >yeast3.out1
#	java -jar kNN.jar yeast_train.arff yeast_test.arff 5 >yeast5.out1
#	java -jar kNN.jar yeast_train.arff yeast_test.arff 7 >yeast7.out1
#	java -jar kNN-select.jar wine_train.arff wine_test.arff 3 5 7 >wine.out1
#	java -jar kNN-select.jar yeast_train.arff yeast_test.arff 3 5 7 >yeast.out1

##compare
#cp1:	
#	diff -u wine3.out wine3.out1 > diff3wine.out2
#cp2:	
#	diff -u wine5.out wine5.out1 > diff5wine.out2
#cp3:	
#	diff -u wine7.out wine7.out1 > diff7wine.out2
#cp4:	
#	diff -u yeast3.out yeast3.out1 > diff3yeast.out2
#cp5:	
#	diff -u yeast5.out yeast5.out1 > diff5yeast.out2
#cp6:	
#	diff -u yeast7.out yeast7.out1 > diff7yeast.out2
#cp7:	
#	diff -u wine.out wine.out1 > diffwine.out2
#cp8:
#	diff -u yeast.out yeast.out1 > diffyeast.out2
cpp3:
	diff -u pp_3.ou1 pp_3.txt > diffcpp3.out2
cpp357:
	diff -u pp357.ou1 pp_357.txt > diffcpp357.out2
cd3:
	diff -u diabetes_3.out1 diabetes_3.txt > diffdiabetes3.out2
cd357:
	diff -u diabetes357.out1 diabetes357.txt > diffdiabetes357.out2
###
#clean
###
clean:
	rm -f *.out1 *.out2 *~ *.out
