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
public class CdnurSheet implements GstSheet {

    @Serial
    private static final long serialVersionUID = 6962105547943529654L;

    private DataPair title;
    private DataPair notes;
    private DataPair noteValue;
    private DataPair taxableValue;
    private DataPair totalCess;
    private int numOfNotes;
    private Double totalNoteValue;
    private Double totalTaxableValue;
    private Double totalCessAmount;
    private List<CdnurRecord> records;
}
