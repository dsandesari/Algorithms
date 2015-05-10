
public class UnionFind {

	private int[] array;

	//Initializes single dimension array of size (s * s)

	public UnionFind(int numElements) {
		array = new int [numElements];
		for (int i = 0; i < numElements; i++) {
			array[i] = i;
		}
	}

	// union() joins two sets into a single set

	public void union(int a, int b) {
		int root_a = find(a);//finds root of a
		int root_b = find(b);//finds root of b
		if (root_a != root_b) //if roots of the two pairs are not equal
			array[root_a] = root_b;//make one the root of other

	}

	//  find() returns traverses along the path of the  tree and returns the root

	int find(int x) {
        int y=x;
		while (x!=array[x])
		{
            array[x] =array[array[x]];//path compression
		        x = array[x];
            
		}
        array[y]=x;
		return x;  // At root
	}



	//getarray() is used to get the array of sets.
	public  int[] getarray(){
		return array;

	}


}


