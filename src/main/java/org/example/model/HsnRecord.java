package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.math.NumberUtils;

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

    public ItemHsn getItemHsn() {
        return new ItemHsn()
                .setHsn(this.getHsn())
                .setQuantity(NumberUtils.toInt(this.getQuantity()))
                .setValue(NumberUtils.toInt(this.getValue()))
                .setRate(NumberUtils.toInt(this.getRate()))
                .setTaxable(NumberUtils.toDouble(this.getTaxableValue()))
                .setIntegrateTax(NumberUtils.toDouble(this.getIntegrateTax()))
                .setCentralTax(NumberUtils.toDouble(this.getCentralTax()))
                .setStateTax(NumberUtils.toDouble(this.getStateTax()))
                .setCess(NumberUtils.toDouble(this.getCess()));


    }
}
