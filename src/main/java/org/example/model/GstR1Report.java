package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * User : Manish K. Gupta
 */
@NoArgsConstructor
@Accessors(chain = true)
@Getter
public class GstR1Report extends GstSheet {
    @Serial
    private static final long serialVersionUID = 9024343212969550963L;
    private final int rowPairCount = 8;
    private final int headerCount = 15;
    private final int dataStartRow = 9;
    private final boolean summaryInLastRow = true;
    private final int cpRow = -1;
    private final int columnPairCount = -1;
    private final int summaryRow = -1;
}
