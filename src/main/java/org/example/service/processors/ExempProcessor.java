package org.example.service.processors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.ExempRecord;
import org.example.model.ExempSheet;
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

public class ExempProcessor implements IExcelProcessor {
    @Override
    public GstSheet read(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        Iterator<Row> itr = sheet.iterator();
        ExempSheet sheetObj = new ExempSheet();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, sheetObj);
            if (rc == 4) {
                break;
            }
        }
        List<ExempRecord> records = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            ExempRecord record = parseInvoiceRow(row);
            records.add(record);
        }
        sheetObj.setRecords(records);
        return sheetObj;
    }

    private ExempRecord parseInvoiceRow(Row row) {
        ExempRecord record = new ExempRecord();
        record.setDescription(Helper.getCellValueAsString(row.getCell(0)))
                .setNilSupply(Helper.getCellValueAsString(row.getCell(1)))
                .setExemptedSupply(Helper.getCellValueAsString(row.getCell(2)))
                .setNonGstSupply(Helper.getCellValueAsString(row.getCell(3)));
        return record;
    }

    private void parseRow(int rc, Row row, ExempSheet cdnrSheet) {
        switch (rc) {
            case 1:
                DataPair title =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0)));
                cdnrSheet.setTitle(title);
                break;
            case 2:
                DataPair nil = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(1)));
                DataPair exempted = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(2)));
                DataPair nonGst = new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(3)));
                cdnrSheet.setNilRatedSupplies(nil);
                cdnrSheet.setExemptedSupplies(exempted);
                cdnrSheet.setNonGstSupplies(nonGst);
                break;
            case 3:
                Double nilDbl = Helper.getCellValueAsDouble(row.getCell(1));
                Double exemptedDbl = Helper.getCellValueAsDouble(row.getCell(2));
                Double nonGstDbl = Helper.getCellValueAsDouble(row.getCell(3));

                cdnrSheet.getNilRatedSupplies().setValue(String.valueOf(nilDbl));
                cdnrSheet.getExemptedSupplies().setValue(String.valueOf(exemptedDbl));
                cdnrSheet.getNonGstSupplies().setValue(String.valueOf(nonGstDbl));

                cdnrSheet.setTotalNilRatedSupplies(nilDbl);
                cdnrSheet.setTotalExemptedSupplies(exemptedDbl);
                cdnrSheet.setTotalNonGstSupplies(nonGstDbl);
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
