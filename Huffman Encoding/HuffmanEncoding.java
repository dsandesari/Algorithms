
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HuffmanEncoding {

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
		// Read Image file
		inputFileName = args[0];
		File file = new File(inputFileName);
		inputFileName = file.getPath();
		header = "";

		try {
			Scanner input = new Scanner(file);
			if (input.hasNext("P2"))
				header += input.nextLine() + "\n";
			if (input.hasNext("#.*"))
				header += input.nextLine() + "\n";

			columnN = input.nextInt();
			rowN = input.nextInt();
			maxG = input.nextInt();

			img = new int[rowN * columnN];

			System.out.println("Compressing Image..");
			for (int k = 0; k < rowN * columnN; k++) {
				img[k] = input.nextInt();
				//System.out.println(img[k] + " ");
			}
			input.close();

			// Calculate frequencies

			frequencymap = new HashMap<Integer, HuffNode>();

			for (int i = 0; i < img.length; i++) {
				if (frequencymap.get(img[i]) != null) {
					frequencymap.get(img[i]).myFrequency++;

				}

				else
					frequencymap.put(img[i], new HuffNode(img[i], 1));

			}

			HuffNode node = constructTree(frequencymap);
			//System.out.println("Leftnode frequency: " + node.myFrequency+ " LeftNode Value:" + node.myImg + "left  " + node.myLeft+ " right " + node.myRight);
			imageCodes = new HashMap<Integer, String>();
			StringBuffer prefix = new StringBuffer();
			prefix.append('0');
			assignCodes(node, prefix);
			outputFileName = inputFileName.substring(0, inputFileName.lastIndexOf('.'))+"_compressed.huffzip";  //new output file name with path
			File outputFile = new File(outputFileName);

			out = new DataOutputStream(new FileOutputStream(outputFile));
			/* writes tree info to output file */
			
			PriorityQueue<HuffNode> queue1 = new PriorityQueue<HuffNode>();
			queue1.addAll(frequencymap.values());
			
			while(queue1.size()>0){
				HuffNode node1=queue1.poll();
			out.writeByte(node1.myImg);
			out.writeInt(node1.myFrequency);
			//System.out.println("Value:" +node1.myImg+"  Frequency:" +node1.myFrequency);
			}
	
			out.writeByte(0);
			out.writeInt(0); // special code to indicate end of file

			/* writes encoding image info to output file */

			Scanner inputimage = new Scanner(file);
			if (inputimage.hasNext("P2"))
				header += inputimage.nextLine() + "\n";
			if (inputimage.hasNext("#.*"))
				header += inputimage.nextLine() + "\n";

			columnN = inputimage.nextInt();
			rowN = inputimage.nextInt();
			maxG = inputimage.nextInt();
			out.writeInt(columnN);
			out.writeInt(rowN);
			out.writeInt(maxG);
			
			code = imageCodes.get(inputimage.nextInt());
			//System.out.println("code is  "+code);

			for (int i = 0; i < (rowN * columnN) - 1; i++) {

				code = code + imageCodes.get(inputimage.nextInt());
				//System.out.println("code is  "+code);
				

			}
			inputimage.close();
			int rem = (code.length() % 8);
			if (rem != 0) {
				rem = 8 - rem;

				for (int i = 0; i < rem; i++) {
					code = code + "0";
				}
			}

			// store binary code

			// break the binary string into 8-bit segments
			while (code.length() > 7) {
				
				// convert a 8-bit string to a byte
				int c = (int) Integer.parseInt(code.substring(0, 8), 2);//value in int
				byte b=(byte) c;
				out.writeByte(b&0xFF);//value in byte
				code = code.substring(8);

			}
			out.writeByte(0);//EOF
			out.close();
			 long stopTime = System.currentTimeMillis();
		      double elapsedTime = (stopTime - startTime)/1000d;
		    //  System.out.println("The time taken:"+elapsedTime+" secs");
		      System.out.println("The compressed image is: "+outputFileName);
			 System.out.println("######### End ##########");
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void assignCodes(HuffNode tree, StringBuffer pixelcode) {
		assert tree != null;
		if (tree.myImg != -1) {
			
			// store the code values for each distinct pixel in a hashmap
			imageCodes.put(tree.myImg, pixelcode.toString());
			//System.out.println(tree.myImg + ":" + prefix.toString());

		} else if (tree.myImg == -1) {

			HuffNode node = (HuffNode) tree;
			// traverse left if 0 is encountered
			pixelcode.append('0');
			assignCodes(node.myLeft, pixelcode);
			pixelcode.deleteCharAt(pixelcode.length() - 1);
			// traverse right if 1 is encountered
			pixelcode.append('1');
			assignCodes(node.myRight, pixelcode);
			pixelcode.deleteCharAt(pixelcode.length() - 1);
		}
	}

	private static HuffNode constructTree(HashMap<Integer, HuffNode> frequencymap) {

		PriorityQueue<HuffNode> queue = new PriorityQueue<HuffNode>();
		queue.addAll(frequencymap.values());
		while (queue.size() > 1) {

			HuffNode leftnode = queue.poll();
			//System.out.println("Leftnode frequency: " + leftnode.myFrequency+ " LeftNode Value:" + leftnode.myImg);

			HuffNode rightnode = queue.poll();
			//System.out.println("Right frequency: " + rightnode.myFrequency+ " Right Value:" + rightnode.myImg);
			HuffNode merge = new HuffNode(leftnode, rightnode);
			//System.out.println("merge frequency: " + merge.myFrequency+ " merge Value:" + merge.myImg);
			queue.offer(merge);

		}
		return queue.element();

	}

}
