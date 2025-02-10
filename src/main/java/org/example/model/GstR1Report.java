package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.util.List;

/**
 * User : Manish K. Gupta
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class GstR1Report implements GstSheet {
    @Serial
    private static final long serialVersionUID = 9024343212969550963L;
    private DataPair period;
    private DataPair gstin;
    private DataPair legalName;
    private DataPair tradeName;
    private DataPair aggregateTurnoverOfPrecedingFinancialYear;
    private DataPair aggregateTurnover;
    private List<InvoiceRecord> invoiceRecords;
    private Double totalInvoiceValue;
    private Double totalTaxableValue;
    private Double totalTaxAmount;
    private Double totalCentralTaxAmount;
    private Double totalStateTaxAmount;
    private Double totalCessAmount;


}
