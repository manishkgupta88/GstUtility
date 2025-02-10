package org.example.service.processors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.CdnurRecord;
import org.example.model.CdnurSheet;
import org.example.model.DataPair;
import org.example.model.GstSheet;
import org.example.service.IExcelProcessor;
import org.example.util.Helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class CdnrProcessor implements IExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        Iterator<Row> itr = sheet.iterator();
        CdnurSheet sheetObj = new CdnurSheet();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, sheetObj);
            if (rc == 4) {
                break;
            }
        }
        List<CdnurRecord> records = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            CdnurRecord invoice = parseInvoiceRow(row);
            records.add(invoice);
        }
        sheetObj.setRecords(records);
        return sheetObj;
    }

    private CdnurRecord parseInvoiceRow(Row row) {
        CdnurRecord invoice = new CdnurRecord();
        invoice
                .setUrType(Helper.getCellValueAsString(row.getCell(0)))
                .setNoteNo(Helper.getCellValueAsString(row.getCell(1)))
                .setNoteDate(Helper.getCellValueAsString(row.getCell(2)))
                .setNoteType(Helper.getCellValueAsString(row.getCell(3)))
                .setPlaceOfSupply(Helper.getCellValueAsString(row.getCell(4)))
                .setNoteValue(Helper.getCellValueAsString(row.getCell(5)))
                .setApplicableTaxRate(Helper.getCellValueAsString(row.getCell(6)))
                .setTaxRate(Helper.getCellValueAsString(row.getCell(7)))
                .setTaxableValue(Helper.getCellValueAsString(row.getCell(8)))
                .setCessAmount(Helper.getCellValueAsString(row.getCell(9)));
        return invoice;
    }

    private void parseRow(int rc, Row row, CdnurSheet cdnrSheet) {
        switch (rc) {
            case 1:
                DataPair title =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                cdnrSheet.setTitle(title);
                break;
            case 2:
                DataPair notes = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(1)));
                DataPair noteValue = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(5)));
                DataPair taxableValue = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(8)));
                DataPair totalCess = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(9)));

                cdnrSheet.setNotes(notes);
                cdnrSheet.setNoteValue(noteValue);
                cdnrSheet.setTaxableValue(taxableValue);
                cdnrSheet.setTotalCess(totalCess);
                break;
            case 3:

                int noteCount = Helper.getCellValueAsInt(row.getCell(1));
                Double noteVal = Helper.getCellValueAsDouble(row.getCell(5));
                Double taxableValueDbl = Helper.getCellValueAsDouble(row.getCell(8));
                Double totalCessDbl = Helper.getCellValueAsDouble(row.getCell(9));
                
                cdnrSheet.getNotes().setValue(String.valueOf(noteCount));
                cdnrSheet.getNoteValue().setValue(String.valueOf(noteVal));
                cdnrSheet.getTaxableValue().setValue(String.valueOf(taxableValueDbl));
                cdnrSheet.getTotalCess().setValue(String.valueOf(totalCessDbl));
                
                cdnrSheet.setNumOfNotes(noteCount);
                cdnrSheet.setTotalNoteValue(noteVal);
                cdnrSheet.setTotalTaxableValue(taxableValueDbl);
                cdnrSheet.setTotalCessAmount(totalCessDbl);
                break;
        }
    }

    @Override
    public GstSheet merge(List<GstSheet> gstSheets) {
        return null;
    }

    @Override
    public void write(String path, GstSheet gstSheet) {

    }
}
