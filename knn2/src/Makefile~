###
#
#
#
#
###

##test
test:
	java -jar kNN-select.jar wine_train.arff wine_test.arff 3 5 7 >wine.out1
	java -jar kNN-select.jar yeast_train.arff yeast_test.arff 3 5 7 >yeast.out1

##compare
cp1:	
	diff -u wine.out wine.out1 > diffwine.out2
cp2:	
	diff -u yeast.out yeast.out1 > diffyeast.out2

###
#clean
###
clean:
	rm -f *.out1 *.out2 *~
