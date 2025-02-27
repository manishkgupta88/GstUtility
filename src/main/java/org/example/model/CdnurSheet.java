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
public class CdnurSheet extends GstSheet {

    @Serial
    private static final long serialVersionUID = 6962105547943529654L;
    private final int rowPairCount = 1;
    private final int cpRow = 1;
    private final int dataStartRow = 4;
    private final int summaryRow = 2;
    private final int columnPairCount = 10;
    private final int headerCount = 10;
    private final int[] uniqueCountIndexes = {1};
}
