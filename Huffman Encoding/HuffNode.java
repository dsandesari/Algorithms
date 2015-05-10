


public class HuffNode implements Comparable <HuffNode>{
	public int myImg;
	public int myFrequency;
	public HuffNode myLeft, myRight;
	public HuffNode(int newmyImg,int newmyFrequency) {
		myImg=newmyImg;
		myFrequency=newmyFrequency;
		myLeft= myRight=null;
	}
	
	public HuffNode(HuffNode left,HuffNode right) {
		myImg=-1;
		myFrequency=left.myFrequency+right.myFrequency;
		myLeft=left;
		myRight=right;
		
	   
	}

	public int compareTo(HuffNode o) {
		// TODO Auto-generated method stub
		 if(this.myFrequency>o.myFrequency)
	    	  return 1;
	      if(this.myFrequency<o.myFrequency)
	    	  return -1;
	     if( this.myImg>o.myImg)
	          return 1;
	      return -1;
	}

	
  
}
