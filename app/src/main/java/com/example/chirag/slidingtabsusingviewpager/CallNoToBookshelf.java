package com.example.chirag.slidingtabsusingviewpager;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class CallNoToBookshelf {

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
                if (ShelfOrder.containsKey(lcc)) {
                    position[1] = ShelfOrder.get(lcc);
                } else {
                    position[1] = ShelfOrder.get("RD525");
                }
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
            if (ShelfOrder.containsKey(lcc)) {
                position[1] = ShelfOrder.get(lcc);
            } else {
                position[1] = ShelfOrder.get("N82");
            }
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
        if (position[0] == 0) {
            position[0] = 9;
        }
        System.out.println(position[0]);
        System.out.println(position[1]);
        return position;
    }

    public HashMap<String, Integer> read() {
        try {
            System.out.println("start");
            Map<String, Integer> dataset = new HashMap<String, Integer>();

            InputStream is = assetManager.open("booklocation");
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(is, "gbk");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            try {
                while ((line = reader.readLine()) != null) {

                    String[] temp = line.split(",");

                    if (temp.length == 2) {
                        if (!temp[1].equals("shelfNo")) {
                            dataset.put(temp[0], Integer.parseInt(temp[1]));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // FileInputStream excelFile = new FileInputStream(assetManager.open()new File(FILE_NAME));

//            workbook = new XSSFWorkbook(assetManager.open(FILE_NAME));
//            //workbook=new XSSFWorkbook(new FileInputStream(FILE_NAME));
//            Sheet dataTypeSheet = workbook.getSheetAt(0);

//            int rowStart = dataTypeSheet.getFirstRowNum() + 1;
//            int rowEnd = dataTypeSheet.getLastRowNum();
//            // gets rows progressively
//            for (int i = rowStart; i <= rowEnd; i++) {
//                Row currentRow = dataTypeSheet.getRow(i);
//                if (currentRow != null) {
//                    int current = currentRow.getFirstCellNum();
//                    Cell cell = currentRow.getCell(current);
//                    if (null != cell) {
//                        String lcc = cell.getStringCellValue();
//                        current++;
//                        cell = currentRow.getCell(current);
//                        int shelfNo = (int) cell.getNumericCellValue();
//                        dataset.put(lcc, shelfNo);
//                    }
//                }
//            }
            Log.e("dataset", Integer.toString(dataset.size()));
            return (HashMap<String, Integer>) dataset;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
