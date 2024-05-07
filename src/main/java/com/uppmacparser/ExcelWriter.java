package com.uppmacparser;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;

public class ExcelWriter {
    String[] titles = {"", "         name         ", "lonelyInit", "locations", "transitions", "flowers", "linear", "dag", "single", "deadEnds"};

    ArrayList<String[]> ntaData;

    int rowIndex = 0;

    public ExcelWriter() {
        ntaData = new ArrayList<String[]>(); 
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
        File outputFile = new File("output.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        // writing the data into the sheets... 
        for (int i = 0; i < ntaData.size(); i++) {
            //printObject(ntaData.get(i));
        }

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
                    str = formatLength(str, titles[j].length());
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

    public String formatLength(String data, int length) {
        String newData = data;
        for (int i = 0; i < length - data.length(); i++) {
            newData += " ";
        }
        return newData;
    }
}
