package org.example.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.io.File;

/**
 * User : Manish K. Gupta
 */

public class Helper {

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return null;
        }
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() :
                String.valueOf(cell.getNumericCellValue());
    }

    public static Double getCellValueAsDouble(Cell cell) {
        String str = getCellValueAsString(cell);
        return NumberUtils.toDouble(str, 0.0);
    }

    public static int getCellValueAsInt(Cell cell) {
        Double val = getCellValueAsDouble(cell);
        return val.intValue();
    }

    public static String getOutputPath(String path) {
        String separator = File.separator;
        if (path.endsWith(separator)) {
            path = path.substring(0, path.lastIndexOf(separator));
        }
        if (separator.equals("\\")) {
            separator = "\\\\";
        }
        String[] arr = path.split(separator);
        separator = File.separator;
        if (arr != null && arr.length > 1) {
            int end = path.lastIndexOf(separator);
            return path.substring(0, end);
        }
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win") ? Constants.ExcelFile.WindowsOutputFile : Constants.ExcelFile.LinuxOutputFile;
    }

}
