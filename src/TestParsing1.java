import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestParsing1 {

	public static void main(String[] args) throws IOException {

		File file = new File("testFolder", "SomeFolder");
		if(file.mkdir()){
			System.out.println("File created");
		}
		File file2 = new File("testFolder", "otherTest.txt");
		if(file2.createNewFile()){
			System.out.println("File2 created");
		}

		// changing the file permissions for test.txt
		boolean ePermissionChanged = file.setExecutable(false);
		boolean rPermissionChanged = file.setReadable(false);
		boolean wPermissionChanged = file.setWritable(true);
		System.out.println("File permissions changed." + ePermissionChanged + rPermissionChanged + wPermissionChanged);

		// printing the permissions associated with the file currently
		System.out.println("Executable: " + file.canExecute());
		System.out.println("Readable: " + file.canRead());
		System.out.println("Writable: "+ file.canWrite());

	}
}
