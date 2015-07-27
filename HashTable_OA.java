package shake_n_bacon;

import providedCode.*;

/**
 * @author <Yi-Ching Oun; Tzu-Wei Chuang>
 * @UWNetID <youn0001; twc5>
 * @studentID <1267202; 1238519>
 * @email <cathy810218@gmail.com; vivi51123@gmail.com>
 * 
 *        This HashTable_OA class extends DataCounter, and it is implemented
 *        using quadratic probing (open addressing). This hash table is created
 *        to store information (data and counts) from the given text file.Since
 *        open addressing has bad performance when the load factor gets too
 *        large, it will change its table size with a larger prime number when
 *        the load factor is greater than 0.5. The size of this HashTable is
 *        able to grow at least up to 200,000.
 *
 */
public class HashTable_OA extends DataCounter {
	// declare fields
	private DataCount[] array;
	private int size; // The number of unique data elements in the structure.
	private double loadFactor;
	private Hasher h;
	private Comparator<String> c;
	private int whichSize;
	private static final int[] tableSize = {5, 157, 491, 1381, 3779, 7043, 12611, 102233, 198179, 205129 };
	
	/*
	 * Constructor initializes the fields
	 */
	public HashTable_OA(Comparator<String> c, Hasher h) {
		whichSize = 0;
		array = new DataCount[tableSize[whichSize]];
		size = 0; // number of elements in the table
		loadFactor = 0;
		this.c = c;
		this.h = h;
	}

	/**
	 * Increment the count for a particular data element.
	 * 
	 * @param data
	 *            data element whose count to increment.
	 */
	@Override
	public void incCount(String data) {
		loadFactor = (double) size / tableSize[whichSize];
		// if the number of elements is half as big as the table size, resize it.
		if (loadFactor > 0.5) {
			whichSize++;
			// create a new table with a larger table size
			DataCount[] newArray = new DataCount[tableSize[whichSize]];

			// rehash: iterate through the entire old array table
			SimpleIterator iter = getIterator();
			while (iter.hasNext()) {
				DataCount oldData = iter.next();
				int index = h.hash(oldData.data) % tableSize[whichSize];
				// for quadratic probing
				int i = 1; 
				while (newArray[index] != null) {
					index = (h.hash(oldData.data) + i * i) % tableSize[whichSize];
					i++;
				}
				newArray[index] = oldData; // put the old table's element into new array
			}
			array = newArray;
		}
		// increase its count
		dataAndCount(data).count++;
	}
	
	/**
	 * Get the number of elements in the table
	 * @see providedCode.DataCounter#getSize()
	 */
	@Override
	public int getSize() {
		return size;
	}

	/**
	 * Get the number of count of the passed in data
	 * @param data A string that holds a piece of data (word) from user's input file
	 * @return int Number of count of the passed data
	 */
	@Override
	public int getCount(String data) {
		// call dataAndCount in order to access DataCount and get its count
		DataCount temp = dataAndCount(data);
		return temp.count;
	}

	/**
	 * This function gets the access to both data and its count
	 * @param data A string that holds a piece of data (word) from user's input file
	 * @return DataCount This returns the information of data and count of the passed in data
	 */
	private DataCount dataAndCount(String data) {
		int index = h.hash(data) % tableSize[whichSize];		
		int i = 1; 
		// when there is an element in the array, compare the element and the passed in data to check
		// if they are the same, if they are not the same, do quadratic probing to find next available index.
		while ((array[index] != null) && (c.compare(data, array[index].data) != 0)) {
			// hash code for quadratic probing
			index = (h.hash(data) % tableSize[whichSize] + i * i) % tableSize[whichSize];
			i++;
		}
		// if the data is not in the array, create one and increase the size
		if (array[index] == null) {
			array[index] = new DataCount(data, 0);
			size++; 
		}	
		return array[index];
	}

	/**
	 * Clients must not increment counts between an iterator's creation and its
	 * final use. Data structures need not check this.
	 * 
	 * @return an iterator for the elements.
	 */
	@Override
	public SimpleIterator getIterator() {
		return new iteratorOA();
	}
	
	/**
	 * This iteratorOA is an inner class that implements SimpleIterator.
	 * It will iterate through the hash table
	 * 
	 */
	private class iteratorOA implements SimpleIterator {
		private int index;
		private int currFound;

		/*
		 * constructor initializes the fields
		 */
		public iteratorOA() {
			index = 0;
			currFound = 0; // number of element you already found
		}

		/**
		 * @return DataCount next element in collection
		 * @throws java.util.NoSuchElementException
		 *             if no next element
		 */
		public DataCount next() {
			while (array[index] == null) {
				index++;
			}
			currFound++;
			return array[index++];
		}

		/**
		 * Check if there is more element in the hash table
		 * @return whether there are more elements to iterate through
		 */
		public boolean hasNext() {
			// return true if it hasn't iterated through the last element
			return currFound < size;
		}
	}
}