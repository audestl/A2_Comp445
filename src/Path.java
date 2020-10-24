import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.LinkOption;

public class Path {

	public static void main(String[] args) {


		
		
		File folder = new File("/Users/Audrey-Laure/DataDirectory");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
			  
		    System.out.println(listOfFiles[i].getName());
		  } 
		  else if (listOfFiles[i].isDirectory()) {
		    
			  System.out.println(listOfFiles[i].getName());
		  }
		}
	
	}
}
