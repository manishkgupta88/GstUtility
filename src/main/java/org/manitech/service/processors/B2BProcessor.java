package org.manitech.service.processors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.manitech.model.B2BSheet;
import org.manitech.model.DataPair;
import org.manitech.model.GstSheet;

import java.util.LinkedList;

/**
 * User : Manish K. Gupta
 */

public class B2BProcessor extends AbstractExcelProcessor {
    private static final Logger logger = LogManager.getLogger(B2BProcessor.class);

    @Override
    public GstSheet getGstSheetObj() {
        return new B2BSheet();
    }

    @Override
    public void sheetMerge(GstSheet gstSheet) {
        if (gstSheet == null || CollectionUtils.isEmpty(gstSheet.getRecords())) {
            return;
        }
        logger.info("Updating the records for B2B Sheets");
        for (LinkedList<DataPair> record : gstSheet.getRecords()) {
            if (CollectionUtils.isEmpty(record)) {
                continue;
            }
            for (int i = 0; i < gstSheet.getHeaderCount(); i++) {
                if (i == 5 && record.size() > 5) {
                    DataPair dataPair = record.get(5);
                    dataPair.setValue("07-Delhi");
                }
            }
        }
    }
}
