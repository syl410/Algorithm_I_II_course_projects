import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
	
	private Node<Item> first; // ### <Item>
	private Node<Item> last;
	private int size;

	private class Node<Item> {
		Item item;
		Node<Item> next;
		Node<Item> before;

		Node (Item item, Node<Item> next, Node<Item> before){
			this.item = item;
			this.next = next;
			this.before = before;
		}
	}

	// construct an empty deque
	public Deque() {
		first = null;
		last = null;
		size = 0;
	}

	// is the deque empty?
	public boolean isEmpty() {
		if (first == null) {
			return true;
		} else {
			return false;
		}
	}

	// return the number of items on the deque
	public int size() {
		return size;
	}

	// add the item to the front
	public void addFirst(Item item) {
		if (item == null){
			throw new java.lang.IllegalArgumentException(); // ### ()
		}

		Node<Item> oldFirst = first;
		first = new Node<Item> (item, oldFirst, null);
		if (size == 0) {
			last = first;
		} else {
			oldFirst.before = first;
		}
		size ++;
	}

	// add the item to the end
	public void addLast(Item item) {
		if (item == null) {
			throw new java.lang.IllegalArgumentException();
		}

		Node<Item> oldLast = last;
		last = new Node<Item> (item, null, oldLast);
		if (size == 0) {
			first = last;
		} else {
			oldLast.next = last;
		}
		size ++;
	}

	// remove and return the item from the front
	public Item removeFirst() {
		if (isEmpty()) { // ### ()
			throw new java.util.NoSuchElementException();
		}

		Item item = first.item;
		if (size == 1) {
			first = last = null;
		} else {
			first = first.next;
			first.before = null;
		}
		
		size --;
		return item;
	}

	// remove and return the item from the end
	public Item removeLast() {
		if (isEmpty()) {
			throw new java.util.NoSuchElementException();
		}

		Item item = last.item;
		if (size == 1) {
			last = first = null;
		} else {
			last = last.before;
			last.next = null;
		}

		size --;
		return item;
	}

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator() {
		return new DequeIterator(); // Iterator<Item> is a class. DequeIterator is class object.
					    // ### DequeIterator doesn't need <>
	}

	private class DequeIterator implements Iterator<Item> {
		Node<Item> current = first;

		public boolean hasNext(){
			if(current == null) {
				return false;
			} else {
				return true;
			}
		}

		public Item next() {
			if(hasNext()) {
				Item current_item = current.item;
				current = current.next;
				return current_item;
			} else {
				throw new java.util.NoSuchElementException();
			}
		}

		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}

	// unit testing (optional)
	public static void main(String[] args) {
		Deque<Integer> testQ = new Deque<Integer>(); // ### need ()
		for(int i = 0; i < 20 ; i++){
			testQ.addFirst(i);
		}
		System.out.println("after addFirst 1-20: ");
		// ### DequeIterator itr = testQ.iterator(); // error for it is non-static
		Iterator<Integer> itr = testQ.iterator();

		while(itr.hasNext()) { // ### ()
			System.out.print(" " + itr.next());
		}
		System.out.println("");

		System.out.println("remove first 10: ");
		for(int i = 0; i < 10 ; i++){
			testQ.removeFirst();
		}
		itr = testQ.iterator();
		while(itr.hasNext()) {
			System.out.print(" " + itr.next());
		}
		System.out.println("");

		System.out.println("remove last 5: ");
		for(int i = 0; i < 5 ; i++){
			testQ.removeLast();
		}
		itr = testQ.iterator();
		while(itr.hasNext()) {
			System.out.print(" " + itr.next());
		}
		System.out.println("");

		System.out.println("add last 5 \"1\": ");
		int one = 1;
		for(int i = 0; i < 5 ; i++){
			testQ.addLast(one);
		}
		itr = testQ.iterator();
		while(itr.hasNext()) {
			System.out.print(" " + itr.next());
		}
		System.out.println("");

	}
}
