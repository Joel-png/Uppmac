package com.uppmacparser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Runner {

    Navigator navigator;
    ExcelWriter excelWriter;
    ArrayList<Nta> ntas = new ArrayList<Nta>();
    ArrayList<TemplateProperty> templateProperties = new ArrayList<TemplateProperty>();

    public Runner () {

    }

    public Navigator run(String[] args) throws Exception {

        
        if (args.length == 0) {
            System.out.println("You need to specify a path!");
            System.out.println("For mass property analysis use the args '-a [folder]'");
            System.exit(1);
        }
        
        excelWriter = new ExcelWriter();
        // excel column names
        excelWriter.writeRow(new String[] {"NTA name", "Template Name", "numPopularLocations", "isLinear", "hasLonelyInit", "isSingleLocation"});

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

                traverseFolder(inputDirectory.getAbsolutePath(), "");
                // for (int j = 0; j < contents.length; j++) {
                //     System.out.println("Analysing file: " + contents[j]);
                    
                //     // pass file if .xml
                //     String extension = "";
                //     int i = contents[j].lastIndexOf('.');
                //     if (i >= 0) { extension = contents[j].substring(i+1); }
                //         //System.out.println(extension);
                //     if (new String("xml").equals(extension)) {
                //         File inputFile = new File(inputDirectory.getAbsolutePath() + "\\" + contents[j]);
                        
                //         // all the computing of file is done here
                //         DefaultParser parser = new DefaultParser(formatXML(inputFile), contents[j]);
                //         navigator = new Navigator(parser.parse(), args[0], excelWriter);
                //         navigator.getProperties();
                        
                        
                //     } else {
                //         System.out.println("Specified file is of type '" + extension + "', please use a file with type 'xml'");
                //     }
                // }
                excelWriter.writeIndividualData(templateProperties);
                excelWriter.writeNtaData(ntas);
                excelWriter.finish();
                



            // file -p
            } else if (args[0].equals(new String("-p"))) {
                System.out.println();
                System.out.println("-- UPPMAC --");
                System.out.println();
                System.out.println("Analysing .xml file specified");
                System.out.println();
        
                System.out.println("Analysing file: " + args[1]);
                excelWriter.writeRow(new String[] {args[1]});
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

        if (args.length == 3) {
            if (args[0].equals(new String("-merge"))) {
                
                StringBuilder content = new StringBuilder();
                String x;
                String line;
                try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
                    line = br.readLine();
                    while ((line = br.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    x = content.toString();
                }
                String y;
                content = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader(args[2]))) {
                    line = br.readLine();
                    while ((line = br.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    y = content.toString();
                }
                String output = merge(x, y, args[1] + args[2]);
                File outputFile = new File("output/merged.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
                writer.write(output);
                writer.close();
            }
        }

        if (args.length == 4) {
            if (args[0].equals(new String("-find"))) {
                
                StringBuilder content = new StringBuilder();
                String x;
                String line;
                try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
                    line = br.readLine();
                    while ((line = br.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    x = content.toString();
                }
                int output = findInString(x, args[2], args[3]);
                System.out.println("Found in index: " + output);
            }
        }

        if (args.length == 3) {
            if (args[0].equals(new String("-fetch"))) {
                
                StringBuilder content = new StringBuilder();
                String x;
                String line;
                try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
                    line = br.readLine();
                    while ((line = br.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    x = content.toString();
                }
                String output = fetchInString(x, Integer.valueOf(args[2]), " ");
                System.out.println("Found result: " + output);
            }

            if (args[0].equals(new String("-fetchname"))) {
                
                StringBuilder content = new StringBuilder();
                String x;
                String line;
                try (BufferedReader br = new BufferedReader(new FileReader(args[1]))) {
                    line = br.readLine();
                    while ((line = br.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    x = content.toString();
                }
                String output = fetchInString(x, Integer.valueOf(args[2]), ".xml");
                System.out.println("Found result: " + output);
            }
        }

        return navigator;
    }

    public String merge(String xValue, String yValue, String name) {
        String[] xArray = xValue.split(" ");
        String[] yArray = yValue.split(" ");

        StringBuilder result = new StringBuilder();
        result.append(name + System.getProperty("line.separator"));
        for (int i = 0; i < xArray.length; i++) {
            result.append(xArray[i]).append(" ").append(yArray[i]).append(" ");
        }

        return result.toString();
    }

    public int findInString(String string, String find0, String find1) {
        
        String[] strings = string.split(" ");
        for (int i = 0; i+1 < strings.length; i+=2) {
            if (strings[i].equals(find0) && strings[i+1].equals(find1)) {
                return i/2;
            }
        }

        return -1;
    }

    public String fetchInString(String string, int i, String seperator) {
        String[] strings = string.split(seperator);
        return strings[i];
    }

    public void traverseFolder(String absolutePath, String name) throws Exception {
        System.out.println(absolutePath);
        File file = new File(absolutePath);
        if (file.isDirectory()) {
            String directoryContents[] = file.list();
            for (int i = 0; i < directoryContents.length; i++) {
                traverseFolder(absolutePath + "\\" + directoryContents[i], directoryContents[i]);
            }
        } else {
            analyze(absolutePath, name);
        }
    }

    public void addProperties(ArrayList<TemplateProperty> newProperties) {
        for (int i = 0; i < newProperties.size(); i++) {
            templateProperties.add(newProperties.get(i));
        }
    }
    public void analyze(String path, String name) throws Exception {
        System.out.println("Analysing file: " + name);
                    
                    // pass file if .xml
                    String extension = "";
                    int i = name.lastIndexOf('.');
                    if (i >= 0) { extension = name.substring(i+1); }
                        //System.out.println(extension);
                    if (new String("xml").equals(extension)) {
                        File inputFile = new File(path);
                        
                        // all the computing of file is done here
                        DefaultParser parser = new DefaultParser(formatXML(inputFile), name);
                        Nta currentNta = parser.parse();
                        navigator = new Navigator(currentNta, name, excelWriter);
                        navigator.getProperties();
                        addProperties(navigator.identifier.getTemplateProperties());
                        int[] decComplexityData = navigator.identifier.getTotalTemplateComplexity();
                        currentNta.templateFunctions = decComplexityData[0];
                        currentNta.templateVariables = decComplexityData[1];
                        currentNta.templateComplexity = decComplexityData[2];
                        currentNta.globalDecLength = currentNta.getLengthOfDeclaration(currentNta.globalDeclaration);
                        currentNta.totalTempDecLength = currentNta.totalTempDeclarationLength();
                        ntas.add(currentNta);
                    } else {
                        System.out.println("Specified file is of type '" + extension + "', please use a file with type 'xml'");
                    }
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
            currentLine = currentLine.replaceAll("[^\\x00-\\x7F]", "");
            writer.write(currentLine + System.getProperty("line.separator"));
        }
        writer.close();
        reader.close();

        
        return tempFile;
    }

}
