package com.mauvaisetroupe.eadesignit.service.importfile.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class ExcelUtils {

    // 50 characters
    private static final int MAX_COLUMN_SIZE = 50 * 256;

    public static void autoSizeAllColumns(Sheet sheet) {
        int nbColumns = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int i = 0; i < nbColumns; i++) {
            sheet.autoSizeColumn(i);
            if (sheet.getColumnWidth(i) > MAX_COLUMN_SIZE) {
                sheet.setColumnWidth(i, MAX_COLUMN_SIZE);
            }
        }
    }

    public static void addHeaderColorAndFilte(Workbook workbook, Sheet sheet) {
        CellStyle style = createStyle(sheet, "aab3f2");
        int nbColumns = sheet.getRow(0).getPhysicalNumberOfCells();
        sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, nbColumns - 1));
        for (int i = 0; i < nbColumns; i++) {
            sheet.getRow(0).getCell(i).setCellStyle(style);
        }
    }

    public static void alternateColors(Workbook workbook, Sheet sheet, int columnNumberForPattern) {
        CellStyle style1 = createStyle(sheet, "f7f7e6");
        CellStyle style2 = createStyle(sheet, "d5e5ed");

        CellStyle currenStyle = style1;

        if (sheet == null || sheet.getRow(1) == null || sheet.getRow(1).getCell(columnNumberForPattern) == null) {
            return;
        }

        String currentvalue = sheet.getRow(1).getCell(columnNumberForPattern).getStringCellValue();

        int nbColumns = sheet.getRow(0).getPhysicalNumberOfCells();
        int nbRows = sheet.getPhysicalNumberOfRows();
        for (int i = 1; i < nbRows; i++) {
            Row row = sheet.getRow(i);
            String pattern = row.getCell(columnNumberForPattern).getStringCellValue();
            if (!currentvalue.equals(pattern)) {
                if (currenStyle == style1) {
                    currenStyle = style2;
                } else {
                    currenStyle = style1;
                }
                currentvalue = pattern;
            }
            for (int j = 0; j < nbColumns; j++) {
                if (row.getCell(j) == null) {
                    row.createCell(j);
                }
                row.getCell(j).setCellStyle(currenStyle);
            }
        }
    }

    private static CellStyle createStyle(Sheet sheet, String rgb) {
        XSSFCellStyle style = (XSSFCellStyle) sheet.getWorkbook().createCellStyle();
        byte[] rgbB;
        try {
            rgbB = Hex.decodeHex(rgb);
            // get byte array from hex string
            XSSFColor color = new XSSFColor(rgbB, null); //IndexedColorMap has no usage until now. So it can be set null.
            style.setFillForegroundColor(color);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return style;
    }
}
