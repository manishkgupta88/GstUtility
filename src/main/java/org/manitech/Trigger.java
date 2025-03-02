package org.manitech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.manitech.forms.IntroForm;
import org.manitech.service.ExcelConsolidator;
import org.manitech.util.Constants;
import org.manitech.util.Helper;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URISyntaxException;


public class Trigger {
    private static final Logger logger = LogManager.getLogger(Trigger.class);

    public static void main(String[] args) throws URISyntaxException {
        logger.info("Starting trigger");
        String folderPath = "Q:\\src\\intellij\\GstUtility\\src\\resources\\gst\\files\\";
        logger.info("Using path: " + folderPath);
        triggerForms();
        //ExcelConsolidator consolidator = new ExcelConsolidator();
        //consolidator.consolidate(folderPath);
        logger.info("Completed");
    }

    private static void triggerForms() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Unable to set the look and feel", e);
        }
        SwingUtilities.invokeLater(() -> {
            IntroForm form = new IntroForm();  // Replace with your form class name
        });
    }
}