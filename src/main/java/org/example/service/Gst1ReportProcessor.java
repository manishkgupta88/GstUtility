package org.example.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.model.DataPair;
import org.example.model.GstR1Report;
import org.example.model.GstSheet;
import org.example.model.InvoiceRecord;
import org.example.util.Helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

public class Gst1ReportProcessor implements IExcelProcessor {

    @Override
    public GstSheet read(Sheet sheet) {
        Iterator<Row> itr = sheet.iterator();
        GstR1Report gstR1Report = new GstR1Report();
        int rc = 0;
        while (itr.hasNext()) {
            ++rc;
            Row row = itr.next();
            parseRow(rc, row, gstR1Report);
            if (rc == 7) {
                break;
            }
        }
        List<InvoiceRecord> invoiceRecords = new ArrayList<>();
        Row row = null;
        while (itr.hasNext()) {
            row = itr.next();
            InvoiceRecord invoice = parseInvoiceRow(row);
            if (StringUtils.isEmpty(invoice.getPartyName())) {
                break;
            }
            invoiceRecords.add(invoice);
        }
        gstR1Report.setInvoiceRecords(invoiceRecords);
        parseTotalRow(gstR1Report, row);
        return gstR1Report;
    }

    private void parseTotalRow(GstR1Report gstR1Report, Row row) {
        gstR1Report
                .setTotalInvoiceValue(Helper.getCellValueAsDouble(row.getCell(5)))
                .setTotalTaxableValue(Helper.getCellValueAsDouble(row.getCell(8)))
                .setTotalTaxAmount(Helper.getCellValueAsDouble(row.getCell(10)))
                .setTotalCentralTaxAmount(Helper.getCellValueAsDouble(row.getCell(11)))
                .setTotalStateTaxAmount(Helper.getCellValueAsDouble(row.getCell(12)))
                .setTotalCessAmount(Helper.getCellValueAsDouble(row.getCell(13)));
    }

    private InvoiceRecord parseInvoiceRow(Row row) {
        InvoiceRecord invoice = new InvoiceRecord();
        invoice.setGstin(Helper.getCellValueAsString(row.getCell(0)))
                .setPartyName(Helper.getCellValueAsString(row.getCell(1)))
                .setTransactionType(Helper.getCellValueAsString(row.getCell(2)))
                .setInvoiceNo(Helper.getCellValueAsString(row.getCell(3)))
                .setInvoiceDate(Helper.getCellValueAsString(row.getCell(4)))
                .setInvoiceValue(Helper.getCellValueAsString(row.getCell(5)))
                .setTaxRate(Helper.getCellValueAsString(row.getCell(6)))
                .setCessRate(Helper.getCellValueAsString(row.getCell(7)))
                .setTaxableValue(Helper.getCellValueAsString(row.getCell(8)))
                .setReverseCharge(Helper.getCellValueAsString(row.getCell(9)))
                .setTaxAmount(Helper.getCellValueAsString(row.getCell(10)))
                .setCentralTaxAmount(Helper.getCellValueAsString(row.getCell(11)))
                .setStateTaxAmount(Helper.getCellValueAsString(row.getCell(12)))
                .setCessAmount(Helper.getCellValueAsString(row.getCell(13)))
                .setPlaceOfSupply(Helper.getCellValueAsString(row.getCell(14)));
        return invoice;
    }

    private void parseRow(int rc, Row row, GstR1Report gstR1Report) {
        switch (rc) {
            case 1:
                DataPair period =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                gstR1Report.setPeriod(period);
                break;
            case 3:
                DataPair gstin =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                gstR1Report.setGstin(gstin);
                break;
            case 4:
                DataPair legalName =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                gstR1Report.setLegalName(legalName);
                break;
            case 5:
                DataPair traderName =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                gstR1Report.setTradeName(traderName);
                break;
            case 6:
                DataPair atpfy =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                gstR1Report.setAggregateTurnoverOfPrecedingFinancialYear(atpfy);
                break;
            case 7:
                DataPair at =
                        new DataPair().setLabel(Helper.getCellValueAsString(row.getCell(0))).setValue(Helper.getCellValueAsString(row.getCell(1)));
                gstR1Report.setAggregateTurnover(at);
                break;
        }
    }

    @Override
    public void write(String path, GstSheet gstSheet, boolean includeHeaders) {
        System.out.println("test");
    }
}
