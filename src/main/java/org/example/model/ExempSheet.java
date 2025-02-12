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
public class ExempSheet implements GstSheet {

    @Serial
    private static final long serialVersionUID = 6962105547943529654L;
    private DataPair title;
    private DataPair nilRatedSupplies;
    private DataPair exemptedSupplies;
    private DataPair nonGstSupplies;
    private Double totalNilRatedSupplies;
    private Double totalExemptedSupplies;
    private Double totalNonGstSupplies;
    private List<ExempRecord> records;
}
