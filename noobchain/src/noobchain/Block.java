/**
 * @author jasontiller
 *
 */

package noobchain; // this file is part of the noobchain package

//import needed java Internet packages and other utilities: InetAddress, UnknownHostException, ArrayList, and Date
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

// class for creating Block objects
public class Block {
	// declare needed object fields
	// Holds text information about the miner of the block
	private String miner;
	// Holds text information about the miner's IP address for the machine that
	// mined the block
	private String minerAddress;
	// Holds the hash of the current block
	public String hash;
	// Holds the hash of the previous block
	public String previousHash;
	// Holds the current block's merkel root
	public String merkleRoot;
	// holds the list of transactions
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	// holds a timestamp as number of milliseconds since 1/1/1970.
	public long timeStamp;
	// holds the nonce value for the current block
	public int nonce;

	// Constructor: A method for constructing Block objects
	public Block(String previousHash) {
		// Set the miner using the system's current user
		miner = System.getProperty("user.name");
		// Set minerAddress to an empty string
		minerAddress = "";
		// Compiler will attempt to run the code in the 'try' block
		try {
			// Set minerAddress to the current machine's IP Address
			minerAddress = InetAddress.getLocalHost().getHostAddress();
		}
		// If the compiler fails to run the code in the 'try' block, it will run the
		// code in the 'catch' block
		catch (UnknownHostException e) {
			// print error information to the console
			e.printStackTrace();
		}
		// sets the current block's previousHash value to the value of the previous
		// block's hash
		this.previousHash = previousHash;
		// gets the current time as a timestamp
		this.timeStamp = new Date().getTime();
		// Calculate a new hash based on the current block's contents
		this.hash = calculateHash();
	}

	// A method to add transactions to this block; returns true or false
	public boolean addTransaction(Transaction transaction) {
		// if there is not a transaction, return false
		if (transaction == null)
			return false;
		// if the block is not the genesis block
		if ((!"0".equals(previousHash))) {
			// if the transaction fails
			if ((transaction.processTransaction() != true)) {
				// print failed transaction message to the console and return false
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		// Add transaction to the current block
		transactions.add(transaction);

		// print successful transaction message and print the transaction's SwansonQuote
		// to the console
		System.out.println("Transaction Successfully added to Block: Ron Swanson - " + transaction.swansonQuote);
		// return true
		return true;
	}

	// A method to calculate and return a new hash based on the block's contents as
	// a String
	public String calculateHash() {
		// creates and stores a cryptographic string of characters using a block's
		// instance variables
		String calculatedhash = StringUtil.applySha512(
				previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + miner + minerAddress + merkleRoot);
		// returns the calculated cryptographic string
		return calculatedhash;
	}

	// A method to create (a.k.a., "mine") a new block for the blockchain
	public void mineBlock(int difficulty) {
		// calculate the merkle root for the block based on the transactions contained
		// in the block
		merkleRoot = StringUtil.getMerkleRoot(transactions);
		// Create a string with difficulty * "0"
		String target = StringUtil.getDifficultyString(difficulty);
		// use a loop to calculate the block's hash until the target string with the
		// correct number of '0s' at the beginning, based on the difficulty variable, is
		// created
		while (!hash.substring(0, difficulty).equals(target)) {
			// increase the nonce value until the target is reached
			nonce++;
			// calculate the block's hash based on its current values
			hash = calculateHash();
		}
		// print final results to the console
		System.out.println("Block Mined!!! : " + hash);
		System.out.println("Block Miner: " + miner);
		System.out.println("Block mined at address: " + minerAddress);
	}

	/**
	 * @return the miner
	 */
	public String getMiner() {
		return miner;
	}

	/**
	 * @param miner the miner to set
	 */
	public void setMiner(String miner) {
		this.miner = miner;
	}

	/**
	 * @return the minerAddress
	 */
	public String getMinerAddress() {
		return minerAddress;
	}

	/**
	 * @param minerAddress the minerAddress to set
	 */
	public void setMinerAddress(String minerAddress) {
		this.minerAddress = minerAddress;
	}

}