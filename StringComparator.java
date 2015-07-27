package shake_n_bacon;

import providedCode.*;

/**
 * @author <Yi-Ching Oun; Tzu-Wei Chuang>
 * @UWNetID <youn0001; twc5>
 * @studentID <1267202; 1238519>
 * @email <cathy810218@gmail.com; vivi51123@gmail.com>
 * 
 * This StringComparator implements Comparator<String>. 
 * It compares two strings and check if these two strings are the same.
 */
public class StringComparator implements Comparator<String> {

	/**
	 * This compare methods compare two strings and check if they are the same.
	 * If they are not the same, find which one comes first.
	 * 
	 * @param s1 The first input string
	 * @param s2 The second input string
	 * @return int If the two strings are the same, return 0; if the string s1 
	 * 			comes first alphabetically, return -1, else return 1.
	 * @see providedCode.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String s1, String s2) {
		int index = 0;
		int s1_len = s1.length();
		int s2_len = s2.length();
		
		while (s1_len > index && s2_len> index) {
			int difference = s1.charAt(index) - s2.charAt(index);
			if (difference != 0){ 
				return difference;
			} else {
				index ++;
			}
		}
	
		// if these two strings have the same characters, check if they have the same length
		if (s1_len == s2_len){
			return 0;
		} else if (s1_len > s2_len) {
			return 1;
		} else {
			return -1;
		}
	}
}
