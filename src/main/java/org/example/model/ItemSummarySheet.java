package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serial;
import java.util.Map;

/**
 * User : Manish K. Gupta
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ItemSummarySheet extends GstSheet {

    @Serial
    private static final long serialVersionUID = 6962105547943529654L;
    private final int rowPairCount = 1;
    private final int cpRow = 1;
    private final int dataStartRow = 4;
    private final int summaryRow = 2;
    private final int columnPairCount = 11;
    private final int headerCount = 11;
    private Map<ItemHsnKey, ItemHsn> hsnMap;

    public ItemHsnKey getItemHsnKey(String hsn, String rate) {
        return new ItemHsnKey(hsn, NumberUtils.toInt(rate));
    }
}
