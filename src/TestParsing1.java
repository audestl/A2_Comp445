import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestParsing1 {

	public static void main(String[] args) throws IOException {

		List<String> list = new ArrayList<String>();
		list.add("Content-length:5");
		list.add("User-agent:me");
		String headers = String.join("\\r\\n", list);

		String str = String.join("\\r\\n", "POST /localhost:8080 HTTP/1.0", headers, "","Hello");
		System.out.println(str);
		System.out.println("POST /localhost:8080 HTTP/1.0\\r\\nContent-length:5\\r\\nUser-agent:me\\r\\n\\r\\nHello");


		String request = "POST /localhost:8080 HTTP/1.0\r\nContent-length:5\r\nUser-agent:me\r\n\r\nHelloGET /localhost:8080 HTTP/1.0\r\nContent-length:5\r\nUser-agent:me\r\n\r\nHello";
		int index = 0;

		String entityBody = null;

		StringReader reader = new StringReader(request);
		BufferedReader br = new BufferedReader(reader);
		HashMap<String, String> head = new HashMap();


		String initial = br.readLine();
		while(initial != null) {
			TestParsing1.Read(br, initial, head);
			initial = br.readLine();
		}

	}

	public static void Read(BufferedReader br, String initial, HashMap<String, String> head) throws IOException {

		//headers

		String line = br.readLine();
		int idx = line.indexOf(":");

		while (!line.equals("")) {

			idx = line.indexOf(':');
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

		String newRequest = initial.concat(head.toString()).concat(entityBody);
		ArrayList<String> allRequests = new ArrayList();

		allRequests.add(newRequest);
	}

}
