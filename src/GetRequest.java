import java.util.HashMap;
import java.util.Map;

public class GetRequest {

    public String Method;
    public String Url;
    public Map<String, String> Headers;

    public GetRequest(String method, String url, HashMap<String, String> headers){
        Method = method;
        Url = url;
        Headers = headers;
    }

}
