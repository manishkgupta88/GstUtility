package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * User : Manish K. Gupta
 */

@Data
@NoArgsConstructor
public abstract class GstSheet implements Serializable {
    private String name;
    private LinkedList<DataPair> summaryList = new LinkedList<>();
    private LinkedList<LinkedList<DataPair>> rowPairs = new LinkedList<>();
    private LinkedList<DataPair> columnPairs = new LinkedList<>();
    private LinkedList<DataPair> tableHeaders = new LinkedList<>();
    private LinkedList<LinkedList<DataPair>> records = new LinkedList<>();

    public abstract int getRowPairCount();

    public abstract int getHeaderCount();

    public abstract int getDataStartRow();

    public abstract int getColumnPairCount();

    public abstract int getCpRow();

    public abstract int getSummaryRow();

    public int[] getUniqueCountIndexes() {
        return null;
    }

    public boolean isSummaryInLastRow() {
        return getSummaryRow() == -1;
    }
}
