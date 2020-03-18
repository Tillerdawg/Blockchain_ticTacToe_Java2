/**
 *
 */
package noobchain; // this file is part of the noobchain package

// Import needed io, net, and nio modules: BufferedReader, InputStreamReader, URL, URLConnection, and Charset
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * @author jasontiller
 *
 */

/*
 * This Java class uses the Ron Swanson Quotes API, as created by James Sean
 * Wright. Information about this API can be found on Wright's github page:
 * https://github.com/jamesseanwright/ron-swanson-quotes
 */

// Note: Ron Swanson quote will only be attached to successful transactions

// A class for creating SwansonQuote objects
public class SwansonQuote {

	// Initialize a new String, which will hold the quote, to empty
	private String quote = "";
	// Use a String to hold the source URL as text
	private String source = "https://ron-swanson-quotes.herokuapp.com/v2/quotes";

	// A method for constructing SwansonQuote objects
	public SwansonQuote() {
		// Call the specified URL and store the contents of the response as text; the
		// response from the URL is an array
		String response = callURL(source);
		// Since the response is an array, use a substring calculation to remove the
		// "[]"
		quote = response.substring(1, response.length() - 1) + "\n";
	}

	// A method for retrieving the response from a Web API and storing the response
	// as a String
	public static String callURL(String myURL) {
		// Create a StringBuilder object to assist in constructing the response from the
		// API
		StringBuilder sb = new StringBuilder();
		// Create a URLConnection object for use later
		URLConnection urlConn = null;
		// Create an InputStreamReader object for use later
		InputStreamReader in = null;
		// The compiler will attempt the code in the 'try' block
		try {
			// Create a new URL object to store the myURL parameter
			URL url = new URL(myURL);
			// Use URLConnection object to open a connection to the specified URL
			urlConn = url.openConnection();
			// Check to see if there the connection is established
			if (urlConn != null)
				// If so, set the ReadTimeout to a minute (60,000 milliseconds
				urlConn.setReadTimeout(60 * 1000);
			// Check to see if there is a connection and an InputStream
			if (urlConn != null && urlConn.getInputStream() != null) {
				// Set the value of the InputStreamReader to the urlConn.getInputStream value
				// and the value of the Charset object
				in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
				// Use a BufferedReader object to read the contents of the InputStreamReader
				// object
				BufferedReader bufferedReader = new BufferedReader(in);
				// if the BufferedReader object exists
				if (bufferedReader != null) {
					// Create a local int variable, which is initialized with a value of 0
					int cp;
					// Loop through the BufferedReader object character by character
					while ((cp = bufferedReader.read()) != -1) {
						// Add each character at position 'cp' to the StringBuilder object
						sb.append((char) cp);
					}
					// Close the BufferedReader object
					bufferedReader.close();
				}
			}
			// Close the InputStream object
			in.close();
		}
		// if the compiler is unsuccessful in running the code in the 'try' block, it
		// will execute the code in the 'catch' block
		catch (Exception e) {
			// Throw a new RuntimeException error and print information about the error to
			// the console
			throw new RuntimeException("Exception while calling URL:" + myURL, e);
		}

		// Return the StructuredBuffer object's contents as a String to the calling
		// method
		return sb.toString();
	}

	// A method for getting the source of the Ron Swanson Quote API
	public String getSource() {
		return source;
	}

	// A method for setting a new source for the Ron Swanson Quote API
	public void setSource(String source) {
		this.source = source;
	}

	// A method for getting a new Ron Swanson quote
	public String getQuote() {
		// return the quote to the calling function
		return quote;

	}

	// A method for setting a new Ron Swanson quote
	public void setQuote(String quote) {
		this.quote = quote;
	}
}
