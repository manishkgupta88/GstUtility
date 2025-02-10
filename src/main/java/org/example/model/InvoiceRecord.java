package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * User : Manish K. Gupta
 */

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class InvoiceRecord {
    private String gstin;
    private String partyName;
    private String transactionType;
    private String invoiceNo;
    private String invoiceDate;
    private String invoiceValue;
    private String taxRate;
    private String cessRate;
    private String taxableValue;
    private String reverseCharge;
    private String taxAmount;
    private String centralTaxAmount;
    private String stateTaxAmount;
    private String cessAmount;
    private String placeOfSupply;
}
