package org.example.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

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

}
