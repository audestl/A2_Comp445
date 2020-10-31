import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request {
    public String Method;
    public String Url;
    public String Version;
    public Map<String, String> Headers;
    public String Body;

    public Request(String method, String url, String version, HashMap<String, String> headers, String body){
        this.Method = method;
        this.Url = url;
        this.Version = version;
        this.Headers = headers;
        this.Body = body;
    }

    public Request(String method, String url, String version, HashMap<String, String> headers){
        this.Method = method;
        this.Url = url;
        this.Version = version;
        this.Headers = headers;
    }

    public String GetHeaderString(){
        List<String> formattedHeaders = new ArrayList<String>();
        this.Headers.forEach((name, value) -> formattedHeaders.add(String.format("%s: %s", name, value)));
        String headers = String.join("\r\n", formattedHeaders);
        return headers;
    }

    public String ToString(){

        String str = null;
        if(Body!=null) {
            str = Method + " " + Url + " " + Version + " " + GetHeaderString() + " " + Body;
        }
        else{
            str = Method + " " + Url + " " + Version + " " + GetHeaderString();
        }
        return str;
    }

}
