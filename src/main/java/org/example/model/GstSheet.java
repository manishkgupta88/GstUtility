package org.example.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User : Manish K. Gupta
 */

@Data
@NoArgsConstructor
public abstract class GstSheet implements Serializable {
    private String name;
    private LinkedList<DataPair> summaryList = new LinkedList<DataPair>();
    private LinkedList<DataPair> rowPairs = new LinkedList<DataPair>();
    private LinkedList<DataPair> columnPairs = new LinkedList<DataPair>();
    private LinkedList<DataPair> tableHeaders = new LinkedList<DataPair>();
    private List<List<String>> records = new ArrayList<>();

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
        return false;
    }
}
