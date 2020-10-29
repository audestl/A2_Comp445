import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestParsing1 {

	public static void main(String[] args) throws IOException {
	
		String request = "POST /localhost:8080 HTTP/1.0\r\nContent-length:5\r\nUser-agent:me\r\n\r\nHelloGET /localhost:8080 HTTP/1.2\\r\\nContent-length:5\\r\\nUser-agent:me\\r\\n\\r\\nHello";
		int index = 0;
		
		String entityBody = null;
		
		StringReader reader = new StringReader(request);
		BufferedReader br = new BufferedReader(reader);
		HashMap<String, String> head = new HashMap();
		
		
		
		TestParsing1.Read(br, head);
		while(br.read()!=0) {
			TestParsing1.Read(br, head);
		}
	
	}
	
public static void Read(BufferedReader br, HashMap<String, String> head) throws IOException {
		
		String initial = br.readLine();
		
		//headers
	
		String line = br.readLine();
		int idx = line.indexOf(":");

		while (!line.equals("")) {

			idx = line.indexOf(':');
			if (idx < 0) {
				head = null;
				break;
			}
			else{
				String key = line.substring(0, idx).toLowerCase();
				String value = line.substring(idx+1);
				head.put(key, value);
			}
			line = br.readLine();
		}

		String entityBody="";
		if(head.containsKey("content-length")) {
			int contentLength=0;
			contentLength = Integer.parseInt(head.get("content-length"));
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
		
		String newRequest = initial.concat(head.toString()).concat(entityBody);
		ArrayList<String> allRequests = new ArrayList();
		
		allRequests.add(newRequest);
		}

}
