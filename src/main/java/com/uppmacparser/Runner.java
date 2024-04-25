package com.uppmacparser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Runner {

    Navigator navigator;
    
    public Runner () {

    }

    public Navigator run(String[] args) throws Exception {

        
        if (args.length == 0) {
            System.out.println("You need to specify a path!");
            System.out.println("For mass property analysis use the args '-a [folder]'");
            System.exit(1);
        }
        
        ExcelWriter excelWriter = new ExcelWriter();
        // excel column names
        excelWriter.writeRow(new Object[] {"NTA name", "Template Name", "numPopularLocations", "isLinear", "hasLonelyInit", "isSingleLocation"});

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
                
                // all the computing of file is done here
                DefaultParser parser = new DefaultParser(formatXML(inputFile), args[0]);
                navigator = new Navigator(parser.parse(), args[0], excelWriter);
                navigator.navigate();
                
            } else {
                System.out.println("Specified file is of type '" + extension + "', please use a file with type 'xml'");
                System.exit(1);
            }
            System.exit(1);
        }

        // property analytics
        if (args.length == 2) {
            // folder -ap
            if (args[0].equals(new String("-ap"))) {
                System.out.println();
                System.out.println("-- UPPMAC --");
                System.out.println();
                System.out.println("Analysing all .xml files in specified folder");
                System.out.println();
                File inputDirectory = new File(args[1]);
                //File tempFile = new File("temp.xml");
                String contents[] = inputDirectory.list();

                for (int j = 0; j < contents.length; j++) {
                    System.out.println("Analysing file: " + contents[j]);
                    
                    // pass file if .xml
                    String extension = "";
                    int i = contents[j].lastIndexOf('.');
                    if (i >= 0) { extension = contents[j].substring(i+1); }
                        //System.out.println(extension);
                    if (new String("xml").equals(extension)) {
                        File inputFile = new File(inputDirectory.getAbsolutePath() + "\\" + contents[j]);
                        
                        // all the computing of file is done here
                        DefaultParser parser = new DefaultParser(formatXML(inputFile), contents[j]);
                        navigator = new Navigator(parser.parse(), args[0], excelWriter);
                        navigator.getProperties();
                        
                        
                    } else {
                        System.out.println("Specified file is of type '" + extension + "', please use a file with type 'xml'");
                    }
                }
                excelWriter.finish();

            // file -p
            } else if (args[0].equals(new String("-p"))) {
                System.out.println();
                System.out.println("-- UPPMAC --");
                System.out.println();
                System.out.println("Analysing .xml file specified");
                System.out.println();
        
                System.out.println("Analysing file: " + args[1]);
                excelWriter.writeRow(new Object[] {args[1]});
                // pass file if .xml
                String extension = "";
                int i = args[1].lastIndexOf('.');
                if (i >= 0) { extension = args[1].substring(i+1); }
                    //System.out.println(extension);
                if (new String("xml").equals(extension)) {
                    File inputFile = new File(args[1]);

                    // all the computing of file is done here
                    DefaultParser parser = new DefaultParser(formatXML(inputFile), args[1]);
                    navigator = new Navigator(parser.parse(), args[0], excelWriter);
                    navigator.getProperties();
                    excelWriter.finish();
                    
                } else {
                    System.out.println("Specified file is of type '" + extension + "', please use a file with type 'xml'");
                }
            }   else {
                System.out.println("For printing properties use '-p [xml file]', for all files in directory use '-ap [folder]'");
            }
        }
        return navigator;
    }

    public File formatXML(File inputFile) throws IOException {
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

        return tempFile;
    }

}
