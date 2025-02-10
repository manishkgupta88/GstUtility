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
public class CdnrRecord {
    private String gstin;
    private String partyName;
    private String noteNo;
    private String noteDate;
    private String noteType;
    private String placeOfSupply;
    private String reverseCharge;
    private String noteSupplyType;
    private String noteValue;
    private String applicableTaxRate;
    private String taxRate;
    private String taxableValue;
    private String cessAmount;
}
