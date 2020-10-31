import java.io.File;
import java.io.IOException;

public class FilePermissionTest {

    public static void main(String[] args) throws IOException {

        File file = new File("testFolder", "test.txt");
        if(file.createNewFile()){
            System.out.println("File created");
        }
        File file2 = new File("testFolder", "nonWritableFile.txt");
        if(file2.createNewFile()){
            System.out.println("File2 created");
        }

        // changing the file permissions for test.txt
        boolean ePermissionChanged = file2.setExecutable(false);
        boolean rPermissionChanged = file2.setReadable(false);
        boolean wPermissionChanged = file2.setWritable(false, false);
        System.out.println("File permissions changed." + ePermissionChanged + rPermissionChanged + wPermissionChanged);

        // printing the permissions associated with the file currently
        System.out.println("Executable: " + file2.canExecute());
        System.out.println("Readable: " + file2.canRead());
        System.out.println("Writable: "+ file2.canWrite());

    }
}