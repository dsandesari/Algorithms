import java.io.*;
import java.util.*;


public class Percolation {

	/**
	 * @param args
	 */
	public static  HashMap<Integer, String> cache ;
	public static int s;
	public static void main(String[] args) throws IOException {

		if(args.length==3){

			float p=Float.parseFloat(args[0]);//percolation probability
			int n=Integer.parseInt(args[1]);//number of runs
			s=Integer.parseInt(args[2]);//board size
			int percolates=0;//variable to count of how many times system percolates
			for(int turn=0;turn<n;turn++){ //calculates whether lattice percolates for n turns
				
				int[][] a = new int[s][s];
				a=openRandomSites(p);//create a random 2D array.
				int boardsize=s*s;
				UnionFind uf=new UnionFind(boardsize);//creates object and initializes constructor of Union Find
				//Initialize 1D array to pass it to union find constructor
				int []b=new int [boardsize];
				for (int i = 0; i < boardsize; i++) {
					b[i] = i;
				}
				
				converting_to_sets(a,b,uf);//uses union-find to find the elements in  same set and unions them.
				
				int [] c=uf.getarray();//gets the values of 1D array from union find class
				int [][] d=new int[s][s];
				
                //stores the values of 1D array to 2D array
				for (int k=0,j=0,i=0;k<boardsize;k++,j++){
					d[i][j]=uf.find(c[k]);
					if(a[i][j]==0){
						d[i][j]=a[i][j]; //final 2D array
					}
					if((j+1)%s==0)
					{
						i++;j=-1;

					}
				}
				
				
				//calculating if system percolates
				if(ispercolates(d))//returns true if system percolates.
					percolates++;


			}
            System.out.println("For Percolation probability value p= "+p); 
            System.out.println("The system percolates "+percolates+" times for n = "+n+" runs");
			System.out.println("Percolation Rate: "+percolates+" / "+n+" = "+percolates / (n * 1.0f));
		}

		else if(args.length!=0){
		    if(args[0].endsWith(".txt")){
		    	
			//read the no of lines in the file to get size of the array
			s=0;
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			while(br.readLine()!=null){
				s++;
			}
			br.close();
			int  boardsize=s*s;
			int[][] a=new int[s][s];
			BufferedReader newbr = new BufferedReader(new FileReader(args[0]));
			String newline=null;
			int q=0,p=0;
			//Read the contents of file to 2D array
			while ((newline = newbr.readLine()) != null) {//reads until last line of file

				String[] line_contents = newline.split(" ");//splits into strings separated by the spaces
				q=0;    
				for (String str : line_contents) {
					a[p][q]=Integer.parseInt(str);// stores each value in array
					q++;
				}
				p++;
			}
			newbr.close();

			createPPM(a);//write output to PPM file
			UnionFind uf=new UnionFind(boardsize);//creates object and initializes constructor of Union Find
			//Initialize 1D array to pass it to union find constructor
			int []b=new int [boardsize];
			for (int i = 0; i < boardsize; i++) {
				b[i] = i;
			}
			
			converting_to_sets(a,b,uf);//uses union-find to find the elements in  same set and unions them.


			//convert 1D to 2D to represent in grid format
			int [] c=uf.getarray();//gets the values of 1D array from union find class
			int [][] d=new int[s][s];
			
			int[] arr=new int[boardsize];
			for (int k=0,j=0,i=0;k<boardsize;k++,j++){
				d[i][j]=uf.find(c[k]);
				if(a[i][j]==0)
				d[i][j]=a[i][j]; //changes the values of closed sites to 0.
				arr[k]=d[i][j]; //converting 2D array to 1D array with closed sites initialized to 0.
				System.out.print(" "+d[i][j]+" ");
				if((j+1)%s==0)
				{
					i++;j=-1;
					System.out.println("\n");

				}
			}

			int k = 0;
			if((a[0][0]==1)&&(d[0][0]==0)) //one more cluster added in a special case
			{k++;
			d[0][0]=1234567891;
			}

			//sort arrays and remove duplicates	
			Arrays.sort(arr);	
			for (int i = 1; i < boardsize; i++)
			{
				if (arr[i] != arr[k])
				{
					k++;
					arr[k]=arr[i];

				}

			}
             System.out.println("The number of clusters are : "+ k);
             
			
             //Picks a random RGB color value and assigns to a particular value of cell in a hash map
			Random rand=new Random();
			cache = new HashMap<Integer, String>();
			for(int i=0;i<=k;i++){
				int red=rand.nextInt(254)+0;
				int green=rand.nextInt(254)+0;
				int blue=rand.nextInt(254)+0;
				String str=" "+red+" "+green+" "+blue+" ";
				cache.put(new Integer(arr[i]),new String(str));//storing color value in hash map

			}
			cache.put(new Integer(0),new String(" "+255+" "+255+" "+255+" "));//assigning color to open cells
			cache.put(new Integer(1234567891),new String(" "+0+" "+255+" "+0+" "));//assigning color in special case
			//writes a multi-colored PPM file showing different clusters
			createColoredPPM(d,s);
			System.out.println("** 'Colored_Image.ppm' - Image file which outputs the "+k+" clusters with "+k+" different colors where white color represents the closed cells");
			System.out.println("** 'Simple_Image.ppm'  - Image file which outputs two colors where red represents closed cells and green represents open cells.");
		}
		
		else{
			System.out.println("Invalid file format");
		    }
		}
		else if(args.length==0){
			System.out.println("\n Invalid Input Format !" +
		            "\n Enter the input in the following format:"+
					"\n  java Percolation p n s // p - percolation probability; n - no. of runs; s Ð board size;" +
					"\n   #Program outputs percolation rate" +
					"\n  java Percolation fname.txt // the game board setting is saved in the file named fname.txt " +
					"\n   #Program outputs the number of clusters on the board and two images 1.Colored_Image.ppm 2.Simple_Image.ppm");
		}

	}//end of main



	private static void createColoredPPM(int[][] d, int s) {
		// Generate colored clusters

		try {
			BufferedWriter out = new BufferedWriter( new FileWriter("Colored_Image.ppm"));
			out.write("P3"+"\n");
			out.write(s+" "+s+"\n");
			out.write("255"+"\n");

			for (int i1=0;i1<s;i1++){
				for(int j=0;j<s;j++){

					out.write(cache.get(d[i1][j]));

				}
				out.write("\n");
			}
			out.close();//Close the output stream
		} catch (IOException e) {
			// Catch if any IOException
			e.printStackTrace();

		}



	}


	private static boolean ispercolates(int d[][]) {
		// checks whether system percolates.
		int count=0;
		for(int i=0;i<s;i++){
			for(int j=0;j<s;j++){
				/* compares the elements in first row and last row
				 * if the elements are not zero and equal
				 * then there is a definite path from first row to last row
				 */

				if((d[0][i]!=0)&&(d[s-1][j]!=0)&&(d[0][i]==d[s-1][j]))
					count++;

			}
		}

		if(count>0)
			return true;
		else
			return false;
	}




	private static void converting_to_sets(int[][] a, int[] b, UnionFind uf) {
		// Traverses through all the open cells and checks if they are in same set if not unions them
		for (int i=0;i<s;i++){
			for(int j=0;j<s;j++){


				if((j+1)<s){
					if(a[i][j]==a[i][j+1])//if node to right is open the union them
						uf.union(b[i*s+j], b[i*s+j+1]);
				}
				if((j-1)>=0){
					if(a[i][j]==a[i][j-1])//if node to left is open the union them
						uf.union(b[i*s+j],b[i*s+j-1]);
				}
				if((i-1)>=0){
					if(a[i][j]==a[i-1][j])//if node above is open the union them
						uf.union(b[i*s+j],b[(i-1)*s+j]);
				}
				if((i+1)<s){
					if(a[i][j]==a[i+1][j])//if node below is open the union them
						uf.union(b[i*s+j],b[(i+1)*s+j]);
				}


			}

		}//end of union

	}


	private static void createPPM(int[][] a) {
		// writes output to a single color
		try {
			FileWriter fstream = new FileWriter("Simple_Image.ppm");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("P3"+"\n");
			out.write(s+" "+s+"\n");
			out.write("255"+"\n");

			for (int i=0;i<s;i++){
				for(int j=0;j<s;j++){
					if(a[i][j]==0)
						out.write(" "+255+" "+0+" "+0+" ");//if closed red color
					else
						out.write(" "+0+" "+255+" "+0+" ");//if open green color
				}
				out.write("\n");
			}

			out.close();//Close the output stream
		} catch (IOException e) {
			e.printStackTrace();

		}
	}


	private static int[][] openRandomSites(float p) {
		/* Generates a random number for each site
		 * checks if the site probability is less than given probability
		 * then opens the site
		 * else closes the site
		 */
		int arr[][]=new int[s][s];
		Random rand =new Random();
		for (int i=0;i<s;i++){
			for(int j=0;j<s;j++){

				float k= rand.nextFloat();
				if(k<p)
					arr[i][j]=1;

				else
					arr[i][j]=0;
			}
		}

		return arr;

	}



}//end of class UnionFind
