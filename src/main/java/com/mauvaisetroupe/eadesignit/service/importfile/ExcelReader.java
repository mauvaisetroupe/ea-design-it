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
import org.springframework.web.multipart.MultipartFile;

public class ExcelReader {

    private final Logger log = LoggerFactory.getLogger(ExcelReader.class);
    private final Map<String, List<Map<String, Object>>> excelDF;
    private final List<String> columnNames;
    private final List<String> sheetName;

    public ExcelReader(MultipartFile excel, List<String> columnNames, String... sheetName) throws Exception {
        this.columnNames = columnNames;
        this.excelDF = importExcel(excel);
        this.sheetName = Arrays.asList(sheetName);
    }

    private Map<String, List<Map<String, Object>>> importExcel(MultipartFile excel) throws Exception {
        return importExcel(excel.getInputStream());
    }

    private Map<String, List<Map<String, Object>>> importExcel(InputStream excel) throws EncryptedDocumentException, IOException {
        Workbook workbook = WorkbookFactory.create(excel);
        Map<String, List<Map<String, Object>>> sheetAsMapOfArray = new LinkedHashMap<>();
        for (int sheetnum = 0; sheetnum < workbook.getNumberOfSheets(); sheetnum++) {
            Sheet sheet = workbook.getSheetAt(sheetnum);
            // System.out.println("Sheet : " + workbook.getSheetName(sheetnum));
            // First row is header
            Row firstRow = sheet.getRow(0);
            if (firstRow != null) {
                String[] labels = new String[firstRow.getLastCellNum()];

                for (int j = 0; j < firstRow.getLastCellNum(); j++) {
                    String cellVal = CellUtil.getCell(firstRow, j).getStringCellValue();
                    if (StringUtils.hasText(cellVal)) {
                        labels[j] = removeParenthesis(CellUtil.getCell(firstRow, j).getStringCellValue());
                        if (this.sheetName != null && this.sheetName.contains(sheet.getSheetName())) {
                            Assert.isTrue(
                                columnNames.contains(labels[j]),
                                "Could not find column name '" + labels[j] + "' in Excel File. Authorized values : " + this.columnNames
                            );
                        }
                    } else {
                        log.error("Ignoring column numer #" + j + ". Stop reading other columns");
                        break;
                    }
                }
                System.out.println(sheet.getSheetName());
                List<Map<String, Object>> rowAsArrayList = new ArrayList<>();
                for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
                    Map<String, Object> rowAsArray = new HashMap<>();
                    Row row = sheet.getRow(rownum);
                    // System.out.println(rownum);
                    for (int colnum = 0; colnum < labels.length; colnum++) {
                        Cell cell = row.getCell(colnum);
                        Object value = null;
                        if (cell != null) {
                            value = getCellValue(cell);
                        }
                        rowAsArray.put(labels[colnum], value);
                    }
                    // System.out.println("Row as Array : " + rowAsArray );
                    if (!isEmpty(rowAsArray)) rowAsArrayList.add(rowAsArray);
                }
                sheetAsMapOfArray.put(workbook.getSheetName(sheetnum), rowAsArrayList);
                // System.out.println("Excel Util : " + rowAsArrayList);
            }
        }

        return sheetAsMapOfArray;
    }

    private String removeParenthesis(String stringCellValue) {
        if (stringCellValue == null) return null;
        if (stringCellValue.contains("(")) return stringCellValue.substring(0, stringCellValue.indexOf("("));
        return stringCellValue;
    }

    public boolean isEmpty(Map<String, Object> rowAsArray) {
        for (Object cell : rowAsArray.values()) {
            if (!ObjectUtils.isEmpty(cell)) return false;
        }
        return true;
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

    public List<Map<String, Object>> getSheetAt(int i) {
        return this.excelDF.entrySet().iterator().next().getValue();
    }

    public List<Map<String, Object>> getSheet(String sheetname) {
        return this.excelDF.get(sheetname);
    }
}
