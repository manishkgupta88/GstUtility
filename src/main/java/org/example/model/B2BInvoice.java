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
public class B2BInvoice {
    private String gstin;
    private String partyName;
    private String invoiceNo;
    private String invoiceDate;
    private String invoiceValue;
    private String placeOfSupply;
    private String reverseCharge;
    private String applicableTaxRate;
    private String invoiceType;
    private String ecommGstin;
    private String taxRate;
    private String taxableValue;
    private String cessAmount;
}
