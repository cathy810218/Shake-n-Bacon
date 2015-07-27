package shake_n_bacon;

import java.io.IOException;

import providedCode.*;

/**
 * @author <Yi-Ching Oun; Tzu-Wei Chuang>
 * @UWNetID <youn0001; twc5>
 * @studentID <1267202; 1238519>
 * @email <cathy810218@gmail.com; vivi51123@gmail.com>
 * 
 * This Correlator class computes correlation of the given two txt files.
 * It determines the similarities of these two .txt files by calculating
 * the variance using matched words frequencies and their difference.
 */
public class Correlator {

	/**
	 * The main method calculates the variance and prints it out in double format. 
	 * @param args An array of String arguments
	 */
	public static void main(String[] args) {
		if (args.length != 3) { // if its not 3 arguments
			usage();
		}
		
		// Look at the first argument, and check if it's -s or -o
		// two different counters for two different input files
		String firstArg = args[0].toLowerCase();
		DataCounter counter1 = chooseSCorOA(firstArg);
		DataCounter counter2 = chooseSCorOA(firstArg);
		DataCounter counter; 
		
		// Take in second and third inputs, and then pass them into countWords
		// then return the number of total words in each file
		String SecondArg = args[1];
		String ThirdArg = args[2];
		SimpleIterator iter;
		int total_words_file1 = countWords(SecondArg, counter1);
		int total_words_file2 = countWords(ThirdArg, counter2);
	
		// check which has less unique words, use it as the iterator
		if (counter1.getSize() < counter2.getSize()) {
			counter = counter1; 
		} else {
			counter = counter2; 
		}
		iter = counter.getIterator();
		
		double variance = 0.0;
		double differ_sqrt = 0.0;
		while (iter.hasNext()) {
			DataCount word = iter.next();
			int count1 = counter1.getCount(word.data);
			int count2 = counter2.getCount(word.data);
			double freq1 = (double) count1 / total_words_file1;
			double freq2 = (double) count2 / total_words_file2;
			
			// only care when the freq is between 0.0001~0.01
			if ((freq1 < 0.01 && freq1 > 0.0001) && (freq2 < 0.01 && freq2 > 0.0001)) {
				differ_sqrt = (freq1 - freq2) * (freq1 - freq2);
				variance = variance + differ_sqrt;
			}
		}
		System.out.println(variance);
	}
	
	
	/**
	 * This method checks if the user chooses to user separating chaining or open addressing
	 * @param arg This is user's input. It should be either -s or -o, else call usage method
	 * @return DataCounter A counter that counts the number of times you see each piece of data.
	 */
	private static DataCounter chooseSCorOA(String arg){
		StringHasher hash = new StringHasher();
		StringComparator comp = new StringComparator();
		DataCounter counter = null;
		if (arg.equals("-s")) {
			counter = new HashTable_SC(comp, hash);
		} else if (arg.equals("-o")) {
			counter = new HashTable_OA(comp, hash);
		} else {
			usage();
		}
		return counter;
	}
	
	/**
	 * This method prints error message when the user gives wrong input and then exit
	 */
	private static void usage() {
		System.err
				.println("Usage: [-s | -o] <filename> <filename>");
		System.err.println("-s for hashtable with separate chaining");
		System.err.println("-o for hashtable with open addressing");
		System.exit(1);
	}
	
	/**
	 * This countWords method calculates the total number of words in the input file
	 * @param file This is the input file that user passes in
	 * @param counter It's DataCounter that takes in each word from the input file
	 * @return int The total number of word.
	 */
	private static int countWords(String file, DataCounter counter) {
		try {
			int counts = 0;
			FileWordReader reader = new FileWordReader(file);
			String word = reader.nextWord();
			while (word != null) {
				counter.incCount(word);
				word = reader.nextWord();
				counts ++;
			}
			return counts;
		} catch (IOException e) {
			System.err.println("Error processing " + file + " " + e);
			System.exit(1);
		}
		return 0;
	}
	
}
