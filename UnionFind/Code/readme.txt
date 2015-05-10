This file provides a high-level explanation of UNION FIND implementation,estimates the value of “percolation threshold” for a square lattice.
It has also the additional information that helps the user to run the program.


//************************************/Implementation of UNION FIND Algorithm/*****************************************//

The Union Find algorithm can be readily used to generate square mazes.
The project contains two files:  1.Percolation.java and 2.UnionFind.java
Percolation consists of actual implementation of the program by using union find algorithm in unionFind.java file

The implementation consist of two parts:
1.In the first part we input p n s from command line. // p - percolation probability; n - # of runs; s – board size
The program outputs percolation rate.

The program creates a board size of s*s and opens random cells based on percolation probability of each cell.
Then it checks whether system percolates for n runs.
The program calculates p=(no of times it percolates)/n.


2.In the second part the input to the program is a game board setting saved in a text file.
We input the file name from command line the program outputs number of clusters in the file.
It also outputs two files:
 [Bonus (5%)]'Colored_Image.ppm' - Image file which consists of all the clusters with different colors where white color represents the closed cells.
             'Simple_Image.ppm'  - Image file which consists of two colors where red represents closed cells and green represents open cells.
		
In this part the program scans the game board setting from a text file and finds the different clusters.
The program not only calculates the no of clusters but also outputs two image files in PPM format with ascii encoding.
One files represent 1 with green color and 0 with red color.
The second files differentiates different clusters with different colors.


//************************************/Execution of UNION FIND Algorithm/*****************************************//

The project is developed using Eclipse in Java. 

Compilation:
javac Percolation.java

Execution:
Part ONE:

java Percolation p n s
 // p - percolation probability; n - # of runs; s – board size
 //outputs - percolation rate.

Part TWO:

java Percolation filename.txt
  //outputs-No of clusters on command line.
  //outputs two 256 bit PPM files with ascii encoding in the compilation folder:
   [Bonus (5%)]'Colored_Image.ppm' - Image file which consists of all the clusters with different colors where white color represents the closed cells.
               'Simple_Image.ppm'  - Image file which consists of two colors where red represents closed cells and green represents open cells.


Note:
The input file in part two should be a text file and should contain the game board setting as shown below with “spaces” between each digit:
1 0 1 1 1 1 0 0 1 1 
0 1 0 1 0 1 1 1 1 1 
1 0 1 1 1 0 1 0 1 1 
1 0 1 1 0 1 1 0 0 0 
0 1 1 1 1 0 1 1 0 1 
0 1 1 0 1 0 1 0 1 1 
0 0 0 1 1 0 0 1 1 1 
1 0 0 1 0 0 1 0 1 0 
1 1 1 1 1 1 0 0 0 1 
1 1 1 1 0 0 1 1 1 1


If the input entered through command line is invalid:
For example: java Percolation
Output will be:

 Invalid Input Format !
 Enter the input in the following format:
  java Percolation p n s // p - percolation probability; n - no. of runs; s – board size;
   #Program outputs percolation rate
  java Percolation fname.txt // the game board setting is saved in the file named fname.txt 
   #Program outputs the number of clusters on the board and two images 1.Colored_Image.ppm 2.Simple_Image.ppm









