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
import javax.print.DocFlavor.STRING;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class ExcelReader {

    private static final int IGNORE_ROW_AFTER_N_EMPTY_CELL = 4;
    private final Logger log = LoggerFactory.getLogger(ExcelReader.class);
    Workbook workbook = null;

    public ExcelReader(InputStream excel) throws EncryptedDocumentException, IOException {
        if (excel != null) this.workbook = WorkbookFactory.create(excel);
    }

    public List<Map<String, Object>> getSheet(String sheetName) {
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
                                } else {
                                    value = ((String) value).trim();
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
        if (stringCellValue.contains("(")) {
            stringCellValue = stringCellValue.substring(0, stringCellValue.indexOf("("));
        }
        return stringCellValue.trim();
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
        if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                return date;
            } else {
                Double d = cell.getNumericCellValue();
                return d;
            }
        } else if (cellType == CellType.BOOLEAN) {
            return cell.getBooleanCellValue();
        } else if (cellType == CellType.FORMULA) {
            if (cell.getCachedFormulaResultType() == cellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCachedFormulaResultType() == cellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCachedFormulaResultType() == cellType.BOOLEAN) {
                return cell.getBooleanCellValue();
            } else if (cell.getCachedFormulaResultType() == cellType.ERROR) {
                return null; //check formula
            } else {
                log.error("Ignoring cell " + cell);
                return null;
            }
        }
        return cell.getStringCellValue();
    }
}
