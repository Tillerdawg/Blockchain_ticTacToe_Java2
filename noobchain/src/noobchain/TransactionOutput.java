/**
 * @author jasontiller
 *
 */

package noobchain; // this file is part of the noobchain package

// import needed security module: PublicKey
import java.security.PublicKey;

// Class for creating TransactionOutput objects
public class TransactionOutput {
	// Initialize TransactionOutput object fields
	public String id; // the id of a given TransactionOutput object
	public PublicKey recipient; // also known as the new owner of these coins.
	public float value; // the amount of coins they own
	public String parentTransactionId; // the id of the transaction this output was created in

	// A method for constructing TransactionOutput objects
	public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
		// initialize instance variables
		this.recipient = recipient; // sets the recipient of the transaction
		this.value = value; // sets the value of the transaction
		this.parentTransactionId = parentTransactionId; // sets the parent transaction's id
		// creates a unique id value for the transaction
		this.id = StringUtil
				.applySha256(StringUtil.getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
	}

	// a boolean method for checking to see a coin belongs to the recipient
	public boolean isMine(PublicKey publicKey) {
		// returns true or false
		return (publicKey == recipient);
	}

}
