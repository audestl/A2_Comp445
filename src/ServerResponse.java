
public class ServerResponse {

	

	public String getStatusCode(int code) {
		
		String correctCode = null;
		
		switch(code) {
		
		case 1: correctCode = "200 : OK";
		case 2: correctCode = "404 : Not found";
		case 3: correctCode = "201 : Created";	
		case 4: correctCode = "400 : Bad Request";
		case 5: correctCode = "403 : Forbidden";
		
		}
		return correctCode;
	}
}
