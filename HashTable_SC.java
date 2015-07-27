package shake_n_bacon;

import providedCode.*;

/**
 * @author <Yi-Ching Oun; Tzu-Wei Chuang>
 * @UWNetID <youn0001; twc5>
 * @studentID <1267202; 1238519>
 * @email <cathy810218@gmail.com; vivi51123@gmail.com>
 * 
 * A HashTable implemented with separate chaining that resize/rehash itself
 * when load factor > 2. The HashTable is able to grow at least up to 200,000.
 * Its capacity is always a prime number. It uses the hashing strategy of the 
 * ASCII character codes multiply by its position in the string. 
 * An inner class for the node object is used. 
 */
public class HashTable_SC extends DataCounter {

	private HashNode[] keyTable; //the table at some index
	private int size; //The number of unique data elements in the structure.
	private Hasher h; 
	private Comparator<String> c;
	private int whichSize;
	private double loadFactor; 
	private static final int[] tableSize = {5, 157, 491, 1381, 3779, 7043, 12611, 102233, 198179, 205129}; 
	
	/**
	 * Constructor initialization
	 * @param c
	 * @param h
	 */
	public HashTable_SC(Comparator<String> c, Hasher h) {
		whichSize = 0;
		keyTable = new HashNode[tableSize[whichSize]]; //index 0 gives 5, 1 gives 11...etc
		size = 0;
		this.c = c;
		this.h = h;
		loadFactor = 0; 
	}

	/**
	 * This inner class creates a hash node holding a DataCount node and a next field pointer 
	 */
	private class HashNode {
		public DataCount node; // data stored in this node
		public HashNode next; // link to next node in the list
	
		// post: constructs a node with given data and null link
		public HashNode(DataCount node) {
			this(node, null);
		}
	
		// post: constructs a node with given data and given link
		public HashNode (DataCount node, HashNode next) {
			this.node = node;
			this.next = next;
		}
	}
	
	/**
	 * Increment the count for a particular data element.
	 * @param data: element whose count to increment.
	 */
	@Override
	public void incCount(String data) {
		//calculate load factor
		loadFactor = ((double) size) / tableSize[whichSize]; 
		//check if the table size if enough, if not then whichSize++, use a different table 
		if (loadFactor > 2) {
		//resize the table and put them into the new table
			whichSize++; 
			HashNode[] newKeyTable = new HashNode[tableSize[whichSize]]; 
			//iterate through every node in the table, including the ones in the link list
			for (HashNode pointer : keyTable) { //iterate through the old table
					while (pointer != null) { 
						//put each node in the new table 
						int index = h.hash(pointer.node.data) % tableSize[whichSize];
						
						if (newKeyTable[index] != null) { //let the newest node points to the older nodes
							newKeyTable[index] = new HashNode(pointer.node, newKeyTable[index]); 
						} else { //no element in that bucket yet
							newKeyTable[index] = new HashNode(pointer.node); 
						}
						pointer = pointer.next;
					}
			} 
			keyTable = newKeyTable; 
		}
		//no resizing necessary, somehow find the data's corresponding count and increment that count
		int index = h.hash(data) % tableSize[whichSize];
		boolean found = false; 
		if (keyTable[index] != null) { //if there's element in that bucket 
				HashNode pointer = keyTable[index]; //keyTable[index] == HashNode
				while (pointer != null) { 
					if (c.compare(data, pointer.node.data) == 0) { 
						//you have found it, increment its count
						pointer.node.count++; 
						found = true; 
					}
					pointer = pointer.next;
				}
				if (!found) {
					//element not in the bucket, therefore create a node and add it into the bucket
					keyTable[index] = new HashNode(new DataCount(data, 1), keyTable[index]); 
					size++; 
				}
		} else { //no element in that bucket yet, create this node and give it a count of 1
			keyTable[index] = new HashNode(new DataCount(data, 1)); 
			size++; 
		}
	}

	/**
	 * Returns the current number of elements (size) in the HashTable
	 */
	@Override
	public int getSize() {
		return size;
	}

	/**
	 * The current count for the data, 0 if it is not in the counter.
	 */
	@Override
	public int getCount(String data) {
		int count = 0; 
		int index = h.hash(data) % tableSize[whichSize];
		if (keyTable[index] != null) { //if there's element in that bucket 
				HashNode pointer = keyTable[index]; //keyTable[index] == HashNode
				while (pointer != null) { 
					if (c.compare(data, pointer.node.data) == 0) { 
						//you have found it
						count = pointer.node.count;
					}
					pointer = pointer.next;
				}
		}
		return count; 
	}
		
	/**
	 * Clients must not increment counts between an iterator's creation and its
	 * final use. Data structures need not check this.
	 * @return an iterator for the elements.
	 */
	@Override
	public SimpleIterator getIterator() {
		SimpleIterator itr = new iteratorSC(); 
		return itr;
	}
	
	/**
	 * This iteratorSC is an inner class that implements SimpleIterator.
	 * It will iterate through the hash table
	 */
	private class iteratorSC implements SimpleIterator {
		private int index;
		private int currFound; 	//number of nodes we have iterated through
		private HashNode pointer; //has a dataCount and a next field
		/*
		 * constructor initializes the fields 
		 */
		public iteratorSC() { 
			index = 0; 
			currFound = 0; 
			while(index < (tableSize[whichSize] - 1) && keyTable[index] == null) {
				index++;
			} 
			pointer = keyTable[index];
			//pointer will be pointing to the first non-null element
		}
		/**
		 * @return next element in collection
		 * @throws java.util.NoSuchElementException
		 *             if no next element
		 */
		public DataCount next() { 
			HashNode temp = pointer; 
			currFound++; 
			if (hasNext()) {
				if (pointer.next == null) { //if the next bucket is null, go find the next available bucket
					index++; 
					while(index < (tableSize[whichSize]) && keyTable[index] == null) {
						index++;
					}
					pointer = keyTable[index]; 
				} else { //in the same bucket, iterate the next node
					pointer = pointer.next; 
				}
			}	
			return temp.node; 
		}

		/**
		 * @return whether there are more elements to iterate through
		 */
		public boolean hasNext(){
			// return true if it has a next element
			return (currFound < size); 
		}
	}   
}