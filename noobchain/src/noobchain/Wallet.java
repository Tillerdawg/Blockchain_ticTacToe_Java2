/**
 * @author jasontiller
 *
 */

package noobchain; // this file is part of the noobchain package

/* Import needed security and utility modules:
 * KeyPair
 * KeyPairGenerator
 * PrivateKey
 * PublicKey
 * SecureRandom
 * ECGenParameterSpec, Note: ECGenParameterSpec is not used in the final assignment.
 * ArrayList
 * HashMap
 * Map
*/
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// A class for creating Wallet objects
public class Wallet {

	// Initialize Wallet object fields
	public PrivateKey privateKey;
	public PublicKey publicKey;
	public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

	// Method for constructing a Wallet object
	public Wallet() {
		// Generate a public and private key pair
		generateKeyPair();
	}

	// A method for creating a public and private key pair
	public void generateKeyPair() {
		// Compiler will try to execute the code in the 'try' section
		try {
			/*
			 * Creates a new key pair generator using Elliptic Curve Digital Signature
			 * Algorithm, a.k.a., ECDSA, which is used by BitCoin
			 */
//			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
			/* Use the Algorithm - DSA */
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");

			// Creates a random number using SHA1PRNG
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			// Creates elliptical curve parameters using prime192v1
//			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

			// Initialize the key generator and generate a KeyPair
//			keyGen.initialize(ecSpec, random); // 256
			keyGen.initialize(512, random);
			// Creates a keyPair object and generates the public and private keys
			KeyPair keyPair = keyGen.generateKeyPair();
			// Set the private key from the keyPair
			privateKey = keyPair.getPrivate();
			// Set the public key from the keyPair
			publicKey = keyPair.getPublic();
		} // If the compiler cannot execute the code in the 'try' section, it will execute
			// the code in the 'catch' section
		catch (Exception e) {
			// throw a runtime exception error
			throw new RuntimeException(e);
		}
	}

	// A method to return the Wallet's balance
	public float getBalance() {
		// Initialize a local total variable
		float total = 0;
		// Get the value for each item in the UTXOs HashMap set
		for (Map.Entry<String, TransactionOutput> item : NoobChain.UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			if (UTXO.isMine(publicKey)) { // if output belongs to me ( if coins belong to me )
				UTXOs.put(UTXO.id, UTXO); // add it to our list of unspent transactions.
				total += UTXO.value;
			}
		}
		// Return the balance
		return total;
	}

	// A method for returning transaction information from a sendFunds request
	public Transaction sendFunds(PublicKey _recipient, float value) {
		// Check to see if there are enough funds to send a transaction
		if (getBalance() < value) {
			// If there are not enough funds in the balance, the transaction will fail and
			// return null
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
		// Create a new ArrayList object to hold the transaction inputs
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		// Initialize a local total variable
		float total = 0;
		// For each item in the UTXOs set...
		for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
			// Get each item's value
			TransactionOutput UTXO = item.getValue();
			// add the item's value to the total
			total += UTXO.value;
			// add a TransactionInput object to the inputs ArrayList
			inputs.add(new TransactionInput(UTXO.id));
			// If the total of Output items is greater than the value being sent, break
			if (total > value)
				break;
		}

		// Create a new transaction using the Wallet's public key, the recipient's
		// public key, the value of the transaction, and inputs
		Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
		// Generate a signature for the transaction using the Wallet's private key
		newTransaction.generateSignature(privateKey);

		// For each TransactionInput object in the inputs Arraylist ...
		for (TransactionInput input : inputs) {
			// remove the TransactionOutputId from the UTXOs HashMap
			UTXOs.remove(input.transactionOutputId);
		}

		// Return transaction information
		return newTransaction;
	}

}
