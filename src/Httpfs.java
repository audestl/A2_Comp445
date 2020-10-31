import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

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
					System.out.println("Verbose option enabled");
					break;
				case "-p":
					if(portNotDefined) {
						try {
							port = Integer.parseInt(args[i + 1]);
							portNotDefined = false;
							i++;
						} catch (NumberFormatException e) {
							System.out.println(e.getMessage());
							System.exit(0);
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
					if(verboseEnabled) {
						System.out.println("Server with new port "+port+" has been created and is now listening.");
					}
				}
				else{
					System.out.println("Port number needs to be within Non-Reserved port range: [1024, 65535]. + " +
							"Try again.");
					System.exit(0);
				}
			}
			else{
				server = new ServerSocket(8080);
				if(verboseEnabled) {
					System.out.println("Server with default port 8080 has been created and is now listening.");
				}
			}
			Socket client = server.accept();
			if(verboseEnabled) {
				System.out.println("Client is now connected with Server.");
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream());
			HashMap<String, String> head = new HashMap();

			String initial;
			Request request = null;

			// This while loop represents the infinite loop but we make sure to read the whole request in the
			// GetRequestMessage function. Therefore, the stopping condition has been met already and we just break
			// from the loop.
			while((initial = br.readLine()) != null){
				if(verboseEnabled) {
					System.out.println("Server received request from client.");
				}
				request = GetRequest(br, initial, head);
				break;
			}
			ServerResponse response = new ServerResponse(request, rootPath);
			if(verboseEnabled) {
				System.out.println("\nPrinting client's request: \n\n");
				System.out.println(request.toString()+"\n");
				System.out.println("Server sending response back to client.");
				System.out.println("\nPrinting Server's response: \n\n");
				System.out.println(response.toString());
			}
			out.print(response.ToString());
			out.flush();
			out.close();
			if(verboseEnabled) {
				System.out.println("Connection has been closed.");
			}
		}
		catch (Exception e){
			System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static Request GetRequest(BufferedReader br, String initial, HashMap<String, String> head) throws IOException {

		Request request = null;
		String requestLine[] = initial.split("\\s");
		String method = requestLine[0].toUpperCase();
		String url = requestLine[1];
		String version = requestLine[2];
		//headers
		String line = br.readLine();

		while (!line.equals("") && line != null) {

			int idx = line.indexOf(':');
			if (idx < 0) {
				head = null;
				break;
			} else {
				String key = line.substring(0, idx).toLowerCase();
				String value = line.substring(idx + 2);
				head.put(key, value);
			}
			line = br.readLine();
		}

		String entityBody = null;
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

		switch(method){
			case "POST":
				if (!entityBody.isEmpty() && entityBody != null) {
					request = new Request(method, url, version, head, entityBody);
					break;
				}
				else{
					request = new Request(method, url, version, head);
					break;
				}
			case "GET":
				request = new Request(method, url, version, head);
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
