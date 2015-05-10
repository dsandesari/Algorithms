

import java.math.BigInteger;
import java.util.*;
public class RSA {
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        
		/*
		 * Java RSA  s                {Program outputs a prime with s digit to the console}
		 * Java RSA  a b              {Program outputs (x,y) s.t. gcd(a,b) = ax+by and y>0}
		 * Java RSA  e p q            {Program outputs (d,n) s.t. ed=1%(p-1)(q-1), n=pq   }
		 * Java RSA  ‘e’ e n message  {Program outputs the encrypted message              }
		 * Java RSA  ‘d’ d n message  {Program outputs the decrypted message              }
		 *
		 */
        
		int argumentLength= args.length;
        
		if (argumentLength==0||argumentLength>4) {
			
			System.out.println(" Wrong input format! Please enter the correct input in the following format:\n"+
                               "\n  Java RSA  s                {Program outputs a prime with s digit to the console}"+
                               "\n  Java RSA  a b              {Program outputs (x,y) s.t. gcd(a,b) = ax+by and y>0} "+
                               "\n  Java RSA  e p q            {Program outputs (d,n) s.t. ed=1%(p-1)(q-1), n=pq   }"+
                               "\n  Java RSA  ‘e’ e n message  {Program outputs the encrypted message              }"+
                               "\n  Java RSA  ‘d’ d n message  {Program outputs the decrypted message              }");
            
		}
		if (argumentLength==1) {
            
			/* Fermat’s test to generate large prime numbers with high confidence
			 * @param: the size of the prime number
			 * @return: a prime number of the given size, n and phiOfN
			 */
            
			int lengthOfPrime=Integer.parseInt(args[0]);
			System.out.println("\nGenerating "+lengthOfPrime+" Digit Prime Numbers..");
			BigInteger randomPrimeP=null;
			BigInteger randomPrimeQ=null;
            randomPrimeP=randomPrimeGenerator(lengthOfPrime);
			randomPrimeQ=randomPrimeGenerator(lengthOfPrime);
            
			System.out.println("\n The Prime Number P of length "+lengthOfPrime+" is:  "+randomPrimeP);
			System.out.println("\n The Prime Number Q of length "+lengthOfPrime+" is:  "+randomPrimeQ);
			BigInteger n=randomPrimeP.multiply(randomPrimeQ);
			System.out.println("\n n= P*Q = "+n);
			BigInteger phiOfN=(randomPrimeP.subtract(BigInteger.ONE)).multiply(randomPrimeQ.subtract(BigInteger.ONE));
			System.out.println("\n phiOfN= (P-1)*(Q-1) = "+phiOfN);
            
			//  Select an odd integer e that is relatively prime to phi(n) = (p-1)(q-1);
			BigInteger e=BigInteger.valueOf(15);
			BigInteger[] extendedEuclidean=null;
			do{
				e=e.add(BigInteger.valueOf(2));
				extendedEuclidean=extendedEuclideanAlg(phiOfN,e);
			}while(extendedEuclidean[2]==BigInteger.ONE);
            
			System.out.println("\n e="+e);
            
		}
		if (argumentLength==2) {
			/* Extended Euclidean Algorithm
			 * @param: two int (a,b) --> {phiOfN,e}
			 * @return: two int (x,y) and gcd(a,b) such that gcd(a,b) = ax+by and y>0.
			 */
			BigInteger a = new BigInteger(args[0]);
			BigInteger b = new BigInteger(args[1]);
			System.out.println("\n Calling Extended Euclidean Algorithm of two numbers (a="+a+", b="+b+")");
			BigInteger[] extendedEuclidean=extendedEuclideanAlg(a,b);
			System.out.println("\n x="+extendedEuclidean[0]+", y="+extendedEuclidean[1]+" and gcd(a,b)="+extendedEuclidean[2]+"=ax+by");
            
		}
		if (argumentLength==3) {
            
			/*Program to calculate d a multiplicative inverse of e and 1 mod phiOfN
			 * input three integers e p and q
			 * your program should output (d,n) s.t. ed=1%(p-1)(q-1), n=pq
			 */
			BigInteger e = new BigInteger(args[0]);
			BigInteger p = new BigInteger(args[1]);
			BigInteger q = new BigInteger(args[2]);
			System.out.println("\n e="+e+" p="+p+" q="+q);
            
			BigInteger n=p.multiply(q);//   n= P*Q
            
			BigInteger phiOfN=(p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));// phiOfN= (P-1)*(Q-1)
            
			// calculating d: a multiplicative inverse of e and 1 mod phiOfN
			BigInteger d=null;
			BigInteger[] extendedEuclidean=extendedEuclideanAlg(phiOfN,e);
			if(extendedEuclidean[1].compareTo(BigInteger.ZERO)<0)
				d=extendedEuclidean[1].add(phiOfN);
			else
				d=extendedEuclidean[1];
			System.out.println("\n d="+d+" n="+n);
            
		}
		if (argumentLength==4) {
			BigInteger e,n,d = BigInteger.valueOf(13);
			String m=null;
            
			if (args[0].equalsIgnoreCase("e") ){
                
				/*     RSA encryption
				 *     @param: int pair (e,n): public key
				 *     string : the message to be encrypted
				 *     @return: string : the encrypted message
				 */
                
				e = new BigInteger(args[1]);
				n = new BigInteger(args[2]);
				m = args[3];
				System.out.print(" Encrypting the message= "+m+" using the public key (e , n) = ( "+e+" , "+n+" )");
				encryptRSA(m,e,n);//calling RSA encryption
                
                
			}
			else if (args[0].equalsIgnoreCase("d")){
				/*       RSA decryption
				 * 		 @param: int pair (d,n): private key
				 * 		 string : message to be decrypted
				 * 		 @return: string : the decrypted message
				 */
				d = new BigInteger(args[1]);
				n = new BigInteger(args[2]);
				m = args[3];
				System.out.format(" Decrypting the message= "+m+" using the private key (d , n) = ( "+d+" ,"+n+" )");
				decryptRSA(m,d,n);//calling RSA decryption
                
			}
		}
        
        
        
	}
    
	/*
	 * The random Prime Generator:
	 * This function generates a random BigInteger and then calls the fermats function.
	 * Fermats function checks whether the BigInteger is prime or not.
	 */
	private static BigInteger randomPrimeGenerator(int lengthOfPrime) {
		BigInteger probablePrime=null;
		do{
			BigInteger big=new BigInteger("10");
			big=big.pow(lengthOfPrime);
			big=big.subtract(BigInteger.ONE);//Greatest number in the range of Probable Prime.
			int bigLength=big.bitLength();//Bit Length of Greatest Probable Prime.
			BigInteger small=new BigInteger("10");
			small=small.pow(lengthOfPrime-1);//Least number in the range of Probable Prime.
            
			/* Random Number in the Range of small and Big(Probable Prime) */
			Random randomNumber = new Random();
			probablePrime = new BigInteger(bigLength, randomNumber);//generates a random number of particular bitlength
			while( probablePrime.compareTo(small) == -1 || probablePrime.compareTo(big) == 1 ) {
				probablePrime = new BigInteger(bigLength, randomNumber);
			}//probable prime should be a number in between small and big
            
            
			/* Fermats Test to find the BigInteger is Prime */
			Boolean isPrime=false;
			isPrime=fermatsTest(probablePrime);
			if (isPrime==false)
				probablePrime=null;
			else
				break;
            
		}while(probablePrime==null);
        
		return probablePrime;
	}
    
    
	/*
	 * Function primality2(N) Input: Positive integer N Output: yes/no
	 * Pick positive integers a1, a2, . . . , ak < N at random if a^(N −1) ≡ 1 (mod N ) for all i = 1, 2, . . . , k:
	 * return yes else:
	 * return no
	 */
    
	private static Boolean fermatsTest(BigInteger probablePrime) {
        
		Boolean isPrime=false;
		for(int i=2;i<4;i++){
            
			BigInteger n=BigInteger.valueOf(i);
			BigInteger b=probablePrime.subtract(BigInteger.ONE);
			BigInteger a=modExponent(n,b,probablePrime);
			a=a.subtract(BigInteger.ONE);
            
			if((a.mod(probablePrime)).compareTo(BigInteger.ZERO)==0)
				isPrime=true;  //probable prime is a prime
			else{
				isPrime=false ;//Probable Prime is not a prime
				break;
			}
            
		}
        
		return isPrime;
	}
    
	/* Computing x^y mod m , m>0 by  repeated squaring algorithm:
	 * mod-exp(x, y, m)
	 * if y = 0 then return(1)
	 * else
	 * z = mod-exp(x, y div 2, m)
	 * if y mod 2 = 0 then return(z * z mod m)
	 * else return(x * z * z mod m)
	 */
	private static BigInteger modExponent(BigInteger x, BigInteger y, BigInteger m)  {
        
		if(y.compareTo(BigInteger.ZERO)==0)
			return(BigInteger.ONE);
		BigInteger z = modExponent(x, y.divide(BigInteger.valueOf(2)), m);
        
		if ((y.mod(BigInteger.valueOf(2))).compareTo(BigInteger.ZERO)==0 )
			return((z.multiply(z)).mod(m));
		else
			return((x.multiply((z.multiply(z)).mod(m))).mod(m).mod(m));
        
	}
    
	/*
	 *  Extended Eculidean Algorithm Implementation
	 *  Input: Two positive integers a and b with a ≥ b ≥ 0
	 *  Output: Integers x,y,d such that d=gcd(a,b) and ax+by=d
	 *  if b = 0: return (1,0,a)
	 *  (x′, y′, d) = Extended-Euclid(b, a mod b)
	 *  return (y′, x′ − ⌊a/b⌋y′, d)
	 */
	//O(k × log2n × log log n × log log log n)
	private static BigInteger[] extendedEuclideanAlg(BigInteger a, BigInteger b) {
        
		BigInteger [ ] temp = new BigInteger[3];
		if (b.compareTo(BigInteger.ZERO)==0) {
			temp[0] = BigInteger.ONE;
			temp[1] = BigInteger.ZERO;
			temp[2] = a;
			return temp;
		}
        
		temp = extendedEuclideanAlg (b, a.mod (b));
		BigInteger x = temp[0];
		BigInteger y = temp[1];
		BigInteger d = temp[2];
        
		temp[0] = y;
		temp[1] = x.subtract (y.multiply(a.divide (b)));
		temp[2] = d;
        
		return temp;
        
	}
	/*
	 * Encryption function:
	 * It stores the ASCII value of each character in a integer and converts an integer representation
	 * The final BigInteger is then encrypted to get the encrypted message
	 */
	private static BigInteger encryptRSA(String message,BigInteger e, BigInteger n) {
        
		BigInteger k=BigInteger.ZERO;
		BigInteger temp=BigInteger.ONE;
		BigInteger encryptedMessage =null;
        
		for (int i=0,l=message.length()-1;i<message.length();i++,l--){
			temp=BigInteger.valueOf(message.charAt(i));//ASCII value of each char is stored.
			k=k.add(temp.multiply((BigInteger.valueOf(128)).pow(l)));//The message is represented as a single integer value.
		}
		encryptedMessage = modExponent(k,e,n);// Calculating m^e mod n
		System.out.println("\nThe Encrypted Message is: "+encryptedMessage);
        
		return encryptedMessage;
	}
	/*
	 * Decryption function:
	 * The encrypted string message is decrypted into an BigInteger
	 * The decrypted BigInteger is then converted to ASCII representation
	 */
	private static void decryptRSA(String m,BigInteger d, BigInteger n) {
		BigInteger temp1=new BigInteger(m);
		BigInteger decrypt=modExponent(temp1,d,n);// Calculating C^d mod n
		BigInteger remainder;
		int i=1;
		char[] result =new char[100];
		//Getting the ASCII value of the integer message	
		do{
			remainder=decrypt.mod((BigInteger.valueOf(128)));
			decrypt=decrypt.divide(BigInteger.valueOf(128));
			result[i-1]=(char) (remainder.intValue());
			i++;
		}while(remainder.compareTo(BigInteger.ZERO)>0);
		//Storing each character of decrypted message in a string variable
		String output=" ";
		for(int k=i-2;k>=0;k--){
			output=output+result[k];
		}
		System.out.println("\nThe Decrypted Message is:"+output);
	}
    
    
}
