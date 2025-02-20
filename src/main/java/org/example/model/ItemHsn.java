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
public class ItemHsn {
    private String hsn;
    private int quantity;
    private double value;
    private int rate;
    private double taxable;
    private double integrateTax;
    private double centralTax;
    private double stateTax;
    private double cess;

    public void merge(ItemHsn itemHsn) {
        this.quantity += itemHsn.getQuantity();
        this.value += itemHsn.getValue();
        this.taxable += itemHsn.getTaxable();
        this.integrateTax += itemHsn.getIntegrateTax();
        this.centralTax += itemHsn.getCentralTax();
        this.stateTax += itemHsn.getStateTax();
        this.cess += itemHsn.getCess();
    }
}
