package com.uppmacparser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;

public class ExcelWriter {
    String[] titles = {"", "name", "lonelyInit", "locations", "transitions", "declarationLength", "functions", "variables", "flowers", "dag", "single", "deadEnds"};
    String[] ntaTitles = {"nameName", "globalFunctions", "globalVariables", "globalComplexity", "templateFunctions", "templateVariables", "templateComplexity"};
    String[] fixedTitles;
    ArrayList<String[]> ntaData;

    int rowIndex = 0;

    public ExcelWriter() {
        ntaData = new ArrayList<String[]>(); 
        fixedTitles = fixTitles();
    }

    public String[] fixTitles() {
        String[] newTitles = new String[titles.length];
        for (int i = 0; i < newTitles.length; i++) {
            newTitles[i] = titles[i];
        }
        newTitles[1] = "         name         ";
        return newTitles;
    }

    public void writeRow(String[] strings) {
        //System.out.println(rowIndex++);
        //printObject(strings);
        ntaData.add(strings);
    }

    public void printObject(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            System.out.print(strings[i] + " ");
        }
        System.out.println();
    }

    public void finish() throws IOException {
        File outputFile = new File("output/output.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        // writing the data into the sheets... 
        // for (int i = 0; i < ntaData.size(); i++) {
        //     printObject(ntaData.get(i));
        // }

        for (int i = 0; i < ntaData.size(); i++) {
            //row = spreadsheet.createRow(i+1);
            String[] stringArr = ntaData.get(i);
            //int cellid = 0;
            String data = "";
            for (int j = 0; j < stringArr.length; j++) { 
                String str = stringArr[j];
                //Cell cell = row.createCell(cellid++); 
                //cell.setCellValue((String)obj); 
                if (stringArr.length == titles.length) {
                    str = formatLength(str, fixedTitles[j].length());
                }
                
                data += str + " | ";
                
            } 
            writer.write(data + System.getProperty("line.separator"));
        }

        writer.close();
        
        //workbook.write(out); 
        //workbook.close();
        //out.close(); 
    }

    public void writeIndividualData(ArrayList<TemplateProperty> templateProperties) throws IOException {
        String[] data = new String[titles.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = "" + titles[i] + System.getProperty("line.separator");
        }

        for (int i = 0; i < templateProperties.size(); i++) {
            TemplateProperty currentTemplateProperty = templateProperties.get(i);

            if (isOutlier(currentTemplateProperty)) {
                for (int j = 1; j < data.length; j++) { 
                    // "", "         name         ", "lonelyInit", "locations", "transitions", "declarationLength", "functions", "variables", "flowers", "dag", "single", "deadEnds"
                    data[j] += currentTemplateProperty.getData(j) + " ";
                }
            }
        }

        for (int i = 1; i < data.length; i++) {
            String filename = "output/" + titles[i] + ".txt";
            File outputFile = new File(new String(filename));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(data[i]);
            writer.close();
        }
    }

    public void writeNtaData(ArrayList<Nta> ntas) throws IOException {
        String[] data = new String[ntaTitles.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = "" + ntaTitles[i] + System.getProperty("line.separator");
        }

        for (int i = 0; i < ntas.size(); i++) {
            Nta currentNta = ntas.get(i);

            for (int j = 0; j < data.length; j++) { 
                data[j] += currentNta.getData(j) + " ";
            }
        }

        for (int i = 0; i < data.length; i++) {
            String filename = "output/ntaOutput/" + ntaTitles[i] + ".txt";
            File outputFile = new File(new String(filename));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(data[i]);
            writer.close();
        }
    }

    public boolean isOutlier(TemplateProperty tempProp) {
        if (tempProp.numLocations > 200) {
            return false;
        }
        if (tempProp.variables > 100) {
            return false;
        }
        return true;
    }

    public String formatLength(String data, int length) {
        String newData = data;
        for (int i = 0; i < length - data.length(); i++) {
            newData += " ";
        }
        return newData;
    }
}
