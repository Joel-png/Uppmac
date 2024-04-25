package com.uppmacparser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
    XSSFWorkbook workbook;
    XSSFSheet spreadsheet;
    XSSFRow row;
    ArrayList<Object[]> ntaData;

    int rowIndex = 0;

    public ExcelWriter() {
        System.out.println("//* ignore this");
        workbook = new XSSFWorkbook();
        System.out.println("*//");
        spreadsheet = workbook.createSheet(" Uppaal NTA Data "); 
        ntaData = new ArrayList<Object[]>(); 
    }

    public void writeRow(Object[] object) throws InterruptedException {
        System.out.println(rowIndex++);
        printObject(object);
        ntaData.add(object);
    }

    public void printObject(Object[] object) {
        for (int i = 0; i < object.length; i++) {
            System.out.print(object[i] + " ");
        }
        System.out.println();
    }

    public void finish() throws IOException {
  
        // writing the data into the sheets... 
        for (int i = 0; i < ntaData.size(); i++) {
            printObject(ntaData.get(i));
        }

        for (int i = 0; i < ntaData.size(); i++) {
            row = spreadsheet.createRow(i+1);
            Object[] objectArr = ntaData.get(i);
            int cellid = 0;
            for (Object obj : objectArr) { 
                
                Cell cell = row.createCell(cellid++); 
                cell.setCellValue((String)obj); 
            } 
        }

        FileOutputStream out = new FileOutputStream( 
            new File("nta.xlsx")); 
  
        workbook.write(out); 
        workbook.close();
        out.close(); 
    }
}
