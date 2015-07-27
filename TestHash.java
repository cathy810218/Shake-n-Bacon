package shake_n_bacon;

import java.io.IOException;
import java.util.Scanner;
import providedCode.*;
/**
 * @author <Yi-Ching Oun; Tzu-Wei Chuang>
 * @UWNetID <youn0001; twc5>
 * @studentID <1267202; 1238519>
 * @email <cathy810218@gmail.com; vivi51123@gmail.com>
 * 
 * The testHash class tests both separate chaining and open addressing hash tables.
 * It takes in user's input
 * 
 */

public class TestHash {

	public static void main(String[] args) {
		System.out.println("This is the beginning of the testing.");
		chooseHashTable();
		getUserInput();
	}
		
	public static void test(DataCounter hashTable){
		int totalCount = countWords("testfile1.txt", hashTable);
		DataCount[] count = getCountsArray(hashTable);
		insertionSort(count, new DataCountStringComparator());
		printDataCount(count);
		System.out.println("Total number of words: " + totalCount);
				
		
	}
	
	private static void chooseHashTable() {
		System.out.println("Enter the number to start testing");
		System.out.println("1: Test OA (Open Addressing)");
		System.out.println("2: Test SC (Separating Chaining");
		System.out.println("3: Exit");
	}
	
	private static void getUserInput(){
		int choice;
		DataCounter hashOA;
		DataCounter hashSC;
		Scanner input = new Scanner(System.in);
		choice = input.nextInt();
		if (choice == 1) {
			System.out.println("This Hash Table is implemented using open addressing: quadratic probing");
			hashOA = new HashTable_OA(new StringComparator(),
					new StringHasher());
			test(hashOA);
			chooseHashTable();
			getUserInput();
		} else if (choice == 2) {
			System.out.println("This Hash Table is implemented using separate chaining");
			hashSC = new HashTable_SC(new StringComparator(),
					new StringHasher());
			test(hashSC);
			chooseHashTable();
			getUserInput();
		} else {
			System.out.println("End of test!");
		}
	}

	private static DataCount[] getCountsArray(DataCounter counter) {
		DataCount[] count = new DataCount[counter.getSize()]; //an array size of unique words
		int index = 0; 
		SimpleIterator itr = counter.getIterator(); 
		while (itr.hasNext()) { 
			DataCount itrnext = itr.next(); 
			count[index] = new DataCount(itrnext.data, itrnext.count); 
			index++; 
		}
		return count;
	}

	private static int countWords(String file, DataCounter counter) {
		try {
			int totalCount = 0;
			FileWordReader reader = new FileWordReader(file);
			String word = reader.nextWord();
			while (word != null) {
				counter.incCount(word);
				word = reader.nextWord();
				totalCount++;
			}
			return totalCount;
		} catch (IOException e) {
			System.err.println("Error processing " + file + " " + e);
			System.exit(1);
		}
		return 0;
	}

	// IMPORTANT: Used for grading. Do not modify the printing *format*!
	private static void printDataCount(DataCount[] counts) {
		for (DataCount c : counts) {
			System.out.println(c.count + "\t" + c.data);
		}
	}

	/*
	 * Sort the count array in descending order of count. If two elements have
	 * the same count, they should be ordered according to the comparator. This
	 * code uses insertion sort. The code is generic, but in this project we use
	 * it with DataCount and DataCountStringComparator.
	 * 
	 * @param counts array to be sorted.
	 * 
	 * @param comparator for comparing elements.
	 */
	private static <E> void insertionSort(E[] array, Comparator<E> comparator) {
		for (int i = 1; i < array.length; i++) {
			E x = array[i];
			int j;
			for (j = i - 1; j >= 0; j--) {
				if (comparator.compare(x, array[j]) >= 0) {
					break;
				}
				array[j + 1] = array[j];
			}
			array[j + 1] = x;
		}
	}
}
