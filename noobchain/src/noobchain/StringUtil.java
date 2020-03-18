/**
 * @author jasontiller
 *
 */
package noobchain; // this file is part of the noobchain package

// import needed security and utility modules: Key, MessageDigest, PrivateKey, PublicKey, Signature, Arraylist, Base64, and List
import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

// import GsonBuilder module
import com.google.gson.GsonBuilder;

// A class for creating StringUtil objects
public class StringUtil {

	// Applies ECDSA Signature and returns the result as a byte array.
	public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
		// Creates a Signature object called 'dsa'
		Signature dsa;
		// Creates and initializes a new byte array called 'output'
		byte[] output = new byte[0];
		// The compiler will attempt to run the code in the 'try' block
		try {
			// Initialize the signature with an instance of ECDSA
			dsa = Signature.getInstance("ECDSA", "BC");
			// Sign the signature with the private key
			dsa.initSign(privateKey);
			/*
			 * Create and initialize a new byte array called 'strByte' to the value of the
			 * bytes in the input string
			 */
			byte[] strByte = input.getBytes();
			// Update the signature with the strByte byte array
			dsa.update(strByte);
			/*
			 * Create and initialize a new byte array called 'realSig' to the value of the
			 * signed signature
			 */
			byte[] realSig = dsa.sign();
			// Assign the value of realSig to the output byte array
			output = realSig;
		}
		/*
		 * if the compiler is unable to run the code in the 'try' block, it will run the
		 * code in the 'catch' block
		 */
		catch (Exception e) {
			// throw a new runtime exception
			throw new RuntimeException(e);
		}
		// return the value of the 'output' variable to the calling function
		return output;
	}

	// Applies SHA1withDSA Signature and returns the result as a byte array.
	public static byte[] applySHA1withDSA(PrivateKey privateKey, String input) {
		// Creates a Signature object called 'dsa'
		Signature dsa;
		// Creates and initializes a new byte array called 'output'
		byte[] output = new byte[0];
		// The compiler will attempt to run the code in the 'try' block
		try {
			// Initialize the signature with an instance of SHA1withDSA
			dsa = Signature.getInstance("SHA1withDSA", "BC");
			// Sign the signature with the private key
			dsa.initSign(privateKey);
			/*
			 * Create and initialize a new byte array called 'strByte' to the value of the
			 * bytes in the input string
			 */
			byte[] strByte = input.getBytes();
			// Update the signature with the strByte byte array
			dsa.update(strByte);
			/*
			 * Create and initialize a new byte array called 'realSig' to the value of the
			 * signed signature
			 */
			byte[] realSig = dsa.sign();
			// Assign the value of realSig to the output byte array
			output = realSig;
		}
		/*
		 * if the compiler is unable to run the code in the 'try' block, it will run the
		 * code in the 'catch' block
		 */
		catch (Exception e) {
			// throw a new runtime exception
			throw new RuntimeException(e);
		}
		// return the value of the 'output' variable to the calling function
		return output;
	}

	// Applies Sha256 to a string and returns the result as a String
	public static String applySha256(String input) {
		// The compiler will attempt to perform the code inside the 'try' section
		try {
			// Create a new MessageDigest object using SHA-256
			MessageDigest digest = MessageDigest.getInstance("SHA-256");

			// Create a new byte array to hold the SHA-256 digest using "UTF-8" encoding
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			// Create a new StringBuffer object called 'hexString' to store the hash
			StringBuffer hexString = new StringBuffer();
			// For each member of the byte [] ...
			for (int i = 0; i < hash.length; i++) {
				// Create a hexidecimal value for each member
				String hex = Integer.toHexString(0xff & hash[i]);
				// If the calculated value of 'hex' has a length of 1, append a '0'
				if (hex.length() == 1)
					hexString.append('0');
				// Append hex value to the hexString
				hexString.append(hex);
			}
			// return the value of hexString as a text string
			return hexString.toString();
		}
		// If the compiler is unsuccessful in running the code inside the 'try' section,
		// it will run the code in the 'catch' section
		catch (Exception e) {
			// If the compiler runs the 'catch,' throw a new runtime exception error
			throw new RuntimeException(e);
		}
	}

	/*
	 * Information about SHA-512 taken can be found at
	 * https://en.bitcoinwiki.org/wiki/SHA-512. SHA-512 is a function of
	 * cryptographic algorithm SHA-2, which is an evolution of SHA-1. SHA-512 is
	 * similar to SHA-256, except that it uses 1024 bit "blocks" and accepts as
	 * input a 2^128 bits maximum length string. Other ways in which SHA-512 differs
	 * from SHA-256: the message is broken into 1024-bit chunks; the initial hash
	 * values and round constants are extended to 64 bits; there are 80 rounds
	 * instead of 64; the message schedule array has 80 - 64-bit words instead of 64
	 * - 32-bit words; to extend the message schedule array w, the loop is from 16
	 * to 79 instead of from 16 to 63; the round constants are based on the first 80
	 * primes 2..409; the word size used for calculations is 64 bits long; the
	 * appended length of the message (before pre-processing), in bits, is a 128-bit
	 * big-endian integer; and the shift and rotate amounts used are different.
	 */

	// Applies Sha512 to a string and returns the result as a String
	public static String applySha512(String input) {
		// The compiler will attempt to perform the code inside the 'try' section
		try {
			// Create a new MessageDigest object using SHA-512
			MessageDigest digest = MessageDigest.getInstance("SHA-512");

			// Create a new byte array to hold the SHA-512 digest using "UTF-8" encoding
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			// Create a new StringBuffer object called 'hexString' to store the hash
			StringBuffer hexString = new StringBuffer();
			// For each member of the byte [] ...
			for (int i = 0; i < hash.length; i++) {
				// Create a hexidecimal value for each member
				String hex = Integer.toHexString(0xff & hash[i]);
				// If the calculated value of 'hex' has a length of 1, append a '0'
				if (hex.length() == 1)
					hexString.append('0');
				// Append hex value to the hexString
				hexString.append(hex);
			}
			// return the value of hexString as a text string
			return hexString.toString();
		}
		// If the compiler is unsuccessful in running the code inside the 'try' section,
		// it will run the code in the 'catch' section
		catch (Exception e) {
			// If the compiler runs the 'catch,' throw a new runtime exception error
			throw new RuntimeException(e);
		}
	}

	// A method to return the difficulty as a String
	public static String getDifficultyString(int difficulty) {
		// Returns difficulty string target, to compare to hash. eg difficulty of 5 will
		// return "00000"
		return new String(new char[difficulty]).replace('\0', '0');
	}

	// A method that converts an object into JSON and returns the result as a String
	public static String getJson(Object o) {
		// Returns object information as JSON
		return new GsonBuilder().setPrettyPrinting().create().toJson(o);
	}

	// A method to return the merkel root as a String
	public static String getMerkleRoot(ArrayList<Transaction> transactions) {
		// initialize a local 'count' variable to the size of the transactions array
		// list
		int count = transactions.size();
		// Create a new List object to store information about the previous Tree layer
		List<String> previousTreeLayer = new ArrayList<String>();
		// For each transaction in the transactions Array list ...
		for (Transaction transaction : transactions) {
			// Add the transaction id to the previousTreeLayer list
			previousTreeLayer.add(transaction.transactionId);
		}
		// Create a new List object to store previousTreeLayer information
		List<String> treeLayer = previousTreeLayer;
		// While the 'count' is greater than 1 ...
		while (count > 1) {
			// set treeLayer to an empty ArrayList object
			treeLayer = new ArrayList<String>();
			// for each part of the previousTreeLayer object
			for (int i = 1; i < previousTreeLayer.size(); i += 2) {
				// Add the hash (SHA-512) of the previous two tree layers
				treeLayer.add(applySha512(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
			}
			// Set the 'count' to the size of the treeLayer object
			count = treeLayer.size();
			// Set the value of previousTreeLayer to the value of treeLayer
			previousTreeLayer = treeLayer;
		}
		// create a string object to hold the merkel root; if the tree layer size is 1,
		// then use the value of the treelayer object at position '0', otherwise set the
		// value of the string to empty ("").
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		// Return the string value contained in the merkleRoot variable
		return merkleRoot;
	}

	// A method to return Key information as a String
	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

	// A method to verify a String signature; returns a true or false
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		// The compiler will attempt to run the code in the 'try' block
		try {
			// Create a new Signature object using ECDSA
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			// verify the signature using the publicKey
			ecdsaVerify.initVerify(publicKey);
			// Update the Signature object with the bytes from 'data'
			ecdsaVerify.update(data.getBytes());
			// Return a true if the byte array 'signature' is verified or a false if it is
			// not verified
			return ecdsaVerify.verify(signature);
		}
		// if the compiler is unable to run the code in the 'try' block, it will run the
		// code in the 'catch' block
		catch (Exception e) {
			// throw a new runtime exception error
			throw new RuntimeException(e);
		}
	}

	// A method to verify a String signature; returns a true or false
	public static boolean verifySHA1withDSASig(PublicKey publicKey, String data, byte[] signature) {
		// The compiler will attempt to run the code in the 'try' block
		try {
			// Create a new Signature object using SHA1withDSA
			Signature dsaVerify = Signature.getInstance("SHA1withDSA", "BC");
			// verify the signature using the publicKey
			dsaVerify.initVerify(publicKey);
			// Update the Signature object with the bytes from 'data'
			dsaVerify.update(data.getBytes());
			// Return a true if the byte array 'signature' is verified or a false if it is
			// not verified
			return dsaVerify.verify(signature);
		}
		// if the compiler is unable to run the code in the 'try' block, it will run the
		// code in the 'catch' block
		catch (Exception e) {
			// throw a new runtime exception error
			throw new RuntimeException(e);
		}
	}
}
