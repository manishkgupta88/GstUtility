package org.manitech.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.manitech.model.DataPair;

import java.io.File;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * User : Manish K. Gupta
 */

public class Helper {

    public static DataPair getCellData(Cell cell) {
        if (cell == null) {
            return null;
        }
        return new DataPair().setType(cell.getCellType()).setValue(getCellValueAsString(cell));
    }

    private static String getCellValueAsString(Cell cell) {
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() :
                String.valueOf(cell.getNumericCellValue());
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

    public static int getUniqueCountFromList(LinkedList<LinkedList<DataPair>> records, int index) {
        if (CollectionUtils.isEmpty(records)) {
            return 0;
        }
        return records.stream()
                .filter(record -> record != null && record.size() > index && StringUtils.isNotEmpty(record.get(index).getValue()))
                .map(record -> record.get(index).getValue())
                .collect(Collectors.toSet())
                .size();

    }
}
