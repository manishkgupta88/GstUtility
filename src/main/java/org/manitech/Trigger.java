package org.manitech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.manitech.forms.IntroForm;
import org.manitech.util.PomVersionReader;

import javax.swing.*;


public class Trigger {
    private static final Logger logger = LogManager.getLogger(Trigger.class);

    public static void main(String[] args) throws Exception {
        logger.info("Starting trigger");
        String version = PomVersionReader.getVersion();
        triggerForms(version);
        /*String folderPath = "Q:\\src\\intellij\\GstUtility\\src\\resources\\gst\\files\\";
        logger.info("Using path: " + folderPath);
        ExcelConsolidator consolidator = new ExcelConsolidator();
        consolidator.consolidate(folderPath);*/
        logger.info("Completed");
    }

    private static void triggerForms(String version) {
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
            IntroForm form = new IntroForm(version);  // Replace with your form class name
        });
    }
}