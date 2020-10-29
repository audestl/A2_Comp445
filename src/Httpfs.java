import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Httpfs {

	public static void main(String[] args) {

		boolean verboseEnabled = false;
		boolean errorOccurred = false;
		boolean portNotDefined = true;
		boolean pathDirNotDefined = true;
		File rootPath;
		int port = -1;

		for(int i = 0; i < args.length; i++){
			switch(args[i]){
				case "-v":
					verboseEnabled = true;
					break;
				case "-p":
					if(portNotDefined) {
						try {
							port = Integer.parseInt(args[i + 1]);
							portNotDefined = false;
							i++;
						} catch (NumberFormatException e) {
							System.out.println(e.getMessage());
						}
					}
					//Multiple instances of -p option
					else{
						Help();
					}
					break;
				case "-d":
					if(pathDirNotDefined) {
						pathDirNotDefined = false;
						rootPath = new File(args[i+1]);
						if(!rootPath.exists() || !rootPath.isDirectory()){
							errorOccurred = true;
						}
						i++;
					}
					//Multiple instances of -d option
					else{
						Help();
					}
					break;
				default:
					Help();
					break;
			}
		}
	}

	public void CreateConnection(int port){
		boolean errorOccurred = false;
		ServerSocket server = null;
		try {
			if(port != -1) {
				if(port > 1024) {
					server = new ServerSocket(port);
				}
				else{
					errorOccurred = true;
				}
			}
			else{
				server = new ServerSocket(8080);
			}
			Socket client = server.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			//Read through the whole input stream
			// Separate request
			// split into string array
			//String concatenation / headers1 + entityBody1
			
			

			
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void ParseRequest(String requestMessage) throws IOException {

		String method;
		String url;
		String entityBody;
		String version = "HTTP/1.0";
		HashMap<String, String> headers = new HashMap();
		String request = "POST /localhost:8080 HTTP/1.0\r\nContent-length:5\r\nUser-agent:me\r\n\r\n";

		StringReader reader = new StringReader(request);
		BufferedReader br = new BufferedReader(reader);

		String initial = br.readLine();
		String requestLine[] = initial.split("\\s");
		method = requestLine[0];

		for(int i=0; i<requestLine.length; i++) {
			System.out.println(requestLine[i]);
		}

		//headers
		String line = br.readLine();
		int idx = line.indexOf(":");

		while (!line.equals("")) {

			idx = line.indexOf(':');
			if (idx < 0) {
				headers = null;
				break;
			}
			else{
				String key = line.substring(0, idx).toLowerCase();
				String value = line.substring(idx+1);
				headers.put(key, value);
			}
			line = br.readLine();
		}

		for (Map.Entry<String, String> entry : headers.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue().toString());
		}


		// For POST
		// Find content-length header
		int contentLength;
		   if(method.equalsIgnoreCase("post"))
		    {
		    	contentLength = Integer.parseInt(headers.get("content-length"));
			    StringBuilder builder = new StringBuilder();
			    int body;
			    int count=0;
			    body = br.read();
			    while(count<contentLength) {
			    		
			    	 char temp = (char)body;
			    	 builder.append(temp);
			    	 body = br.read();
			    	 count++;
			    	 
			    }
			    entityBody = builder.toString();
		    }
		    else
		    	contentLength=0;
		   
		   
		    


	}

	public static void Help(){
		System.out.println("httpfs is a simple file server.");
		System.out.println("\nusage: httpfs [-v] [-p port] [-d PATH-TO-DIR]\n");
		System.out.println("\t-v \t\tPrints debugging messages.");
		System.out.println("\t-p \t\tSpecifies the port number that the server will listen and server at. " +
				"Default is 8080.");
		System.out.println("\t-d \t\tSpecifies the directory that the server will use to read/write requested files. " +
				"Default is the current directory when launching the application.");
		System.exit(0);
	}

}
