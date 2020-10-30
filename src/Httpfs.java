import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Httpfs {

	public static void main(String[] args) {

		boolean verboseEnabled = false;
		boolean portNotDefined = true;
		boolean pathDirNotDefined = true;
		File rootPath = null;
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
							System.out.println("New default path to directory does not exists or is not a directory.");
							System.exit(0);
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
		if(pathDirNotDefined){
			rootPath = new File("testFolder");
		}
		CreateConnection(port, verboseEnabled, rootPath);
	}

	public static void CreateConnection(int port, boolean verboseEnabled, File rootPath){
		ServerSocket server = null;
		try {
			if(port != -1) {
				if(port > 1024 && port <=65535) {
					server = new ServerSocket(port);
				}
				else{
					System.out.println("Port number needs to be within Non-Reserved port range: [1024, 65535]. + " +
							"Try again.");
					System.exit(0);
				}
			}
			else{
				server = new ServerSocket(8080);
			}
			Socket client = server.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));

			String initial = br.readLine();
			HashMap<String, String> head = new HashMap();
			String requestMessage = GetRequestMessage(br, initial, head);

			Request request = ParseRequest(requestMessage);
			ServerResponse response = new ServerResponse(request, rootPath);

			PrintWriter out = new PrintWriter(client.getOutputStream());
			System.out.println("\nResponse: \n" + response.ToString());
			out.print(response.ToString());
			out.flush();
			out.close();
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}

	public static String GetRequestMessage(BufferedReader br, String initial, HashMap<String, String> head) throws IOException {

		//headers
		String line = br.readLine();

		while (!line.equals("") && line != null) {

			int idx = line.indexOf(':');
			if (idx < 0) {
				head = null;
				break;
			} else {
				String key = line.substring(0, idx).toLowerCase();
				String value = line.substring(idx + 1);
				head.put(key, value);
			}
			line = br.readLine();
		}

		String entityBody = "";
		if (head.containsKey("content-length")) {
			int contentLength = 0;
			contentLength = Integer.parseInt(head.get("content-length"));
			StringBuilder builder = new StringBuilder();
			int body;
			int count = 0;
			while (count < contentLength) {
				body = br.read();
				char temp = (char) body;
				builder.append(temp);
				count++;
			}
			entityBody = builder.toString();
		}

		String newRequest = BuildRequestMessage(initial, head, entityBody);
		return newRequest;
	}

	public static String BuildRequestMessage(String initial, HashMap<String, String> head, String entityBody){

		List<String> formattedHeaders = new ArrayList<String>();
		head.forEach((name, value) -> formattedHeaders.add(String.format("%s: %s", name, value)));
		String headers = String.join("\r\n", formattedHeaders);

		String requestMessage = null;
		if(entityBody != null){
			requestMessage = String.join("\r\n", initial, headers, "", entityBody);
		}
		else{
			requestMessage = String.join("\r\n", initial, headers, "");
		}
		return requestMessage;
	}

	public static Request ParseRequest(String requestMessage) throws IOException {

		System.out.println("Request Message: \n" + requestMessage);
		String method;
		String url;
		String entityBody;
		String version;
		HashMap<String, String> headers = new HashMap();

		StringReader reader = new StringReader(requestMessage);
		BufferedReader br = new BufferedReader(reader);

		String initial = br.readLine();
		String requestLine[] = initial.split("\\s");
		method = requestLine[0].toUpperCase();
		url = requestLine[1];
		version = requestLine[2];

		for(int i=0; i<requestLine.length; i++) {
			System.out.println(requestLine[i]);
		}

		//headers
		String line = br.readLine();

		while (!line.equals("")) {

			int idx = line.indexOf(':');
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
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}


		// For POST
		// Find content-length header
		int contentLength;
		Request request = null;
		switch(method){
			case "POST":
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
				request = new Request(method, url, version, headers, entityBody);
				break;
			case "GET":
				request = new Request(method, url, version, headers);
				break;
			default:
				Help();
				break;
		}
		return request;
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
