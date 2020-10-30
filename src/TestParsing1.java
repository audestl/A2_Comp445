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

		File file = new File("testFolder", "test.txt");
		if(file.createNewFile()){
			System.out.println("File created");
		}
		String actual = Files.readString(file.toPath());
		System.out.println(actual);
		if(file.canRead()){
			System.out.println("Can Read");
		}
	}
}
