package com.example.chirag.slidingtabsusingviewpager;

import android.content.res.AssetManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CallNoToBookshelf {
    private static final String FILE_NAME = "bookShelf.xlsx";
    private static Workbook workbook;
    AssetManager assetManager;

    public CallNoToBookshelf(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public int[] findBookShelf(String callNo) {
        Map<String, Integer> ShelfOrder = new HashMap<String, Integer>();
        ShelfOrder = read();
        String callNo1 = callNo.split("/")[0];
        String lcc = callNo1.split("\\.")[0];
        System.out.println(callNo1);
        int[] position = new int[4];
        char[] arr = callNo1.toCharArray();
        if (arr[0] == 'Q') {
            position[0] = 9;
            if (arr[1] >= '0' && arr[1] <= '9') {
                position[1] = 1;
            }
            if (lcc == "QA76") {
                position[1] = ShelfOrder.get(callNo1.split("\\.")[0] + callNo1.split("\\.")[1]);
            }
            if (arr[1] == 'B') {
                position[1] = 18;
            }
            if (arr[1] == 'L') {
                position[1] = 27;
            }
            if (arr[1] == 'R' || arr[1] == 'S') {
                position[1] = 29;
            }
            if (arr[1] == 'T' || arr[1] == 'U' || arr[1] == 'V' || arr[1] == 'W') {
                position[1] = 29;
            }
            if (position[1] == 0) {
                position[1] = ShelfOrder.get(lcc);
            }
        }
        if (arr[0] == 'R') {
            position[0] = 9;
            if (arr[1] >= '0' && arr[1] <= '9') {
                position[1] = 30;
            }
            if (arr[1] >= 'E' && arr[1] <= 'L') {
                position[1] = 33;
            }
            if (arr[1] >= 'S' && arr[1] <= 'Z')
                position[1] = 34;
        }
        if (position[1] == 0) {
            position[1] = ShelfOrder.get(lcc);
        }

        if (arr[0] == 'N') {
            position[0] = 8;
            if (arr[1] >= 'D' && arr[1] <= 'E') {
                position[1] = 11;
            }
        }

        if (arr[0] == 'T') {
            position[0] = 8;
            if (arr[1] == 'C') {
                position[1] = 19;
            }
            if (arr[1] >= 'E' && arr[1] <= 'G') {
                position[1] = 20;
            }
            if (arr[1] >= 'L' && arr[1] <= 'N') {
                position[1] = 31;
            }
            if (arr[1] == 'T') {
                position[1] = 34;
            }
        }
        System.out.println(position[0]);
        System.out.println(position[1]);
        return position;
    }

    public HashMap<String, Integer> read() {
        try {
            System.out.println("start");
            // extract Excel file

            // FileInputStream excelFile = new FileInputStream(assetManager.open()new File(FILE_NAME));
            workbook = new XSSFWorkbook(assetManager.open(FILE_NAME));
            Sheet dataTypeSheet = workbook.getSheetAt(0);
            Map<String, Integer> dataset = new HashMap<String, Integer>();
            int rowStart = dataTypeSheet.getFirstRowNum() + 1;
            int rowEnd = dataTypeSheet.getLastRowNum();
            // gets rows progressively
            for (int i = rowStart; i <= rowEnd; i++) {
                Row currentRow = dataTypeSheet.getRow(i);
                if (currentRow != null) {
                    int current = currentRow.getFirstCellNum();
                    Cell cell = currentRow.getCell(current);
                    if (null != cell) {
                        String lcc = cell.getStringCellValue();
                        current++;
                        cell = currentRow.getCell(current);
                        int shelfNo = (int) cell.getNumericCellValue();
                        dataset.put(lcc, shelfNo);
                    }
                }
            }
            return (HashMap<String, Integer>) dataset;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
