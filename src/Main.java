import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("You need to specify a path!");
            System.exit(1);
        }

        if (args[0] == null || args[0].trim().isEmpty()) {
            System.out.println("You need to specify a path!");
            System.exit(1);
        } 
        
        String extension = "";
        int i = args[0].lastIndexOf('.');
        if (i >= 0) { extension = args[0].substring(i+1); }
        System.out.println(extension);
        if (new String("xml").equals(extension)) {
            File CP_file = new File(args[0]);

        } else {
            System.out.println("Specified file is of type '" + extension + "', please use a file with type 'xml'");
        }
    }
}
