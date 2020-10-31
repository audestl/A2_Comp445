import java.io.*;
import java.nio.file.Files;

public class ServerResponse {

	public Request Request;
	public File RootPath;
	public File file;
	public int statusCode;
	public String statusPhrase;


	public ServerResponse(Request request, File rootpath) throws IOException {
		this.Request = request;
		this.RootPath = rootpath;
		if(request.Url.contains("/../")){
			this.statusCode = 400;
		}
		else {
			this. file = new File(RootPath, Request.Url);
			this.statusCode = getStatusCode();
		}
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
			if(data.isEmpty()){
				statusCode =404;
				this.statusPhrase = getStatusPhrase();
			}
			responseMessage = Request.Version + " " + statusCode + " " + statusPhrase + "\r\n" +
					Request.GetHeaderString() + "\r\n\r\n"+ data;
			//add data content in the url file
		}
		else{

			responseMessage = Request.Version + " " + statusCode + " " + statusPhrase + "\r\n" +
					Request.GetHeaderString() + "\r\n\r\n";
		}
		return responseMessage;
	}

	public int PostUrl(File file) throws IOException {
		String filePath = file.getPath();
		String newParent = RootPath.getPath();
		String[] array = filePath.split("\\\\");

		if(!file.exists()){
			if(!array[array.length-1].contains(".")){
				return 400;
			}
			else {
				for (int i = 1; i < array.length - 1; i++) {
					File newFile = new File(newParent, array[i]);
					String fileName = newFile.getName();
					if (fileName.contains(".")) {
						return 400;
					} else {
						newFile.mkdir();
						newParent = newFile.getPath();
					}
				}
				File newFile = new File(newParent, array[array.length - 1]);
				newFile.createNewFile();
				FileWriter fileWriter = new FileWriter(file);
				if(Request.Body != null) {
					fileWriter.write(Request.Body);
				}
				fileWriter.close();
				return 201;
			}
		}
		else{
			if(file.isDirectory()){
				if(!file.canWrite()){
					return 403;
				}
				else {
					return 400;
				}
			}
			if(file.isFile()){
				if(!file.canWrite()){
					return 403;
				}
				else {
					FileWriter fileWriter = new FileWriter(file);
					if(Request.Body != null) {
						fileWriter.write(Request.Body);
					}
					fileWriter.close();
					return 200;
				}
			}
		}
		return 400;
	}

	public int ReadUrlFile(File file){
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
					return 200;
				}
			}
		}
		return 400;
	}

	public String GetUrlContent() throws IOException{
		String str = "";
		String contentType;
		if(file!=null && file.exists() && file.canRead()){
			if(file.isFile()) {
				StringBuilder builder = new StringBuilder(str);
				BufferedReader br = new BufferedReader(new FileReader(file));
				String temp;
				while ((temp = br.readLine()) != null) {
					builder.append(temp);
				}
				str = builder.toString();
				contentType = Files.probeContentType(file.toPath());
				int contentLength = str.length();
				if(contentLength != 0){
					Request.Headers.put("content-length", Integer.toString(contentLength));
					Request.Headers.put("content-type", contentType);
				}
			}
			if(file.isDirectory() && Request.Method.equals("GET")){
				File[] fileArray = file.listFiles();
				StringBuilder builder = new StringBuilder(str);
				for(int i=0; i<fileArray.length; i++){
					builder.append(fileArray[i].getName()+"\n");
				}
				str = builder.toString();
			}
		}
		return str;
	}

}
