import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InputStreamTest {

	public static void main(String[] args) throws IOException {
	
		
		
			

	String request = " post /localhost:8080 HTTP/1.0\r\nContent-length:5\r\nUser-agent:me\r\n\r\nHello";
		
	
	StringBuilder builder = new StringBuilder();
	StringReader reader = new StringReader(request);

	BufferedReader in = new BufferedReader(reader);

		
    int body = in.read();
    List<String> list = new ArrayList<>();

    list = in.lines().collect(Collectors.toList());

    for(int i=0; i<list.size(); i++)
    {
    	builder.append(list.get(i)+"\r\n");
    }
    
    String streamResult = builder.toString();

    System.out.println(streamResult);
    
    in.close();
    
  

		
		
	}
}
