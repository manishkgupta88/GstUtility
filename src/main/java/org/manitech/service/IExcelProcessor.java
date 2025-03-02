package org.manitech.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.manitech.model.GstSheet;

import java.util.List;

/**
 * User : Manish K. Gupta
 */

public interface IExcelProcessor {

    GstSheet read(Sheet sheet);

    GstSheet merge(List<GstSheet> gstSheets);

    void write(Sheet wbSheet, GstSheet gstSheet);
}
