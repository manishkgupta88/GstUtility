package org.example.service.processors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.CdnrRecord;
import org.example.model.CdnrSheet;
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
        CdnrSheet b2csSheet = new CdnrSheet();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, b2csSheet);
            if (rc == 4) {
                break;
            }
        }
        List<CdnrRecord> records = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            CdnrRecord invoice = parseInvoiceRow(row);
            records.add(invoice);
        }
        b2csSheet.setRecords(records);
        return b2csSheet;
    }

    private CdnrRecord parseInvoiceRow(Row row) {
        CdnrRecord invoice = new CdnrRecord();
        invoice
                .setGstin(Helper.getCellValueAsString(row.getCell(0)))
                .setPartyName(Helper.getCellValueAsString(row.getCell(1)))
                .setNoteNo(Helper.getCellValueAsString(row.getCell(2)))
                .setNoteDate(Helper.getCellValueAsString(row.getCell(3)))
                .setNoteType(Helper.getCellValueAsString(row.getCell(4)))
                .setPlaceOfSupply(Helper.getCellValueAsString(row.getCell(5)))
                .setReverseCharge(Helper.getCellValueAsString(row.getCell(6)))
                .setNoteSupplyType(Helper.getCellValueAsString(row.getCell(7)))
                .setNoteValue(Helper.getCellValueAsString(row.getCell(8)))
                .setApplicableTaxRate(Helper.getCellValueAsString(row.getCell(9)))
                .setTaxRate(Helper.getCellValueAsString(row.getCell(10)))
                .setTaxableValue(Helper.getCellValueAsString(row.getCell(11)))
                .setCessAmount(Helper.getCellValueAsString(row.getCell(12)));
        return invoice;
    }

    private void parseRow(int rc, Row row, CdnrSheet cdnrSheet) {
        switch (rc) {
            case 1:
                DataPair title =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                cdnrSheet.setTitle(title);
                break;
            case 2:
                DataPair recipients = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                DataPair notes = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(2)));
                DataPair noteValue = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(8)));
                DataPair taxableValue = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(11)));
                DataPair totalCess = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(12)));
                cdnrSheet.setRecipients(recipients);
                cdnrSheet.setNotes(notes);
                cdnrSheet.setNoteValue(noteValue);
                cdnrSheet.setTaxableValue(taxableValue);
                cdnrSheet.setTotalCess(totalCess);
                break;
            case 3:
                int recipientCount = Helper.getCellValueAsInt(row.getCell(0));
                int noteCount = Helper.getCellValueAsInt(row.getCell(2));
                Double noteVal = Helper.getCellValueAsDouble(row.getCell(8));
                Double taxableValueDbl = Helper.getCellValueAsDouble(row.getCell(11));
                Double totalCessDbl = Helper.getCellValueAsDouble(row.getCell(12));

                cdnrSheet.getRecipients().setValue(String.valueOf(recipientCount));
                cdnrSheet.getNotes().setValue(String.valueOf(noteCount));
                cdnrSheet.getNoteValue().setValue(String.valueOf(noteVal));
                cdnrSheet.getTaxableValue().setValue(String.valueOf(taxableValueDbl));
                cdnrSheet.getTotalCess().setValue(String.valueOf(totalCessDbl));

                cdnrSheet.setNumOfRecipients(recipientCount);
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
