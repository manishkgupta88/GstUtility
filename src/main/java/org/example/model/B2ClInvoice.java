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
public class B2ClInvoice {
    private String invoiceNo;
    private String invoiceDate;
    private String invoiceValue;
    private String placeOfSupply;
    private String applicableTaxRate;
    private String taxRate;
    private String taxableValue;
    private String cessAmount;
    private String ecommGstin;
}
