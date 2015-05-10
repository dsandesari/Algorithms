import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;




public class HuffmanDecoding {

	/**
	 * @param args
	 */
	private static int rowN, columnN,maxG;
	static HuffNode ouputTree;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long startTime = System.currentTimeMillis();
		System.out.println("Deompressing....");
		
		try{
		String inputFile = args[0];
		DataInputStream decompIn = new DataInputStream(new FileInputStream(inputFile));
		HashMap<Integer, HuffNode> inputTree = new HashMap<Integer, HuffNode>();
		  int ch;
		    int num;
		    while (true) {
		      ch = decompIn.readByte()&0xFF;
		      //ch = decompIn.readInt();
		      num = decompIn.readInt();
		      //System.out.println("value ch:"+ch);
		       if (num == 0) // EOF
		    	  break;
		      inputTree.put(ch,new HuffNode(ch,num));
		      //System.out.println(" "+ch+" "+num);
		    }
		    
		    PriorityQueue<HuffNode> queue1 = new PriorityQueue<HuffNode>();
			queue1.addAll(inputTree.values());
			while(queue1.size()>0){
				@SuppressWarnings("unused")
				HuffNode node1=queue1.poll();
			
		    //System.out.println("Value:" +node1.myImg+"  Frequency:" +node1.myFrequency);
			}
		    
		     ouputTree=constructTree(inputTree);
		     columnN =decompIn.readInt();
		     rowN=decompIn.readInt();
		     maxG=decompIn.readInt();
		    
		   
		    
		    //reads bit by bit of a byte
		    String finalarr="0";
		 
		    while (true) {
		    
		    	String	bin;
		    	try{
		    			bin = Integer.toBinaryString(decompIn.readByte()&0xFF);
		    			
		    		}
		    		catch(Exception e){
		    			
		    			break;
		    		}
		    	char arr[] = bin.toCharArray();
		    	char narr[] = new char[8];
		    
		    	int i=0;
		    	for( i=0;i<8-arr.length;i++)
		    	{   narr[i]='0';
		    	}
		    	for(int j=0;j<arr.length;++j)
		    	{   narr[i]=arr[j];
		    	    i++;
		    	}
		    
		    	finalarr=finalarr+String.valueOf(narr);
		    	//System.out.println(Integer.parseInt(narr.toString()));
		    }    	
		    	//   System.out.println("Final string "+finalarr);
		    //System.out.println("Final string length "+finalarr.length());
		    String arr[] =new String[finalarr.length()];
		    int k=0;
		  
		    int l=2;
		    do{
		   
		         HuffNode curr = ouputTree;
		         for( ;l<finalarr.length();l++){
		        	// System.out.println(finalarr.charAt(l)+"first value");
		         	if (finalarr.charAt(l)== '0'&&curr.myLeft!=null)
				        curr = curr.myLeft;
				      else if(finalarr.charAt(l)== '1'&&curr.myRight!=null)
				       curr = curr.myRight;
				      else {
				    	  l++; 
				    	  break;
		            }
		    	  }
		    
			
		            arr[k]= Integer.toString(curr.myImg);
		           // System.out.println(curr.myImg);
		         k++;
		     
		    }while(l!=finalarr.length());
		  //  System.out.println("Captured Elements "+(k-1)+" Actual Elements: Columns* Rows  ("+columnN +" * " +rowN+" ) ="+(rowN*columnN));
		   
		    String outFile=inputFile.substring(0, inputFile.lastIndexOf('_')) +"_decompressed.pgm";  //new output file name with path
		        FileWriter fw=  new FileWriter(outFile);
		   		BufferedWriter out = new BufferedWriter(fw);
		       out.write("P2"+"\n");
		       out.write(columnN+" "+rowN+"\n");
		       out.write(maxG+"\n");
			  
		       int d=0;
			   for(int m=0;m<rowN;m++){
				 
			    	for(int n=0;n<columnN;n++){
			    		
			    		out.write(arr[d]+" ");	
			    		//System.out.print(arr[d]+" ");
			    		d++;
			    	}
			    	
			    	out.write("\n");
			     }
 
		    decompIn.close();
		    out.write("\n");
		    out.close();
		    long stopTime = System.currentTimeMillis();
		    double elapsedTime = (stopTime - startTime)/1000d;
		   // System.out.println("The time taken:"+elapsedTime+" secs");
		  	System.out.println("Deompressed Image is "+outFile);
		    System.out.println("######### End ##########");
		
		}

	 catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		
		
	}
	

	private static HuffNode constructTree(
			HashMap<Integer, HuffNode> frequencymap) {

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
