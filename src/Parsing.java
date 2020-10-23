import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Parsing {
	
	

	private String method;
	private String url;
	private String version = "HTTP/1.0";
	private String entityBody;

	
	
	public static void main(String[] args) throws IOException {
		Parsing p = new Parsing();
		p.ParseRequest();
		
	}
	
	public void ParseRequest() throws IOException {
		
		
		String request = "POST /localhost:8080 HTTP/1.0\r\nContent-length:13\r\nUser-agent:me\r\n\r\nHi my name is";
		

		StringReader reader = new StringReader(request);
		BufferedReader br = new BufferedReader(reader);
	
		
	    HashMap<String, String> headers = new HashMap();
	    
	    String initial;
	    String requestLine[];
	    
		
	    initial = br.readLine();
	    
	    
	    requestLine = initial.split("\\s");
	
	    for(int i=0; i<requestLine.length; i++) {
	    	System.out.println(requestLine[i]);
	    }
	    
	    method = requestLine[0];
	    
	    
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
	    
	    for (Entry<String, String> entry : headers.entrySet()) {
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

}
