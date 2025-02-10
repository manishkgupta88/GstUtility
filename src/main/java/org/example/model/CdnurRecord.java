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
public class CdnurRecord {
    private String urType;
    private String noteNo;
    private String noteDate;
    private String noteType;
    private String placeOfSupply;
    private String noteValue;
    private String applicableTaxRate;
    private String taxRate;
    private String taxableValue;
    private String cessAmount;
}
