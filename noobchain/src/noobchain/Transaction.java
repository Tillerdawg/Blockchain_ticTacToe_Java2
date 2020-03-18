/**
 * @author jasontiller
 *
 */
package noobchain; // this file is part of the noobchain package

// import needed security and utility modules: PrivateKey, PublicKey, and ArrayList
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

// a Class for creating Transaction objects
public class Transaction {
	// create Transaction object fields
	// Contains a unique id for Transaction objects
	public String transactionId;
	// Sender's address/public key
	public PublicKey sender;
	// Recipient's address/public key
	public PublicKey recipient;
	// The amount we wish to send to the recipient
	public float value;
	// This is to prevent anybody else from spending funds in our wallet
	public byte[] signature;
	// Creates an array list of inputs
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	// Creates an array list of outputs
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();
	// A rough count of how many transactions have been generated
	private static int sequence = 0;

	/* SwansonQuote == new data for Transaction! */
	public String swansonQuote;

	// Constructor: A method for constructing Transaction objects
	public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
		// Initialize instance variables
		this.sender = from; // sets the sender's publicKey value
		this.recipient = to; // sets the recipient's publicKey value
		this.value = value; // sets the transaction's value
		this.inputs = inputs; // sets the inputs for the transaction
		// Create new SwansonQuote object and store quote in swansonQuote String
		swansonQuote = new SwansonQuote().getQuote();

	}

	// A method for calculating and returning a cryptographic hash for a transaction
	// as a String
	/* Add swansonQuote to calculateHash() */
	private String calulateHash() {
		sequence++; // increase the sequence to avoid 2 identical transactions having the same hash
		// Calculate and return the hash using transaction values
		return StringUtil.applySha512(StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
				+ Float.toString(value) + swansonQuote + sequence);
	}

	// A method to generate a cryptographic signature using a private key
	public void generateSignature(PrivateKey privateKey) {
		// Create a data string of the sender's and recipient's public keys and the
		// value to be sent to the recipient
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
				+ Float.toString(value);
		// Create a cryptographic signature with the data string and the private key
//		signature = StringUtil.applyECDSASig(privateKey, data);
		signature = StringUtil.applySHA1withDSA(privateKey, data);
	}

	// Method to return the inputs value as a floating point number
	public float getInputsValue() {
		// initialize local variable
		float total = 0;
		// Collect all transaction input values
		for (TransactionInput i : inputs) {
			if (i.UTXO == null)
				continue; // if Transaction can't be found skip it, This behavior may not be optimal.
			// add individual total to overall inputs total
			total += i.UTXO.value;
		}
		// return the total inputs value
		return total;
	}

	// A method to return the Outputs value of a transaction as a floating point
	// number
	public float getOutputsValue() {
		// Initialize local variable
		float total = 0;
		// Collect all outputs values
		for (TransactionOutput o : outputs) {
			// Add individual outputs value to the total outputs
			total += o.value;
		}
		// Return the total Outputs value
		return total;
	}

	// A boolean method for processing transactions
	public boolean processTransaction() {
		// Checks to see if a signature on a transaction is invalid
		if (verifySignature() == false) {
			// Prints verification failure message to the console and returns false
			System.out.println("#Transaction Signature failed to verify");
			return false;
		}

		// Gathers transaction inputs (Making sure they are unspent):
		for (TransactionInput i : inputs) {
			// Stores the set of unspent transaction ids
			i.UTXO = NoobChain.UTXOs.get(i.transactionOutputId);
		}

		// Checks if a transaction value is over the minimumTransaction value:
		if (getInputsValue() < NoobChain.minimumTransaction) {
			// print transaction information to the console and return false
			System.out.println("Transaction Inputs too small: " + getInputsValue());
			System.out.println("Please enter the amount greater than " + NoobChain.minimumTransaction);
			return false;
		}

		// Generate transaction outputs:
		float leftOver = getInputsValue() - value; // get value of inputs then the left over change:
		transactionId = calulateHash(); // sets the transaction's unique id
		outputs.add(new TransactionOutput(this.recipient, value, transactionId)); // send value to recipient
		outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); // send the left over 'change' back to
																					// sender

		// Add outputs to Unspent list
		for (TransactionOutput o : outputs) {
			// Add individual output to list
			NoobChain.UTXOs.put(o.id, o);
		}

		// Remove transaction inputs from UTXO lists as spent:
		for (TransactionInput i : inputs) {
			if (i.UTXO == null)
				continue; // if Transaction can't be found skip it
			// Remove individual UTXO from UTXOs list
			NoobChain.UTXOs.remove(i.UTXO.id);
		}
		// Transaction is valid and complete, return true
		return true;
	}

	// A boolean method to verify a cryptographic signature
	public boolean verifySignature() {
		// Create a data string of the sender's and recipient's public keys and the
		// value to be sent to the recipient
		String data = StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient)
				+ Float.toString(value);
		// If the signature can be verified using the sender's public key and the data
		// string, return true, otherwise return false
//		return StringUtil.verifyECDSASig(sender, data, signature);
		return StringUtil.verifySHA1withDSASig(sender, data, signature);
	}
}
