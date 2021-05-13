import edu.princeton.cs.algs4.StdIn;
import java.util.NoSuchElementException;

public class Permutation {
   public static void main(String[] args){
   	int k = Integer.parseInt(args[0]);
	if(k < 0){
		throw new java.lang.IllegalArgumentException();
	}
	RandomizedQueue<String> randomQ = new RandomizedQueue<String>();
	while(!StdIn.isEmpty()){
		randomQ.enqueue(StdIn.readString());
	}
	for(int i = 0; i < k; i++){
		System.out.println(randomQ.dequeue()); // ### don't forget ()
	}
   }
}
