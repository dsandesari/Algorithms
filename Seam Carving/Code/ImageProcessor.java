
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.io.File;

/*
 *	ImageProcessor class process the image and displays the output on the image viewer.
 *  Processing includes stretching, fixedRatio, cropping  and seamCarving.
 */
public class ImageProcessor 
{
   private static int rowN,columnN;     //the image has rowN rows and columnN columns
   private static int[][] img;		      //pixel values of the image 
   private static int[][] originalImg;  //original image 
   private static int[][] energy;
   private static int[][] cumMinEnergy; 
   private static int[][] temp_transpose;
   private String fileName;
   private String header;
   private int maxG;
	
	/* 
	 *	Reads the image file and stores in a 2-D array.
	 */	
   @SuppressWarnings("resource")
public void readImage(File file)
   {			
      this.fileName = file.getPath(); 
      header = "";
      try {
         Scanner input = new Scanner(file);
      	
         if (input.hasNext("P2")) 
            header += input.nextLine()+"\n";
         else
            throw new IOException();
      			
         if (input.hasNext("#.*")) 
            header += input.nextLine()+"\n";
      	
         columnN = input.nextInt();
         rowN = input.nextInt();
         maxG = input.nextInt();
      	
         img = new int[rowN][columnN];
         originalImg = new int[rowN][columnN];
        System.out.println("Processing Original Image..");
         for (int i=0; i<rowN; i++) {
            for (int j=0; j< columnN; j++) {
               originalImg[i][j] = img[i][j] = input.nextInt();
              // System.out.print(img[i][j]+" ");
            }//System.out.println("\n");
         }
         input.close();
      }
      catch (FileNotFoundException e) {
         System.out.println("warning: file not found");
      }
      catch (IOException e) {
         System.out.println("warning: file format error");
      }
   }	
	/*
	 *	ProcessImg process the image based on the input values of processing method and 
	 *	displays the output back on the viewer.
	 */
   public void processImg(String processingMethod, PgmImageViewer viewer)
   {
      System.out.println("processing method = "+processingMethod);
      //resetImgToOriginal();
      if (processingMethod.contains("stretching"))
         viewer.resizeImg(true);
      else if (processingMethod.contains("fixedRatio")) 
         viewer.resizeImg(false);
      else if (processingMethod.contains("cropping")) {
         viewer.resizeImg(false);
         int viewerH = viewer.getHeight();
         int viewerW = viewer.getWidth();
      	//remove one row/column at a time
         if ((double)rowN/columnN/((double)viewerH/viewerW)>1) {
            while ((double)rowN/columnN/((double)viewerH/viewerW)>1)  
               rowN--;				
         }
         else {
            while ((double)rowN/columnN/((double)viewerH/viewerW)<1)
               columnN--;
         }
         img = updateImg();
       } 
       else if(processingMethod.contains("seamCarving")) {	
    	  
         viewer.resizeImg(false);
         String delims = "[_(,) ]+";
         String[] tokens = processingMethod.split(delims);
         int verticalSeamsToBeRemoved = Integer.parseInt(tokens[1]);
         int horizontalSeamsToBeRemoved = Integer.parseInt(tokens[2]);
      	
         System.out.println("  verticalSeamsToBeRemoved= "+verticalSeamsToBeRemoved
            +" horizontalSeamsToBeRemoved = "+horizontalSeamsToBeRemoved); 
      	
         /* Removing vertical seams:
          * For each iteration, cumulative energy is calculated from pixel energy.
          * Vertical seam with minimum cumulative energy is found out.
          * Each vertical seam is removed using seam carving function.
          */
         if(verticalSeamsToBeRemoved>0){
      			for(int v=0;v<verticalSeamsToBeRemoved;v++){
      				calculatePixelEnergy();//calculates the energy function.
      				/*	 
      				    System.out.println("Vertical Pixel Energy:");
      				     for (int i = 0; i < rowN; i++) {
      					  for (int j = 0; j < columnN; j++) {
      						System.out.print(energy[i][j]+" ");
      					   }System.out.println("\n");
      					 }
      				*/
      				calculateCumulativeMinEnergy();//calculates the minimum cumulative energy
      				/*
      				 System.out.println("Vertical Cumulative Energy:");
      				 for (int i = 0; i < rowN; i++) {
      					for (int j = 0; j < columnN; j++) {
      						System.out.print(cumMinEnergy[i][j]+" ");
      					}System.out.println("\n");
      				}
      				*/
      				seamCarving();//removes the vertical seam from the image
      				/*
      				 System.out.println("Vertical Seam Carving :");
       				  for (int i = 0; i < rowN; i++) {
       					for (int j = 0; j < columnN; j++) {
       						System.out.print(img[i][j]+" ");
       					}System.out.println("\n");
       				 }
       				*/
       			  columnN = columnN - 1;//no of columns is reduced by one after removing a vertical seam
       			  img = updateImg();	//updating the image after removing the vertical seam
       	        
      				
      		    }
         }
         
         /* 
          * Removing horizontal seams:
          * To remove horizontal seams,First the image matrix is transposed,Then :
          * For each iteration, cumulative energy is calculated from pixel energy.
          * Vertical seam with minimum cumulative energy is found out.
          * Each vertical seam is removed using seam carving function.
          * After that the matrix is transposed back to original form.
          */
         if(horizontalSeamsToBeRemoved>0){
    		    //The img[][] matrix is transposed.
      			temp_transpose = new int[columnN][rowN];
      			
    			for (int i = 0; i < rowN; i++) {
    				for (int j = 0; j < columnN; j++) {
    					temp_transpose[j][i] = img[i][j];

    				}
    			}
    			img = new int[columnN][rowN];
    			for (int i = 0; i < columnN; i++) {
    				for (int j = 0; j < rowN; j++) {
    					img[i][j] = temp_transpose[i][j];

    				}
    			}
    			int temp1 = rowN;
    			int temp2 = columnN;
    			rowN = temp2;
    			columnN = temp1;
      	
      			for(int h=0;h<horizontalSeamsToBeRemoved;h++){
      				calculatePixelEnergy();//calculates the energy function.
      				/* 
      				 System.out.println("Transpose of Horizontal Pixel Energy:");
      				 for (int i = 0; i < rowN; i++) {
      					for (int j = 0; j < columnN; j++) {
      						System.out.print(energy[i][j]+" ");
      					}System.out.println("\n");
      				 }
      				 */
      				calculateCumulativeMinEnergy();	//calculates the minimum cumulative energy
      				 /*System.out.println("Transpose of Horizontal Cumulative Energy:");
       				   for (int i = 0; i < rowN; i++) {
       					 for (int j = 0; j < columnN; j++) {
       						System.out.print(cumMinEnergy[i][j]+" ");
       					 }System.out.println("\n");
       				   }
       				 */
      				seamCarving();//removes the vertical seam from the transposed image which is equivalent to removing horizontal seam.
      				/* System.out.println("Transpose of Horizontal Seam Carving :");
        				for (int i = 0; i < rowN; i++) {
        					for (int j = 0; j < columnN; j++) {
        						System.out.print(img[i][j]+" ");
        					}System.out.println("\n");
        				}
        				*/
      				columnN = columnN - 1;
      		    }
      			//Transforming the transpose matrix to original matrix
      			temp_transpose = new int[columnN][rowN];
    			for (int i = 0; i < rowN; i++) {
    				for (int j = 0; j < columnN; j++) {
    					temp_transpose[j][i] = img[i][j];

    				}
    			}
    			img= new int[columnN][rowN];
    			for (int i = 0; i < columnN; i++) {
    				for (int j = 0; j < rowN; j++) {
    					img[i][j] = temp_transpose[i][j];

    				}
    			}
    			 temp1 = rowN;
    			 temp2 = columnN;
    			rowN = temp2;
    			columnN = temp1;
     	
         img = updateImg();	//updating the image after removing the horizontal seam
         }
         saveImage();//saves the image as filename_processed.pgm
        
      }
   }	
	
   /*
	 *	Calculates energy values for each pixel of the 2-D image matrix and stores in the energy matrix
	 */
   private void calculatePixelEnergy() {
   
		energy = new int[rowN][columnN];
		for(int i=0;i<rowN;i++){
			for(int j=0;j<columnN;j++){
				if(i==0){//for the elements in the first row.
					
					if (j==0){//for the first element of the array
						if(rowN==1){
							energy[i][j] =Math.abs(img[i][j]-img[i][j+1] );
						}
						else//energy is absolute difference of values of elements on the right side and below
						    energy[i][j]  =Math.abs(img[i][j]-img[i+1][j] )
										+ Math.abs(img[i][j]-img[i][j+1]);
						
						
					}
					if (j>0&&j<columnN-1){//for the element not on the first and last column
						if(rowN==1){
							energy[i][j] = Math.abs(img[i][j]-img[i][j+1]) 
									+ Math.abs(img[i][j]-img[i][j-1]) ;
						}
						else//energy is absolute difference of values of elements on the right side,left side and below
						energy[i][j] = Math.abs(img[i][j]-img[i+1][j]) 
								+ Math.abs(img[i][j]-img[i][j-1]) 
								+ Math.abs(img[i][j]-img[i][j+1]);
						
					}
					if(j==columnN-1){//for the last element in the first row
						if(rowN==1){
							energy[i][j]  =  Math.abs(img[i][j]-img[i][j-1]) ;
						}else//energy is absolute difference of values of elements on the left side and below
						energy[i][j]  = Math.abs(img[i][j]-img[i+1][j]) 
								         + Math.abs(img[i][j]-img[i][j-1]) ;
						
					}

				}else if(j==0&&i!=0){//for the elements on the first column except the first element of the array.
					
					if(i>0&&i<rowN-1){//for the elements of the first column except the element in the last row.
						//energy is absolute difference of values of elements on the right side,left side and below
						energy[i][j] = Math.abs(img[i][j]-img[i-1][j]) 
								+ Math.abs(img[i][j]-img[i+1][j]) 
								+ Math.abs(img[i][j]-img[i][j+1]);
						
					}
					if(i==rowN-1){//for the element in the last row of the first column.
						//energy is absolute difference of values of elements on the right above and right side
						energy[i][j] = Math.abs(img[i][j]-img[i-1][j]) 
								+ Math.abs(img[i][j]-img[i][j+1]);
						
					}

				}else if(i==rowN-1&&j!=0){//for the elements in the last row except the first element in the last row.
					
					if(j>0&&j<columnN-1){//for the elements in the last row except the first and last element in the last row.
						energy[i][j] = Math.abs(img[i][j]-img[i-1][j]) 
								+ Math.abs(img[i][j]-img[i][j-1]) 
								+ Math.abs(img[i][j]-img[i][j+1]);
						
					}
					if(j==columnN-1){//for the element in last row and last column.
						energy[i][j] = Math.abs(img[i][j]-img[i-1][j]) 
								+ Math.abs(img[i][j]-img[i][j-1]) ;
						
					}
				}else if(j==columnN-1&&i!=0&&i!=rowN-1){//for the elements in the last column except the first and last element in the last column.
					
					if(i>0&&i<rowN-1){
						energy[i][j] = Math.abs(img[i][j]-img[i-1][j]) 
								+ Math.abs(img[i][j]-img[i+1][j]) 
								+ Math.abs(img[i][j]-img[i][j-1]) ;
						
					}

				}else{//for all the elements of the array not at the border.
					//energy is absolute difference of values of elements on the right side,left side, below and above
					energy[i][j] = Math.abs(img[i][j]-img[i-1][j]) 
							+ Math.abs(img[i][j]-img[i+1][j]) 
							+ Math.abs(img[i][j]-img[i][j-1]) 
							+ Math.abs(img[i][j]-img[i][j+1]);
				
				}


			}
		}

   }	
	/*
	 *	Calculates cumulative energy values for each pixel of the 2-D energy matrix  
	 *  and stores in the cumMinEnergy matrix
	 */
   private void calculateCumulativeMinEnergy() {
   	
	    cumMinEnergy = new int[rowN][columnN];
		for (int i = 0; i < rowN; i++) {
			for (int j = 0; j < columnN; j++) {
				if (i == 0) {
					cumMinEnergy[i][j] = energy[i][j];
					
				}
			}
		}
		for (int i = 1; i < rowN; i++) {
			for (int j = 0; j < columnN; j++) {

				if (j == 0) {//for first column
                      //Cumulative energy is sum of energy and min cumulative energy of above value and above right value.
					cumMinEnergy[i][j] = energy[i][j]
							+ Math.min(cumMinEnergy[i - 1][j],cumMinEnergy[i - 1][j + 1]);

				

				} else if (j == columnN - 1) {//for last column
					  //Cumulative energy is sum of energy and min cumulative energy of above value and above left value.
					cumMinEnergy[i][j] = energy[i][j]+ 
							Math.min(cumMinEnergy[i - 1][j],cumMinEnergy[i - 1][j - 1]);

					

				} else {
					  //Cumulative energy is sum of energy and min cumulative energy of above value , above left and above right value.
					cumMinEnergy[i][j] = energy[i][j]+ 
							Math.min(cumMinEnergy[i - 1][j - 1], Math.min(cumMinEnergy[i - 1][j],cumMinEnergy[i - 1][j + 1]));

					
				}
			}
		}

   }
	/*
	 *	Finds the vertical seam with minimum cumulative energy from bottom to top and removes the seam.
	 */
   private void seamCarving() {
   
	   
	   int columnindex=0;//column index of element with minimum cumulative energy value
	  //finds the column index with minimum cumulative energy value in the last row.
	   int leastvalue=cumMinEnergy[rowN-1][0];
	   for(int j=0;j<columnN;j++){

		   if (cumMinEnergy[rowN-1][j]<leastvalue)
		   {
			   leastvalue=cumMinEnergy[rowN-1][j];
			   columnindex=j;
		   }
	   }
	   //removes the element with minimum cumulative energy value from the last row.
	   int k=columnindex;
	   for(int j=0;j<columnN-1;j++)
	   {
		   if(j==k){
			   img[rowN-1][j]=img[rowN-1][j+1];    
			   k++;

		   }
	   }

	   for(int i = rowN-2;i>=0;i--){//for each of the other rows from bottom
		   int minEnergy = 0;
		   if(columnindex==0){//for the leftmost element
			   		minEnergy = Math.min(cumMinEnergy[i][columnindex],cumMinEnergy[i][columnindex+1]);
			   		//finds the minimum cumulative energy element from both the current value and one on the right and removes the element from img

			   		if(minEnergy == cumMinEnergy[i][columnindex]){

				       for(int j=columnindex;j<columnN-1;j++){
					      img[i][j]=img[i][j+1];
				       }

		           }else if(minEnergy == cumMinEnergy[i][columnindex+1]){

		        	   columnindex=columnindex+1;
				   for(int j=columnindex;j<columnN-1;j++){
					   img[i][j]=img[i][j+1];
				   }
			     }
		   }else if(columnindex>0&&columnindex<columnN-1){//for the elements not on the borders
			   
			   		minEnergy = Math.min(Math.min(cumMinEnergy[i][columnindex-1],cumMinEnergy[i][columnindex]),cumMinEnergy[i][columnindex+1]);
			   		//finds the minimum cumulative energy element from the one on the right, current value and one on the left and removes the element from img
			   		if(minEnergy == cumMinEnergy[i][columnindex-1]){
			   		columnindex=columnindex-1;
				    for(int j=columnindex;j<columnN-1;j++){
					   img[i][j]=img[i][j+1];
					   }
			        }
			   		else if(minEnergy == cumMinEnergy[i][columnindex]){
			   		for(int j=columnindex;j<columnN-1;j++){
					   img[i][j]=img[i][j+1];
					   }
			       }else if(minEnergy == cumMinEnergy[i][columnindex+1]){

				   columnindex=columnindex+1;
				   for(int j=columnindex;j<columnN-1;j++){
					   img[i][j]=img[i][j+1];
					   }
			       }
		   }else if(columnindex==columnN-1){//for the rightmost element
			    //finds the minimum cumulative energy element from the one on the left and the current value and removes the element from img
			       minEnergy = Math.min(cumMinEnergy[i][columnindex],cumMinEnergy[i][columnindex-1]);
			       if(minEnergy == cumMinEnergy[i][columnindex]){

				   for(int j=columnindex;j<columnN-1;j++){
					   img[i][j]=img[i][j+1];
					   }
			       }else if(minEnergy == cumMinEnergy[i][columnindex-1]){

				    columnindex=columnindex-1;
				    for(int j=columnindex;j<columnN-1;j++){
					   img[i][j]=img[i][j+1];
					   }
			       }
		   }
	   }
	 



   
   }
	/*
	 * updates the image
	 */
   private int[][] updateImg() {
      int[][] processedImg = new int[rowN][columnN];
   
      for (int i=0; i<rowN; i++) {
         for (int j=0; j< columnN; j++) {
            processedImg[i][j] = img[i][j];
         }
      }
      return processedImg;
   }
	
	/*
	 *	Saves the processed image in a pgm file named : original_image_file_name_processed.pgm
	 */
   public void saveImage()
   {
      if (fileName!=null) {
         String outputFileName = fileName.substring(0, fileName.lastIndexOf('.'))
            +"_processed.pgm";  //new output file name with path
         System.out.println("Output Image path is: "+outputFileName);
        
         try {
            FileWriter fwriter = new FileWriter(outputFileName);
            PrintWriter outputFile = new PrintWriter(fwriter);
            outputFile.print(header);
            outputFile.print("" + columnN + " " + rowN + "\n"+maxG+"\n");
            for (int i=0; i<rowN; i++) {
               for (int j=0; j< columnN; j++) {
                  outputFile.print(img[i][j] + " ");
               }
               outputFile.print("\n");
            }
            fwriter.close();
         }
         catch (IOException e) {
            System.out.println("warning: file output error");
         }
      }
      getImg();
   }
	/*
	 *	resets the image array to original image pixel values
	 */
   private void resetImgToOriginal()
   {
      rowN = originalImg.length;
      columnN = originalImg[0].length;
      img = new int[rowN][columnN];
      for (int i=0; i<rowN; i++) {
         for (int j=0; j< columnN; j++) {
            img[i][j] = originalImg[i][j];
         }
      }
   }
	
   public int[][] getImg()
   {
      return img;
   }
	

   public int[][] getOriginalImg()
   {
      return originalImg;
   }
}
