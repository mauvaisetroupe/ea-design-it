package com.mauvaisetroupe.eadesignit.service.importfile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class ExcelReader {

    private static final int IGNORE_ROW_AFTER_N_EMPTY_CELL = 4;
    private final Logger log = LoggerFactory.getLogger(ExcelReader.class);
    private List<Map<String, Object>> excelDF = new ArrayList<>();

    public List<Map<String, Object>> getExcelDF() {
        return excelDF;
    }

    public ExcelReader(InputStream excel, String sheetName) throws EncryptedDocumentException, IOException {
        if (excel != null && sheetName != null) this.excelDF = importExcel(excel, sheetName);
    }

    private List<Map<String, Object>> importExcel(InputStream excel, String sheetName) throws EncryptedDocumentException, IOException {
        Workbook workbook = WorkbookFactory.create(excel);
        Sheet sheet = workbook.getSheet(sheetName);
        List<Map<String, Object>> rowAsArrayList = new ArrayList<>();
        // First row is header
        Row firstRow = sheet.getRow(0);
        if (firstRow != null) {
            String[] labels = new String[firstRow.getLastCellNum()];

            for (int j = 0; j < firstRow.getLastCellNum(); j++) {
                String cellVal = CellUtil.getCell(firstRow, j).getStringCellValue();
                if (StringUtils.hasText(cellVal)) {
                    labels[j] = removeParenthesis(CellUtil.getCell(firstRow, j).getStringCellValue());
                }
            }
            System.out.println(sheet.getSheetName());
            for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
                Row row = sheet.getRow(rownum);
                Map<String, Object> rowAsArray = new HashMap<>();
                if (row != null) {
                    boolean isEmpty = true;
                    for (int colnum = 0; colnum < Math.min(row.getLastCellNum(), labels.length); colnum++) {
                        Cell cell = row.getCell(colnum);
                        Object value = null;
                        if (cell != null) {
                            value = getCellValue(cell);
                            if (value instanceof String) {
                                if (isNull((String) value)) {
                                    value = null;
                                }
                            }
                            // if 3 first cells are empty, do not consider the line
                            if (colnum < IGNORE_ROW_AFTER_N_EMPTY_CELL && !ObjectUtils.isEmpty(value)) isEmpty = false;
                        }
                        rowAsArray.put(labels[colnum], value);
                    }
                    if (!isEmpty) rowAsArrayList.add(rowAsArray);
                }
            }
            // System.out.println("Excel Util : " + rowAsArrayList);
        }
        return rowAsArrayList;
    }

    private String removeParenthesis(String stringCellValue) {
        if (stringCellValue == null) return null;
        if (stringCellValue.contains("(")) return stringCellValue.substring(0, stringCellValue.indexOf("("));
        return stringCellValue;
    }

    protected boolean isNull(String value) {
        if (!StringUtils.hasText(value)) return true;
        value = value.replace("?", "");
        if (!StringUtils.hasText(value)) return true;
        return false;
    }

    public Object getCellValue(Cell cell) {
        if (cell == null) return null;
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return date;
                } else {
                    Double d = cell.getNumericCellValue();
                    return d;
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return cell.getStringCellValue();
        }
    }
}
