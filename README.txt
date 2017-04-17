À set of algorithms linear algebra. 
-----------------------------------

For compiling program need enter in command line from root of project:
>>> mvn install

After compiling get jar file CM_1-0.0.1-SNAPSHOT, the programs runs
with two arguments:
1) First is a command:
	-sw --- Sweep Method 
	-ze --- Zeidel Method
	-sp --- Spins Method
	-si --- Simple Iteration Method
	-qr --- QR Method, only  for 3x3 matrix
	-ga --- Gauss Method
2) Second is a name of file with input data

Format of input file for Sweep Method:
1) Size of tridigional matrix
2) Non zero elements of matrix
3) Vector of right coefficients
4) Accurancy
Example:
5
-10 -9
-5 -21 -8
7 12 2
0 8 2
2 10
7
29
31
56
-24
0,0001

Format of input file for Zeidel Method and Simple iteration method:
1) Size of matrix
2) Matrix
3) Vector of right coefficients
4) Accurancy
Example:
4
19 -4 -9 -1
-2 20 -2 -7
6 -5 -25 9
0 -3 -9 12 
100
-5
34
69
0,001

Format of input file for Sweep Method:
1) Size of matrix
2) Matrix
3) Accurancy
Example:
3
-7 4 5
4 -6 -9
5 -9 -8
0,001

Format of input file for Gauss Method:
1) Size of matrix
2) Matrix
3) Vector of right coefficients
Example:
4
2 -8 0 5
-9 9 -7 6
-6 7 3 8
-1 8 5 1
-40
-58
-75
1

Format of input file for QR decomposition (was tested only on matrix 3x3):
1)Size of matrix
2)Matrix
3)Accurancy
Example:
3
7 6 -3
5 -6 8
-7 4 -5
0,0001
