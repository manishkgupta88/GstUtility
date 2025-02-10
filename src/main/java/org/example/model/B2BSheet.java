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
public class B2BSheet implements GstSheet {

    @Serial
    private static final long serialVersionUID = 6962105547943529654L;
    private DataPair title;
    DataPair recipients;
    DataPair invoices;
    DataPair invoiceValue;
    DataPair taxableValue;
    DataPair totalCess;
    private int numOfRecipients;
    private int numOfInvoices;
    private Double totalInvoiceValue;
    private Double totalTaxableValue;
    private Double totalCessAmount;
    private List<B2BInvoice> invoiceRecords;
}
