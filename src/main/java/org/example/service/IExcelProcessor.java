package org.example.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.GstSheet;

import java.util.List;

/**
 * User : Manish K. Gupta
 */

public interface IExcelProcessor {

    GstSheet read(Sheet sheet);

    GstSheet merge(List<GstSheet> gstSheets);

    void write(String path, GstSheet gstSheet);
}
