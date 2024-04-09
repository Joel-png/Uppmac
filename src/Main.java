import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Main {
    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println("You need to specify a path!");
            System.out.println("For mass property analysis use the args '-a [folder]'");
            System.exit(1);
        }
        
        if (args.length == 1) {
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

        // batch property analytics
        if (args.length == 2) {
            if (args[0].equals(new String("-a"))) {
                File inputDirectory = new File(args[1]);
                //File tempFile = new File("temp.xml");
                String contents[] = inputDirectory.list();
                for (int j = 0; j < contents.length; j++) {
                    System.out.println("Analysing file: " + contents[j]);
                    

                    String extension = "";
                    int i = contents[j].lastIndexOf('.');
                    if (i >= 0) { extension = contents[j].substring(i+1); }
                        //System.out.println(extension);
                    if (new String("xml").equals(extension)) {
                        File inputFile = new File(inputDirectory.getAbsolutePath() + "\\" + contents[j]);
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
                        navigator.getProperties();
                        
                    } else {
                        System.out.println("Specified file is of type '" + extension + "', please use a file with type 'xml'");
                    }
                }
            } else {
                System.out.println("For mass property analysis use the args '-a [folder]'");
                System.exit(1);
            }
        }
    }
}
