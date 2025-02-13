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
public class HsnRecord {
    private String hsn;
    private String description;
    private String unit;
    private String quantity;
    private String value;
    private String rate;
    private String taxableValue;
    private String integrateTax;
    private String centralTax;
    private String stateTax;
    private String cess;
}
