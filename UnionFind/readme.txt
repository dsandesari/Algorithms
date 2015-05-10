This project is to write a program that implements the union-find algorithm and use it to estimate the value of “percolation threshold” for a square lattice.
The algorithm can be readily used to generate square mazes:
Problem: If sites are independently set to be opened with probability p, what is the probability the maze percolates (we can cross the maze from one side to another)? What is the threshold p* such that when p>p*, we can almost certainly to do so and when p<p*, the chance of success is small?
Approach: 1) performing a computer simulation to estimate the threshold. You should run your simulation for n runs for every p value you choose. For each run, based on the value of p, randomly pick sites to be open; 2) using Union-find to examine whether there is a path from one side to another for each run; 3) Estimated p*.
The program program allows the user to specify the number of runs for the simulation and size of the game board (assume width = length) through common line arguments. 
yourProgram.exe p n s // p - percolation probability; n - # of runs; s – board size; // your program should output percolation rate
yourProgram.exe fname.txt // the game board setting is saved in the file named fname.txt // your program should output the number of clusters on the board
//click here for an example. 1 stands for open site; 0 for closed site.