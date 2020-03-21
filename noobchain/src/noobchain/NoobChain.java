package noobchain;

import java.security.Security;
import java.util.ArrayList;
//import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

import game.TicTacToe;

public class NoobChain {

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

	public static int difficulty = 3;
	public static float minimumTransaction = 0.1f;
	public static Wallet walletX;
	public static Wallet walletO;
	public static Transaction genesisTransaction;
	public static Transaction anotherTransaction;
	public static Scanner in;

	public static void main(String[] args) {
		// add our blocks to the blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Setup Bouncey castle as a
																						// Security Provider

		// Create wallets:
		walletX = new Wallet();
		walletO = new Wallet();
		Wallet coinbase = new Wallet();
		in = new Scanner(System.in);

		// create genesis transaction, which sends 100 NoobCoin to WalletX:
		// this adds funds to X's wallet
		genesisTransaction = new Transaction(coinbase.publicKey, walletX.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey); // manually sign the genesis transaction
		genesisTransaction.transactionId = "0"; // manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value,
				genesisTransaction.transactionId)); // manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); // its important to store
																							// our first transaction in
																							// the UTXOs list.

		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");

		// this adds funds to O's wallet
		anotherTransaction = new Transaction(coinbase.publicKey, walletO.publicKey, 100f, null);
		anotherTransaction.generateSignature(coinbase.privateKey);
		anotherTransaction.transactionId = "0";
		anotherTransaction.outputs.add(new TransactionOutput(anotherTransaction.recipient, anotherTransaction.value,
				anotherTransaction.transactionId));
		UTXOs.put(anotherTransaction.outputs.get(0).id, anotherTransaction.outputs.get(0));

		genesis.addTransaction(genesisTransaction);
		genesis.addTransaction(anotherTransaction);
		addBlock(genesis);

		placeBet();

	}

	private static void placeBet() {

		// cannot bet more than you have, check both x and o
		int betX;
		do {
			System.out.println("\nWalletX Value: " + walletX.getBalance());
			System.out.println("\"X\" Place Your Bet: ");
			betX = in.nextInt();
			if (genesisTransaction.getOutputsValue() >= betX) {
				break;
			}
		} while (true);

		int betO;
		do {
			System.out.println("\nWalletO Value: " + walletO.getBalance());
			System.out.println("\"O\" Place Your Bet: ");
			betO = in.nextInt();
			if (anotherTransaction.getOutputsValue() >= betO) {
				break;
			}
		} while (true);

		playGame(betX, betO);
		in.close();
	}

	private static void playGame(int betX, int betO) {
		// play the game
		TicTacToe ttt = new TicTacToe();
		String winner = ttt.play();
		String lastBlock = blockchain.get(blockchain.size() - 1).hash;

		System.out.println("\nWalletX's beginning balance is: " + walletX.getBalance());
		System.out.println("\nWalletO's beginning balance is: " + walletO.getBalance());

		if (winner.equals("X")) {
			Block block1 = new Block(lastBlock);
			System.out.println("\nWalletO is attempting to send funds (" + betO + ") to WalletX...");
			block1.addTransaction(walletO.sendFunds(walletX.publicKey, betO));
			addBlock(block1);
		} else if (winner.equals("O")) {
			Block block1 = new Block(lastBlock);
			System.out.println("\nWalletX is attempting to send funds (" + betX + ") to WalletO...");
			block1.addTransaction(walletX.sendFunds(walletO.publicKey, betX));
			addBlock(block1);
		} else if (winner.equals("Draw")) {
			System.out.println("\nIt's a DRAW, bets are off");
		}

		System.out.println("\nWalletX's balance is: " + walletX.getBalance());
		System.out.println("\nWalletO's balance is: " + walletO.getBalance());

		isChainValid();

		playAgain();

	}

	private static void playAgain() {
		System.out.println("\nPlay Again? Y-Yes N-No");
		String choice = in.next();
		if (choice.equalsIgnoreCase("Y") || choice.equalsIgnoreCase("Yes")) {
			placeBet();
		} else {
			System.out.println("\nThank you for playing Tic-Tac-Toe!");
			in.close();
		}

	}

	public static Boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>(); // a temporary working
																									// list of unspent
																									// transactions at a
																									// given block
																									// state.
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

		// loop through blockchain to check hashes:
		for (int i = 1; i < blockchain.size(); i++) {

			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);
			// compare registered hash and calculated hash:
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
				System.out.println("#Current Hashes not equal");
				return false;
			}
			// compare previous hash and registered previous hash
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				System.out.println("#Previous Hashes not equal");
				return false;
			}
			// check if hash is solved
			if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined");
				return false;
			}

			// loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for (int t = 0; t < currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);

				if (!currentTransaction.verifySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false;
				}
				if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false;
				}

				for (TransactionInput input : currentTransaction.inputs) {
					tempOutput = tempUTXOs.get(input.transactionOutputId);

					if (tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}

					if (input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}

					tempUTXOs.remove(input.transactionOutputId);
				}

				for (TransactionOutput output : currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}

				if (currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
					System.out.println("#Transaction(" + t + ") output recipient is not who it should be");
					return false;
				}
				if (currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}

			}

		}
		System.out.println("Blockchain is valid");
		return true;
	}

	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}
