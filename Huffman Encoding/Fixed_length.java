
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;


public class Fixed_length {

	/**
	 * @param args
	 */
	private static int rowN, columnN,maxG; // the image has rowN rows and columnN
	// columns
	private static int[] img; // original image
	private static String inputFileName;
	private static String outputFileName;
	@SuppressWarnings("unused")
	private static String header;
	static HashMap<Integer, HuffNode> frequencymap;
	static HashMap<Integer, String> imageCodes;
	static DataOutputStream out;
	static String code;


	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		System.out.println("Fixed Length Compression....");
		// Read Image file
		inputFileName = args[0];
		File file = new File(inputFileName);
	   String inputFileName = file.getPath();
		
		header = "";

		try {
			

			Scanner inputimage = new Scanner(file);
			if (inputimage.hasNext("P2"))
				header += inputimage.nextLine() + "\n";
			
			if (inputimage.hasNext("#.*"))
				header += inputimage.nextLine() + "\n";
			

			
			columnN = inputimage.nextInt();
			rowN = inputimage.nextInt();
			maxG = inputimage.nextInt();
			
		System.out.println(columnN+" "+rowN);
				img=new int[rowN*columnN]; 
			for (int k = 0; k < rowN * columnN; k++) {
				img[k] = inputimage.nextInt();
			
			}
			outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf('.'))+"_compressed.fixedlength";  //new output file name with path
			
			File outputFile = new File(outputFileName);
			out = new DataOutputStream(new FileOutputStream(outputFile));
			out.writeBytes(header);
			out.writeByte(columnN&0xFF);
			out.writeByte(rowN&0xFF);
			out.writeByte(maxG&0xFF);

			for (int k = 0; k < rowN * columnN; k++) {
				
				out.writeByte(img[k]&0xFF);
				//System.out.println(img[k] + " ");
			}
			
			
			
			out.writeByte(0);//EOF
			out.close();
			 long stopTime = System.currentTimeMillis();
		      double elapsedTime = (stopTime - startTime)/1000d;
		     // System.out.println("The time taken:"+elapsedTime+" secs");
		      System.out.println("Compressed Image is "+outputFileName);
			  System.out.println("######### End ##########");
			inputimage.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
}
