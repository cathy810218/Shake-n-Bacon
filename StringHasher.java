package shake_n_bacon;

import providedCode.Hasher;

/**
 * @author <Yi-Ching Oun; Tzu-Wei Chuang>
 * @UWNetID <youn0001; twc5>
 * @studentID <1267202; 1238519>
 * @email <cathy810218@gmail.com; vivi51123@gmail.com>
 * 
 * This StringHasher class implements Hasher. 
 * It converts a string into an unique hash code.
 * 
 */
public class StringHasher implements Hasher {

	/**
	 * This hash class calculates the summation of the corresponding ASCII code 
	 * multiplying by a prime number 37 to the power of the index in order to get
	 * an unique hash code.
	 * @param str It's a string that needs to be converts to ASCII code
	 * @return int The hash code value
	 */
	@Override
	public int hash(String str) {
		int hashCode = 0;
		for (int i = 0; i < str.length(); i++) {
			hashCode += (str.charAt(i)) * (int) Math.pow(37, i);
		}
		return Math.abs(hashCode);
	}
}
