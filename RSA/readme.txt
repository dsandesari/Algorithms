


This file provides a high-level explanation of RSA Algorithm implementation and additional information that helps the user to run the program.


//************************************/Implementation of RSA Algorithm/*****************************************//

MAIN FUNCTION:

The RSA program consists of a single main function which accepts inputs using command-line arguments:
The program can be executed in 5 parts:

PART ONE:
Description:The first part outputs a ’s’ digit prime to the console by accepting s through the command-line.It also calculates and prints the value of e and phiOfN to the console.
Input:Integer s which is length of the prime number
Output:Two primes p and q of length s.Value of e such that e is relatively prime to PhiOfN and value of PhiOfN = (p-1)(q-1).
Implementation:
randomPrimeGenerator() function is used to generate a random prime number.This function takes input the length of prime number to be generated as input,calculates the range of numbers and selects a random number in the range of given length.
The random number generated must be tested for prime number.This is done by function fermatsTest() which takes the input as random number and checks for the primality of the number.The fermatsTest() function is implementation of Fermats little theorem for testing primality, with low error probability.if a number fails a primality test then randomPrimeGenerator() function generates another random number and is passes to fermatsTest() to test the primality.This process is repeated until the function finds a prime number.
The value of PhiOfN = (p-1)*(q-1)is calculated by using big integer multiply function.
The value of e is calculated such that gcd(e,phiOfN)=1.

PART TWO:
Description:The second part outputs (x,y) such that gcd(a,b) = ax+by to the console by taking the values (a,b) from the command-line.
Input: Two integer values a and b
Output:Three integer values (x,y) and gcd(a,b) such that gcd(a,b) = ax+by
Implementation:
This part of the program implements extended Euclidean algorithm. This is done by calling the extendedEuclideanAlg() function which takes two BigIntegers as input and returns array of BigInteger values x,y and gcd(a,b) such that ax+by=gcd(a,b).

PART THREE:
Description:The third part outputs two integers (d,n) such that {ed=1%(p-1)(q-1), n=p * q } to the console  by taking the values (e,p,q) from the command-line.
Input:Three integers e, p and q.
Output:The public key (d,n) such that  d=1%(p-1)(q-1) and n=p * q 
Implementation:
This part of the program uses extended Euclidean algorithm to calculate d which is a multiplicative inverse of e and 1 mod phiOfN .To the extendedEuclideanAlg() function PhiOfN and e are given as input to return d.This part of the program also calculates n=p * q.

PART FOUR:
Description:The fourth part outputs the encrypted message to the console by taking character ‘e’ , two integers (e ,n) [known as a public key] and the message to be encrypted from the command-line.
Input: character e , integer e , integer n and message to be encrypted.
Output:the encrypted message which is an integer.
Implementation:
The encryptRSA() takes the public key (e,n) and message to be encrypted as input. The function takes the message and stores the ASCII value of each character in a integer and then converts into a single integer representation.The final BigInteger is then encrypted to get the encrypted message by calling modExponent function.This output is printed to the console.

PART FIVE:
Description:The fifth part outputs the decrypted message to the console by taking character ‘d’ , two integers (d , n) [known as a private key] and the message to be decrypted from the command-line.
Input:character d, integer d , integer n and message to be decrypted.
Output:the original decrypted message.
Implementation:
The decryptRSA() takes the private key (d,n) and message to be decrypted as the input.The encrypted string message is decrypted into an BigInteger by calling the modExponent function.The decrypted BigInteger is then converted to ASCII representation and printed to the console.

//************************************/How to run the Program/*****************************************//

Compilation :        javac RSA.java

If an invalid input is given through command line for example: [Java RSA ] then the output would be:
					 Wrong input format! Please enter the correct input in the following format:\n"+
                               			  java RSA  s                {Program outputs a prime with s digit to the console}
                               			  java RSA  a b              {Program outputs (x,y) s.t. gcd(a,b) = ax+by and y>0}
                               			  java RSA  e p q            {Program outputs (d,n) s.t. ed=1%(p-1)(q-1), n=p * q}
                               			  java RSA  ‘e’ e n message  {Program outputs the encrypted message              }
                               			  java RSA  ‘d’ d n message  {Program outputs the decrypted message              }

TO RUN PART ONE: 
			/*   Fermat’s test to generate large prime numbers with high confidence
			 *   @Input:  The size of the prime number 
			 *   @Output: a prime number of the given size, n and phiOfN
			 */

		Input: java RSA 20
		Output:
			Generating 20 Digit Prime Numbers..

 			The Prime Number P of length 20 is:  15653601142766628133

 			The Prime Number Q of length 20 is:  11737498594999943807

			n= P*Q = 183734121419912812501165957649865322331

			phiOfN= (P-1)*(Q-1) = 183734121419912812473774857912098750392

			e=17
			

TO RUN PART TWO: 
			/*   Extended Euclidean Algorithm
			 *   @Input:  Two int (a,b) --> {phiOfN,e} //PhiOfN and e values are calculated from PART ONE.
			 *   @Output: Two int (x,y) and gcd(a,b) such that gcd(a,b) = ax+by and y>0.
			 */
		Input: java RSA 183734121419912812473774857912098750392 17
		Output:
			Calling Extended Euclidean Algorithm of two numbers (a=183734121419912812473774857912098750392, b=17)

 			x=-6, y=64847336971733933814273479263093676609 and gcd(a,b)=1=ax+by




TO RUN PART THREE: 
			/*   Program to calculate d a multiplicative inverse of e and 1 mod phiOfN
			 *   @Input: Three integers e p and q    // e is calculated in PART ONE
			 *   @Output:Private key (d,n) [s.t. ed=1%(p-1)(q-1), n=pq]
			 */
		Input: java RSA 17 15653601142766628133 11737498594999943807
		Output:
			e=17 p=15653601142766628133 q=11737498594999943807

 			d=64847336971733933814273479263093676609 n=183734121419912812501165957649865322331




TO RUN PART FOUR: 
			/*    RSA encryption
			 *    @input: char e , int e, int n ,message to be encrypted // e and n is calculated in PART ONE.
			 *    @output:The encrypted message		
			 */
		Input:java RSA e 17 183734121419912812501165957649865322331 abcdefghijklmno
 		Output:
			Encrypting the message= abcdefghijklmno using the public key (e , n) = ( 17 , 183734121419912812501165957649865322331 )
			The Encrypted Message is: 7080186182344282870235711934470422854




TO RUN PART FIVE: 
			/*    RSA decryption
			 *    @input: char d , int e, int n ,message to be decrypted   // e is calculated from PART ONE and d ,n is calculated in PART THREE, message is from PART FOUR.
			 *    @output:The decrypted message		
			 */
		Input:java RSA d 64847336971733933814273479263093676609 183734121419912812501165957649865322331 7080186182344282870235711934470422854
		Output:
			Decrypting the message= 7080186182344282870235711934470422854 using the private key (d , n) = ( 64847336971733933814273479263093676609 ,183734121419912812501165957649865322331 )
			The Decrypted Message is: abcdefghijklmno








																					
																					
																			~ DEEKSHITH SANDESARI		



																			





