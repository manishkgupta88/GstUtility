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
public class AtRecord {
    private String placeOfSupply;
    private String applicableTaxRate;
    private String taxRate;
    private String grossAdvanceTax;
    private String cessAmount;
}
