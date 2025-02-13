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
public class HsnSheet implements GstSheet {

    @Serial
    private static final long serialVersionUID = 6962105547943529654L;
    private DataPair title;
    private DataPair hsnCount;
    private DataPair totalValue;
    private DataPair totalTaxableValue;
    private DataPair totalIntegratedTax;
    private DataPair totalCentralTax;
    private DataPair totalStateTax;
    private DataPair totalCess;
    private int numOfHsn;
    private Double total;
    private Double taxableValue;
    private Double integrateTax;
    private Double centralTax;
    private Double stateTax;
    private Double cess;
    private List<HsnRecord> records;
}
