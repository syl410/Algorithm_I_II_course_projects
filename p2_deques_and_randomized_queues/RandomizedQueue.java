import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int size; // current size of queue
	private Item[] randomq;

	// construct an empty randomized queue
	public RandomizedQueue(){
		size = 0;
		randomq = (Item[]) new Object[1]; // ### wrong: new Item[1];
	}

	// is the randomized queue empty?
	public boolean isEmpty(){
		return size == 0;
	}

	// return the number of items on the randomized queue
	public int size(){ 
		return size;
	}

	// add the item
	public void enqueue(Item item){
		if(size == randomq.length){
			resize(2*size);
		}
		randomq[size++] = item;
	}

	// remove and return a random item
	public Item dequeue(){
		if(size == 0){
			throw new java.util.NoSuchElementException();
		}

		int randomNum = (int) (StdRandom.uniform() * size);
		Item ditem = randomq[randomNum];
		randomq[randomNum] = randomq[--size];
		randomq[size] = null;
		if(size < randomq.length/4){
			resize(randomq.length/2);
		}
		return ditem;
	}

	// return a random item (but do not remove it){}
	public Item sample(){
		if(size == 0){
			throw new java.util.NoSuchElementException();
		}

		int randomNum = (int) (StdRandom.uniform() * size);
		return randomq[randomNum];
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator(){
		return new RandqIterator();
	}

	private class RandqIterator implements Iterator<Item> {
		private int itr_num; // number of iteration times 
		private int[] randomOrder;
		
		public RandqIterator() {
			itr_num = 0;
			randomOrder = new int[size];
			for(int i = 0; i < size; i++) {
				randomOrder[i] = i;
			}
			StdRandom.shuffle(randomOrder);
		}
		
		public boolean hasNext(){
			return itr_num < size;
		}
		public Item next() {
			if(!hasNext()){
				throw new java.util.NoSuchElementException(); // ### throw, not return!
			}
			
			return randomq[randomOrder[itr_num++]];
		}
		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}

	// resize array
	private void resize(int newSize){
		Item[] tmp_array = (Item[]) new Object[newSize];
		for(int i = 0; i < size; i++){
			tmp_array[i] = randomq[i];
		}
		randomq = tmp_array;
	}

	// unit testing (optional)
	public static void main(String[] args){
		RandomizedQueue<Integer> testq1 = new RandomizedQueue<Integer>(); // need to use Integer instead of int
		System.out.println("enqueue 1-20 to testq1");
		for(int i = 0; i < 20; i++){
			testq1.enqueue(i);
		}
		System.out.println("print testq1");
		Iterator<Integer> itr1 = testq1.iterator();
		while(itr1.hasNext()){
			System.out.print(" " + itr1.next());
		}
		System.out.println("");
		
		System.out.println("dequeue testq1: " + testq1.dequeue());
		System.out.println("print testq1");
		Iterator<Integer> itr2 = testq1.iterator();
		while(itr2.hasNext()){
			System.out.print(" " + itr2.next());
		}
		System.out.println("");

		System.out.println("sample testq1");
		System.out.println(" " + testq1.sample());
		System.out.println("print testq1");
		Iterator<Integer> itr3 = testq1.iterator();
		while(itr3.hasNext()){
			System.out.print(" " + itr3.next());
		}
		System.out.println("");
		
	}

}
