package org.example.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.GstSheet;

import java.util.List;

/**
 * User : Manish K. Gupta
 */

public interface IExcelProcessor {

    GstSheet read(Sheet sheet);

    void write(String path, List<GstSheet> gstSheetList, boolean includeHeaders);
}
