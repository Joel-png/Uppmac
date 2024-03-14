import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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
            //System.out.println(extension);
        if (new String("xml").equals(extension)) {
            File inputFile = new File(args[0]);
            File tempFile = new File("temp.xml");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            String lineToRemove = "<!DOCTYPE"; // xpath doesn't like this
            while((currentLine = reader.readLine()) != null) {
                if (currentLine.trim().length() > 8) {
                    String trimmedLine = currentLine.trim().substring(0, lineToRemove.length());
                    if(trimmedLine.equals(lineToRemove)) continue;
                }
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            
            DefaultParser parser = new DefaultParser(tempFile);
            Navigator navigator = new Navigator(parser.parse(), args[0]);
            navigator.navigate();
            
        } else {
            System.out.println("Specified file is of type '" + extension + "', please use a file with type 'xml'");
            System.exit(1);
        }
        System.exit(1);
    }
}
