import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Parsing {
	
	
	private String methodType;
	private String host;
	private String relativePath;
	private String query;
	private String key;
	private String value;
	private HashMap<String, String> headers = new HashMap();
	private String formattedRequest;
	private String readyToSend;
	private Boolean verbose=false;
	private String entityBody;
	
	
	public void ParseRequest(String request) throws IOException {
		
		StringReader reader = new StringReader(request);
		BufferedReader br = new BufferedReader(reader);
		request = br.readLine();
	

		// Parse request line

		// 1. Get the methodType of the request
		methodType = request.substring(0, request.indexOf(" "));
		

		// 2.Parse headers if there are any
		while(request.contains("-h")) {

			int h = request.indexOf("-h");

			String temp = request.substring(h);

			h = temp.indexOf("-h");
			int i = temp.indexOf(":");

			key = temp.substring(h + 2, i);
			value = temp.substring(i + 1, temp.indexOf(' '));
			request = temp.substring(temp.indexOf(' '));

			headers.put(key, value);

		}

		// 3. Get the url and split it into host + relative path
		String str = request.substring(request.indexOf("http"));
		URL theUrl = new URL(str);

		host = theUrl.getHost();
		relativePath = theUrl.getPath();
		if (relativePath == null)
			relativePath = "";

		// 4. Check if there are any query parameters

		if (request.contains("?")) {

			String temp = request.substring(request.indexOf("?"));
			query = temp.substring(temp.indexOf("?"), temp.indexOf(' '));
		} else
			query = "";

		
		
		
		
		
	}

}
