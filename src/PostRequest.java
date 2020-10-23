import java.util.HashMap;
import java.util.Map;

public class PostRequest {

    public String Method;
    public String Url;
    public Map<String, String> Headers;

    public PostRequest(String method, String url, HashMap<String, String> headers){
        Method = method;
        Url = url;
        Headers = headers;
    }

}