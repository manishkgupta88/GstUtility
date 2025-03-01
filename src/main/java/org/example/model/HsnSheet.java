package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * User : Manish K. Gupta
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class HsnSheet extends GstSheet {

    @Serial
    private static final long serialVersionUID = 6962105547943529654L;
    private final int rowPairCount = 1;
    private final int cpRow = 1;
    private final int dataStartRow = 4;
    private final int summaryRow = 2;
    private final int columnPairCount = 11;
    private final int headerCount = 11;
    private final int[] uniqueCountIndexes = {0};
    private final int[] textTypeCells = {0};
}
