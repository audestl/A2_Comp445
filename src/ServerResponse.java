import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ServerResponse {

	public Request Request;
	public File RootPath;
	public File file;
	public int statusCode;
	public String statusPhrase;


	public ServerResponse(Request request, File rootpath) throws IOException {
		if(request.Url.contains("/../")){
			System.out.println("\nSecurity issue detected! URL contains \"/../\"");
			System.exit(0);
		}

		this.Request = request;
		this.RootPath = rootpath;
		this. file = new File(RootPath, Request.Url);
		this.statusCode = getStatusCode();
		this.statusPhrase = getStatusPhrase();

	}

	//Uses Rootpath and Request.Url
	public int getStatusCode() throws IOException{
		int statusCode;

		if(Request.Method.equals("POST")){
			statusCode = PostUrl(file);
		}
		else{
			statusCode = ReadUrlFile(file);
		}

		return statusCode;
	}

	//Uses statusCode
	public String getStatusPhrase() {
		
		String phrase;
		switch(statusCode) {
		case 200:
			phrase = "OK";
			break;
		case 404:
			phrase = "Not found";
			break;
		case 201:
			phrase = "Created";
			break;
		case 403:
			phrase = "Forbidden";
			break;
		default:
			phrase = "Bad Request";
			break;
		}
		return phrase;
	}

	public String ToString() throws IOException {
		String responseMessage;
		String data = GetUrlContent();
		if(Request.Method.equals("GET")){
			responseMessage = Request.Version + " " + getStatusCode() + " " + getStatusPhrase()+ "\r\n" +
					Request.GetHeaderString() + "\r\n\r\n"+ GetUrlContent();
			//add data content in the url file
		}
		else{
			responseMessage = Request.Version + " " + getStatusCode() + " " + getStatusPhrase()+ "\r\n" +
					Request.GetHeaderString() + "\r\n\r\n";
		}
		return responseMessage;
	}

	public int PostUrl(File file) throws IOException {
		boolean createdFile = false;
		if(!file.exists()){
			if(file.isDirectory()){
				createdFile = file.mkdir();
			}
			if(file.isFile()){
				createdFile = file.createNewFile();
			}
			if(createdFile){
				return 201;
			}
		}
		else{
			if(file.isFile()){
				if(!file.canWrite()){
					return 403;
				}
				else {
					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(Request.Body);
					fileWriter.close();
					return 200;
				}
			}
		}
		return 400;
	}

	public int ReadUrlFile(File file) throws IOException{
		if(!file.exists()){
			return 404;
		}
		else{
			if(file.isFile()) {
				if (file.canRead())
					return 200;
				else {
					return 403;
				}
			}
			else {
				if(file.isDirectory()){
					return 400;
				}
			}
		}
		return 400;
	}

	public String GetUrlContent() throws IOException{
		String str = "";
		String contentType="";
		if(file.exists() && file.isFile() && file.canRead()){
			str = Files.readString(file.toPath());
			contentType = Files.probeContentType(file.toPath());

		}
		int contentLength = str.length();
		if(contentLength != 0){
			Request.Headers.put("Content-Length", Integer.toString(contentLength));
			Request.Headers.put("Content-Type", contentType);
		}
		return str;
	}

}
