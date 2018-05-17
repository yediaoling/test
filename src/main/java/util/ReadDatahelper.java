package util;

import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by zjuCHENW on 2016/5/13.
 */
public class ReadDatahelper {
    private int rows;
    private int columns;
    private  int[][] data;
    public int[][] getFromXLS(String url){
        File file = new File(url);
        try {
            InputStream in = new FileInputStream(file);
            Workbook workbook =Workbook.getWorkbook(in);
            Sheet sheet = workbook.getSheet(0);
            rows = sheet.getRows();
            columns = sheet.getColumns();

            data = new int[rows][columns];
            for(int i=0;i<rows;i++)
                for(int j=0;j<columns;j++){
                    data[i][j] = Integer.valueOf(sheet.getCell(j,i).getContents());
                }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
