/**
 * @author jasontiller
 *
 */
package noobchain; // this file is part of the noobchain package

// A class for creating TransactionInput objects
public class TransactionInput {
	// Initialize TransactionInput fields
	public String transactionOutputId; // Reference to TransactionOutputs -> transactionId
	public TransactionOutput UTXO; // Contains the Unspent transaction output

	// a method for constructing TransactionInput objects
	public TransactionInput(String transactionOutputId) {
		// Initializes the transactionOutputId instance variable
		this.transactionOutputId = transactionOutputId;
	}
}